package controllers;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import models.Address;
import models.Alert;
import models.AppUser;
import models.FileEntity;
import models.Locality;
import models.PrimaryCity;
import models.Role;
import models.Sex;
import models.State;
import models.clinic.ClinicInvite;
import models.clinic.ClinicUser;
import models.doctor.Appointment;
import models.doctor.AppointmentStatus;
import models.doctor.Clinic;
import models.doctor.Doctor;
import models.doctor.DoctorClinicInfo;
import models.patient.Patient;
import models.pharmacist.Pharmacy;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.RandomStringUtils;
import org.json.JSONArray;

import play.Logger;
import play.data.Form;
import play.libs.F.Function0;
import play.libs.F.Promise;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Http.MultipartFormData.FilePart;
import play.mvc.Result;
import utils.EmailService;
import utils.SMSService;
import actions.BasicAuth;
import actions.ConfirmAppUser;

import com.google.common.io.Files;

@BasicAuth
public class ClinicController extends Controller{

	public static Form<AppUser> registrationForm = Form.form(AppUser.class);
	public static Form<ClinicUser> clinicUserForm = Form.form(ClinicUser.class);

	public static Result searchDoctorsByEmail(){
		return ok(views.html.clinic.inviteDoctorsByEmailID.render());
	}

	/**
	 * @author lakshmi
	 * Action to invitation to the doctor
	 * POST    /secure-clinic/invite-doctor
	 */
	public static Result processSeachDoctorsByEmailId(){
		final String email = request().body().asFormUrlEncoded().get("email")[0].trim();
		final String confirmEmail = request().body().asFormUrlEncoded().get("confirmEmail")[0].trim();

		if(email.compareToIgnoreCase(confirmEmail) != 0){
			flash().put("alert", new Alert("alert-danger","Email Ids do not match").toString());
			return ClinicController.searchDoctorsByEmail();
		}

		final Clinic clinic = LoginController.getLoggedInUser().getClinicUser().clinic;
		final AppUser appUser = AppUser.find.where().ieq("email", email).findUnique();
		if((appUser != null) && (appUser.role.equals(Role.DOCTOR))){
			final int count = DoctorClinicInfo.find.where().eq("clinic", clinic).eq("doctor", appUser.getDoctor()).findRowCount();
			if(count>0){
				flash().put("alert",new Alert("alert-info"," Dr."+appUser.name+" is already associated with clinic "+clinic.name).toString());
			}
			else{
				final Doctor doctor = appUser.getDoctor();
				final List<ClinicInvite> pastInvites = ClinicInvite.getInvites(doctor, clinic);
				if(pastInvites.size() > 0){
					final ClinicInvite invite = pastInvites.get(0);
					invite.dateInvited = new Date();
					invite.invitedBy = LoginController.getLoggedInUser();
					invite.update();
					Promise.promise(new Function0<Integer>() {
						@Override
						public Integer apply() {
							final boolean result = EmailService.sendClinicInvitationConfirmationEmail(appUser, clinic, pastInvites.get(0).verificationCode);
							return 0;
						}
					});
					//flash().put("alert", new Alert("alert-info","An Invitation to "+email+" has already been sent on "+
					//		new SimpleDateFormat("dd-MMM-yyyy (hh:mm)").format(pastInvites.get(0).dateInvited)+".").toString());
					flash().put("alert",new Alert("alert-success","An Invitation has been sent to the Dr."+appUser.name).toString());
					return ClinicController.searchDoctorsByEmail();
				}
				final Random random = new SecureRandom();
				final byte[] array = new byte[32];
				random.nextBytes(array);
				final String verificationCode = Base64.encodeBase64String(array);
				if(verificationCode.contains("/")){
					verificationCode.replaceAll("/", "-");
				}

				final ClinicInvite invite = new ClinicInvite();
				invite.doctor = doctor;
				invite.clinic = clinic;
				invite.dateInvited = new Date();
				invite.invitedBy = LoginController.getLoggedInUser();
				invite.accepted = false;
				invite.verificationCode = verificationCode;
				invite.save();

				Promise.promise(new Function0<Integer>() {
					@Override
					public Integer apply() {
						final boolean result = EmailService.sendClinicInvitationConfirmationEmail(appUser, clinic, invite.verificationCode);
						return 0;
					}
				});
				// End of async
				flash().put("alert",new Alert("alert-success","An Invitation has been sent to the Dr."+appUser.name).toString());
			}
		}else{
			flash().put("alert",new Alert("alert-info","No doctor could be found with email id: "+email+"").toString());
		}
		return redirect(routes.ClinicController.searchDoctorsByEmail());

	}

	/**
	 * @author lakshmi
	 * Action to add doctor to the clinic
	 * GET		/secure-clinic/add-doctor/:docId
	 */
	@ConfirmAppUser
	public static Result addDoctorToTheClinic(final Long doctorId,final Long clinicId, final String verCode){
		final Clinic clinic = Clinic.find.byId(clinicId);
		final Doctor doctor = Doctor.find.byId(doctorId);
		final AppUser loggedInUser = LoginController.getLoggedInUser();

		//server-side check
		if(loggedInUser.getDoctor() == null || loggedInUser.getDoctor().id.longValue() != doctorId.longValue()){
			return redirect(routes.LoginController.processLogout());
		}

		final List<ClinicInvite> invites = ClinicInvite.getInvites(doctor, clinic);
		if(invites.size() == 0){
			flash().put("alert",new Alert("alert-danger","Unauthorized Operation.").toString());
			return Application.index();
		}
		if(invites.get(0).verificationCode.compareTo(verCode) != 0){
			flash().put("alert",new Alert("alert-danger","Unauthorized Operation.").toString());
			return Application.index();
		}

		if(invites.get(0).accepted){
			flash().put("alert",new Alert("alert-danger","Already Accepted Invitation on "+new SimpleDateFormat("").format(invites.get(0).dateAccepted)+".").toString());
			return Application.index();
		}

		final ClinicInvite invite = invites.get(0);
		invite.acceptedBy = loggedInUser;
		invite.accepted = true;
		invite.dateAccepted = new Date();
		invite.update();

		final DoctorClinicInfo doctorClinicInfo = new DoctorClinicInfo();
		doctorClinicInfo.clinic = clinic;
		doctorClinicInfo.doctor = doctor;
		doctorClinicInfo.save();
		Promise.promise(new Function0<Integer>() {
			@Override
			public Integer apply() {
				final boolean result1 = EmailService.sendClinicInvitationSuccessEmail(doctor,clinic);
				return 0;
			}
		});
		// End of async
		return redirect(routes.UserActions.dashboard());
	}


	/**
	 * @author lakshmi
	 * Action to add doctor to the clinic
	 * GET		/secure-clinic/add-doctor/:docId
	 */
	public static Result getDoctors(){
		final Clinic clinic = LoginController.getLoggedInUser().getClinicUser().clinic;
		final List<DoctorClinicInfo> doctorClinicInfos = DoctorClinicInfo.find.where().eq("clinic", clinic).findList();
		Logger.info("size of the list "+doctorClinicInfos.size());
		return ok(views.html.clinic.clinicDoctors.render(doctorClinicInfos));
	}


	/**
	 * @author lakshmi
	 * Action for Clinic backgroundImage and profile
	 * Images of Clinic of loggedIn CLINIC_ADMIN
	 * POST	/secure-clinic/upload-clinic-images
	 */
	public static Result uploadClinicImageProcess() {
		try{
			final Clinic clinic = Clinic.find.byId(Long.parseLong(request().body().asMultipartFormData().asFormUrlEncoded().get("clinicId")[0]));
			// Server side validation
			if((clinic.id.longValue() != LoginController.getLoggedInUser(). getClinicUser().clinic.id.longValue()) || (!LoginController.getLoggedInUser().role.equals(Role.CLINIC_ADMIN))){
				Logger.warn("COULD NOT VALIDATE LOGGED IN USER TO PERFORM THIS TASK");
				Logger.warn("update attempted for Clinic id: "+clinic.id);
				Logger.warn("logged in AppUser: "+LoginController.getLoggedInUser().id);
				Logger.warn("logged in ClinicUser: "+LoginController.getLoggedInUser().getClinicUser().clinic);
				return redirect(routes.LoginController.processLogout());
			}else{
				flash().put("alert", new Alert("alert-info", "Choose an Image to upload.").toString());
			}
			//final String pattern="([^\\s]+(\\.(?i)(JPEG|jpg|png|gif|bmp))$)";
			if (request().body().asMultipartFormData().getFile("backgroundImage") != null) {
				final FilePart image = request().body().asMultipartFormData().getFile("backgroundImage");
				if(image.getContentType().equalsIgnoreCase("image/bmp")||image.getContentType().equalsIgnoreCase("image/png")||image.getContentType().equalsIgnoreCase("image/jpeg")||image.getContentType().equalsIgnoreCase("image/gif")){
					clinic.backgroudImage = Files.toByteArray(image.getFile());
				}else{
					flash().put("alert", new Alert("alert-info", "Sorry. Images Should Be In The Following Formats .JPEG,.jpg,.png,.gif,.bmp").toString());
				}
				clinic.update();
			}

			if (request().body().asMultipartFormData().getFile("profileImage") != null) {
				final FileEntity fileEntity = new FileEntity();
				final FilePart image = request().body().asMultipartFormData().getFile("profileImage");
				if(image.getContentType().equalsIgnoreCase("image/bmp")||image.getContentType().equalsIgnoreCase("image/png")||image.getContentType().equalsIgnoreCase("image/jpeg")||image.getContentType().equalsIgnoreCase("image/gif")){
					fileEntity.fileName = image.getFilename();
					fileEntity.mimeType = image.getContentType();
					fileEntity.byteContent = Files.toByteArray(image.getFile());
					fileEntity.save();
					final Long imageId=fileEntity.id;
					clinic.profileImageList.add(FileEntity.find.byId(imageId));
					clinic.update();
				}else{
					flash().put("alert", new Alert("alert-info", "Sorry. Images Should Be In The Following Formats .JPEG,.jpg,.png,.gif,.bmp").toString());
				}
			} else {
				Logger.info("BG IMAGE NULL");
				flash().put("alert", new Alert("alert-info", "Choose an Image to upload.").toString());
			}
		}catch(final Exception e){
			e.printStackTrace();
			Logger.error("");
			flash().put("alert", new Alert("alert-danger", "Sorry. Something went wrong. Please try again.").toString());
		}
		//return ok(views.html.pharmacist.pharmacy_profile.render(pharmacy.inventoryList, pharmacy));
		return redirect(routes.UserActions.dashboard());

	}

	/**
	 * @author lakshmi
	 * Action to remove profileImage of Clinic
	 * GET/secure-clinic/remove-image/:clinicId/:fileId
	 */
	public static Result removeClinicImage(final Long clinicId,final Long imageId){
		final Clinic clinic = Clinic.find.byId(clinicId);
		Logger.info("before list size="+clinic.profileImageList.size());
		final FileEntity image = FileEntity.find.byId(imageId);
		clinic.profileImageList.remove(image);
		clinic.update();
		//image.delete();
		Logger.info("after list size="+clinic.profileImageList.size());
		//		return ok(views.html.pharmacist.pharmacy_profile.render(pharmacy.inventoryList, pharmacy));
		return redirect(routes.UserActions.dashboard());
	}

	/**
	 * @author : lakshmi
	 * POST	/secure-clinic/basic-update
	 * Action to update the basic details(like name & brief description etc) of Clinic
	 */

	public static Result clinicBasicUpdate() {
		try{
			final Map<String, String[]> requestMap = request().body().asFormUrlEncoded();
			final Clinic clinic = Clinic.find.byId(Long.parseLong(requestMap.get("clinicId")[0]));
			// Server side validation
			if((clinic.id.longValue() != LoginController.getLoggedInUser().getClinicUser().clinic.id.longValue()) || (!LoginController.getLoggedInUser().role.equals(Role.CLINIC_ADMIN))){
				Logger.warn("COULD NOT VALIDATE LOGGED IN USER TO PERFORM THIS TASK");
				Logger.warn("update attempted for Clinic id: "+clinic.id);
				Logger.warn("logged in AppUser: "+LoginController.getLoggedInUser().id);
				Logger.warn("logged in ClinicUser: "+LoginController.getLoggedInUser().getClinicUser().clinic.id);
				return redirect(routes.LoginController.processLogout());
			}
			if(requestMap.get("name") != null && (requestMap.get("name")[0].trim().compareToIgnoreCase("")!=0)){
				clinic.name = requestMap.get("name")[0].trim();
			}
			if(requestMap.get("description") != null && (requestMap.get("description")[0].trim().compareToIgnoreCase("")!=0)){
				clinic.description = requestMap.get("description")[0].trim();
			}
			if(requestMap.get("slugUrl") != null && !(requestMap.get("slugUrl")[0].trim().isEmpty())){
				final String newSlug = requestMap.get("slugUrl")[0].trim();
				if(!newSlug.matches("^[a-z0-9\\-]+$")){
					flash().put("alert", new Alert("alert-danger", "Invalid charactrer provided in Url.").toString());
					return redirect(routes.UserActions.dashboard());
				}
				if(requestMap.get("slugUrl")[0].trim().compareToIgnoreCase(clinic.slugUrl) != 0){
					final int availableSlug = Pharmacy.find.where().eq("slugUrl", requestMap.get("slugUrl")[0].trim()).findRowCount();
					if(availableSlug == 0){
						clinic.slugUrl = requestMap.get("slugUrl")[0].trim();
					}else{
						flash().put("alert", new Alert("alert-danger", "Sorry, Requested URL is not available.").toString());
						return redirect(routes.UserActions.dashboard());
					}
				}
			}

			clinic.update();
		}
		catch (final Exception e){
			e.printStackTrace();
			flash().put("alert", new Alert("alert-danger", "Sorry. Something went wrong. Please try again.").toString());
		}
		return redirect(routes.UserActions.dashboard());
	}

	/**
	 * @author : lakshmi
	 * POST	/secure-clinic/address-update
	 * Action to update the address details of Clinic
	 */

	public static Result clinicAddressUpdate() {

		try{
			final Map<String, String[]> requestMap = request().body().asFormUrlEncoded();
			final Clinic clinic = Clinic.find.byId(Long.parseLong(requestMap.get("clinicId")[0]));
			// Server side validation
			if((clinic.id.longValue() != LoginController.getLoggedInUser().getClinicUser().clinic.id.longValue()) || (!LoginController.getLoggedInUser().role.equals(Role.CLINIC_ADMIN))){
				Logger.warn("COULD NOT VALIDATE LOGGED IN USER TO PERFORM THIS TASK");
				Logger.warn("update attempted for Clinic id: "+clinic.id);
				Logger.warn("logged in AppUser: "+LoginController.getLoggedInUser().id);
				Logger.warn("logged in ClinicUser: "+LoginController.getLoggedInUser().getClinicUser().clinic.id);
				return redirect(routes.LoginController.processLogout());
			}
			Logger.info("map size"+requestMap.toString());
			if(clinic.address == null){
				final Address address = new Address();
				address.save();
				clinic.address = address;
			}
			if(requestMap.get("contactPerson") != null && !(requestMap.get("contactPerson")[0].trim().isEmpty())){
				clinic.contactPersonName = requestMap.get("contactPerson")[0];
			}
			if(requestMap.get("addressLine1") != null && !(requestMap.get("addressLine1")[0].trim().isEmpty())){
				clinic.address.addressLine1 = requestMap.get("addressLine1")[0];
			}
			if(requestMap.get("area") != null && !(requestMap.get("area")[0].trim().isEmpty())){
				clinic.address.area = requestMap.get("area")[0];
			}
			if(requestMap.get("pincode") != null && !(requestMap.get("pincode")[0].trim().isEmpty())){
				clinic.address.pinCode = requestMap.get("pincode")[0];
			}
			if(requestMap.get("state") != null && !(requestMap.get("state")[0].trim().isEmpty())){
				clinic.address.state = Enum.valueOf(State.class,requestMap.get("state")[0]);
			}
			if(requestMap.get("locality") != null && !(requestMap.get("locality")[0].trim().isEmpty())){
				clinic.address.locality = Locality.find.byId(Long.parseLong(requestMap.get("locality")[0]));
			}
			if(requestMap.get("city") != null && !(requestMap.get("city")[0].trim().isEmpty())){
				clinic.address.primaryCity = PrimaryCity.find.byId(Long.parseLong(requestMap.get("city")[0]));
			}
			if(requestMap.get("latitude") != null && !(requestMap.get("latitude")[0].trim().isEmpty())){
				clinic.address.latitude = requestMap.get("latitude")[0];
			}
			if(requestMap.get("longitude") != null && !(requestMap.get("longitude")[0].trim().isEmpty())){
				clinic.address.longitude = requestMap.get("longitude")[0];
			}
			if(requestMap.get("contactNo") != null && !(requestMap.get("contactNo")[0].trim().isEmpty())){
				clinic.contactNo = requestMap.get("contactNo")[0];
			}
			clinic.address.update();
			clinic.update();

		}
		catch (final Exception e){
			e.printStackTrace();
			flash().put("alert", new Alert("alert-danger", "Sorry. Something went wrong. Please try again.").toString());
		}
		return redirect(routes.UserActions.dashboard());
	}
	/**
	 * @author lakshmi
	 * Action to render addClinicUserForm
	 * GET/secure-clinic/add-clinic-user
	 */

	public static Result addClinicUserForm(){
		if(LoginController.getLoggedInUserRole().equals(Role.CLINIC_ADMIN.toString())){
			return ok(views.html.clinic.addClinicUser.render());
		}else{
			flash().put("alert", new Alert("alert-info", "Sorry. Clinic Admin only can access this.").toString());
			return redirect(routes.UserActions.dashboard());
		}
	}
	/**
	 * @author lakshmi
	 * Action to save clinic user
	 * POST/secure-clinic/save-clinic-user
	 */
	public static Result saveClinicUser(){
		if(LoginController.getLoggedInUserRole().equals(Role.CLINIC_ADMIN.toString())){
			final ClinicUser clinicAdmin = LoginController.getLoggedInUser().getClinicUser();
			final Map<String, String[]> requestMap = request().body().asFormUrlEncoded();
			final AppUser appUser = new AppUser();
			final ClinicUser clinicUser = new ClinicUser();
			String password = "";
			if((requestMap.get("name") != null) && !(requestMap.get("name")[0].trim().isEmpty())){
				appUser.name = requestMap.get("name")[0];
			}
			if((requestMap.get("email") != null) && !(requestMap.get("email")[0].trim().isEmpty())){
				final int appUsers = AppUser.find.where().ieq("email", requestMap.get("email")[0].trim()).findRowCount();
				if(appUsers == 0){
					appUser.email = requestMap.get("email")[0].trim().toLowerCase();
				}else{
					flash().put("alert", new Alert("alert-info", requestMap.get("email")[0].trim()+" is already existed in the system. Try with another Email Id.").toString());
					return ok(views.html.clinic.addClinicUser.render());
				}
			}
			if((requestMap.get("password") != null) && !(requestMap.get("password")[0].trim().isEmpty())){
				password = requestMap.get("password")[0];
				try {

					final Random random = new SecureRandom();
					final byte[] saltArray = new byte[32];
					random.nextBytes(saltArray);
					final String randomSalt = Base64.encodeBase64String(saltArray);

					final String passwordWithSalt = password+randomSalt;
					final MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
					final byte[] passBytes = passwordWithSalt.getBytes();
					final String hashedPasswordWithSalt = Base64.encodeBase64String(sha256.digest(passBytes));

					appUser.salt = randomSalt;
					appUser.password = hashedPasswordWithSalt;

				} catch (final Exception e) {
					Logger.error("ERROR WHILE CREATING SHA2 HASH");
					e.printStackTrace();
				}

			}
			if((requestMap.get("mobileNumber") != null) && !(requestMap.get("mobileNumber")[0].trim().isEmpty())){
				appUser.mobileNumber = Long.parseLong(requestMap.get("mobileNumber")[0]);
			}
			if((requestMap.get("sex") != null) && !(requestMap.get("sex")[0].trim().isEmpty())){
				appUser.sex = Enum.valueOf(Sex.class,requestMap.get("sex")[0]);
			}
			if(requestMap.get("dob")[0]!=null ){
				try {
					appUser.dob = new SimpleDateFormat("dd-MM-yyyy").parse(requestMap.get("dob")[0].trim());
					Logger.debug(new SimpleDateFormat("dd-MM-yyyy").parse(requestMap.get("dob")[0].trim()).toString());
					Logger.debug(""+appUser.dob);
				} catch (final Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			appUser.role = Role.CLINIC_USER;
			appUser.save();
			clinicUser.appUser = appUser;
			clinicUser.clinic = clinicAdmin.clinic;
			clinicUser.save();
			// Async Execution
			Promise.promise(new Function0<Integer>() {
				//@Override
				@Override
				public Integer apply() {
					int result = 0;
					if(!EmailService.sendClinicUserConfirmationMail(clinicUser, LoginController.getLoggedInUser().getClinicUser(),requestMap.get("password")[0])){
						result=1;
					}

					return result;
				}
			});
			// End of async

			final StringBuilder smsMessage = new StringBuilder();

			smsMessage.append(" Hi "+clinicUser.appUser.name+" You have been added as a Clinic User at "+clinicAdmin.clinic.name+" By "+clinicAdmin.appUser.name);
			smsMessage.append(" your email id is "+ clinicUser.appUser.email+" and");
			smsMessage.append(" your password is "+password+" .");
			SMSService.sendSMS(clinicUser.appUser.mobileNumber.toString(), smsMessage.toString());
			flash().put("alert", new Alert("alert-success", clinicUser.appUser.name+" Added as Clinic User at "+clinicAdmin.clinic.name+".").toString());
			return redirect(routes.ClinicController.addClinicUserForm());

		}else{
			flash().put("alert", new Alert("alert-info", "Sorry. Clinic Admin only can access this.").toString());
			return redirect(routes.UserActions.dashboard());
		}
	}
	/**
	 * @author lakshmi
	 * Action to List out all ClinicUsers
	 * GET/secure-clinic/all-clinic-users
	 */
	public static Result listClinicUsers(){
		final ClinicUser clinicUser = LoginController.getLoggedInUser().getClinicUser();
		if(LoginController.getLoggedInUserRole().equals(Role.CLINIC_ADMIN.toString())){
			final List<ClinicUser> clinicUsers = ClinicUser.find.where().eq("clinic", clinicUser.clinic)
					.eq("appUser.role", Role.CLINIC_USER)
					.findList();
			return ok(views.html.clinic.clinicUserList.render(clinicUsers));
		}else{
			flash().put("alert", new Alert("alert-info", "Sorry. Clinic Admin only can access this.").toString());
			return redirect(routes.UserActions.dashboard());
		}
	}
	/**
	 * @author lakshmi
	 * Action to render editClinicUser
	 * GET/secure-clinic/edit-clinic-user/:clinicUserId
	 */
	public static Result editClinicUser(final Long clinicUserId){
		final ClinicUser clinicUser = ClinicUser.find.byId(clinicUserId);
		return ok(views.html.clinic.editClinicUser.render(clinicUser));
	}
	/**
	 * @author lakshmi
	 * Action to Update ClinicUser Info
	 * POST/secure-clinic/update-clinic-user
	 */
	public static Result updateClinicUser(){
		if(LoginController.getLoggedInUserRole().equals(Role.CLINIC_ADMIN.toString())){
			final Map<String, String[]> requestMap = request().body().asFormUrlEncoded();
			final ClinicUser clinicUser = ClinicUser.find.byId(Long.parseLong(requestMap.get("clinicUserId")[0]));
			if((requestMap.get("name") != null) && !(requestMap.get("name")[0].trim().isEmpty())){
				clinicUser.appUser.name = requestMap.get("name")[0];
			}
			if((requestMap.get("email") != null) && !(requestMap.get("email")[0].trim().isEmpty())){
				clinicUser.appUser.email = requestMap.get("email")[0];
			}
			if((requestMap.get("password") != null) && !(requestMap.get("password")[0].trim().isEmpty())){
				final String password = requestMap.get("password")[0];
				try {

					final Random random = new SecureRandom();
					final byte[] saltArray = new byte[32];
					random.nextBytes(saltArray);
					final String randomSalt = Base64.encodeBase64String(saltArray);

					final String passwordWithSalt = password+randomSalt;
					final MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
					final byte[] passBytes = passwordWithSalt.getBytes();
					final String hashedPasswordWithSalt = Base64.encodeBase64String(sha256.digest(passBytes));

					clinicUser.appUser.salt = randomSalt;
					clinicUser.appUser.password = hashedPasswordWithSalt;

				} catch (final Exception e) {
					Logger.error("ERROR WHILE CREATING SHA2 HASH");
					e.printStackTrace();
				}
			}
			if((requestMap.get("mobileNumber") != null) && !(requestMap.get("mobileNumber")[0].trim().isEmpty())){
				clinicUser.appUser.mobileNumber = Long.parseLong(requestMap.get("mobileNumber")[0]);
			}
			if((requestMap.get("role") != null) && !(requestMap.get("role")[0].trim().isEmpty())){
				clinicUser.appUser.role = Enum.valueOf(Role.class,requestMap.get("role")[0]);
			}
			if((requestMap.get("sex") != null) && !(requestMap.get("sex")[0].trim().isEmpty())){
				clinicUser.appUser.sex = Enum.valueOf(Sex.class,requestMap.get("sex")[0]);
			}
			if(requestMap.get("dob")[0]!=null ){
				try {
					clinicUser.appUser.dob = new SimpleDateFormat("dd-MM-yyyy").parse(requestMap.get("dob")[0].trim());
					Logger.debug(new SimpleDateFormat("dd-MM-yyyy").parse(requestMap.get("dob")[0].trim()).toString());
					Logger.debug(""+clinicUser.appUser.dob);
				} catch (final Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			clinicUser.appUser.update();
			flash().put("alert", new Alert("alert-success",clinicUser.appUser.name+" Updated Successfully. ").toString());
			return redirect(routes.ClinicController.listClinicUsers());
		}else{
			flash().put("alert", new Alert("alert-info", "Sorry. Clinic Admin only can access this.").toString());
			return redirect(routes.UserActions.dashboard());
		}
	}
	/**
	 * @author lakshmi
	 * Action to get list of DoctorClinicInfos
	 * GET/secure-clinic/get-clinic-doctors/:clinicUserId
	 */
	public static Result getDoctorClinicInfos(final Long clinicUserId){
		if(LoginController.getLoggedInUserRole().equals(Role.CLINIC_ADMIN.toString())){
			final ClinicUser clinicUser = ClinicUser.find.byId(clinicUserId);
			Logger.info(DoctorClinicInfo.find.where().eq("clinic", clinicUser.clinic).findList().size()+"size now");
			return ok(views.html.clinic.doctorClinicInfos.render(DoctorClinicInfo.find.where().eq("clinic", clinicUser.clinic).findList(),clinicUser));
		}else{
			flash().put("alert", new Alert("alert-info", "Sorry. Clinic Admin only can access this.").toString());
			return redirect(routes.UserActions.dashboard());
		}
	}
	/**
	 * @author lakshmi
	 * Action to assign doctors to the clinicUser
	 * POST	/secure-clinic/assign-clinic-doctors/:clinicUserId
	 */
	public static Result assaignDoctorsToClinicUser(final Long clinicUserId){
		if(LoginController.getLoggedInUserRole().equals(Role.CLINIC_ADMIN.toString())){
			final ClinicUser clinicUser = ClinicUser.find.byId(clinicUserId);
			final String[] docIds = request().body().asFormUrlEncoded().get("doctors");
			if(docIds != null && docIds.length > 0){
				for (final String docId : docIds) {
					final Doctor doctor = Doctor.find.byId(Long.parseLong(docId));
					if(!(clinicUser.doctorsList.contains(doctor))){
						clinicUser.doctorsList.add(doctor);
					}
				}
				clinicUser.update();
			}else{
				flash().put("alert", new Alert("alert-info", "Select Doctors to assign.").toString());
			}
			return redirect(routes.ClinicController.listClinicUsers());
		}else{
			flash().put("alert", new Alert("alert-info", "Sorry. Clinic Admin only can access this.").toString());
			return redirect(routes.UserActions.dashboard());
		}
	}
	/**
	 * @author lakshmi
	 * Action to remove ClinicUser
	 * GET/secure-clinic/remove-clinic-user/:clinicUserId
	 */
	public static Result removeClinicUser(final Long clinicUserId){
		if(LoginController.getLoggedInUserRole().equals(Role.CLINIC_ADMIN.toString())){
			final ClinicUser clinicUser = ClinicUser.find.byId(clinicUserId);
			final Clinic clinic = clinicUser.clinic;
			if(clinic.clinicUserList.contains(clinicUser)){
				clinic.clinicUserList.remove(clinicUser);
				clinic.update();
			}
			clinicUser.delete();
			flash().put("alert", new Alert("alert-success", clinicUser.appUser.name+" Deleted Successfully.").toString());
			return redirect(routes.ClinicController.listClinicUsers());
		}else{
			flash().put("alert", new Alert("alert-info", "Sorry. Clinic Admin only can access this.").toString());
			return redirect(routes.UserActions.dashboard());
		}

	}
	/**
	 * @author lakshmi
	 * Action to add Calender to the Clinic.
	 * GET/secure-clinic/weekly-appointments/:docClinicInfoId
	 */
	public static Result viewClinicWeeklyAppointments(final Long docClincInfoId){
		final DoctorClinicInfo clinicInfo = DoctorClinicInfo.find.byId(docClincInfoId);
		return ok(views.html.clinic.viewClinicWeeklyAppointments.render(clinicInfo.slot,clinicInfo));
	}

	/**
	 * @author lakshmi
	 * Action to get CalendarEventsJson
	 * GET/secure-clinic/weekly-appointments-json/:docClinicInfoId
	 */
	@BodyParser.Of(BodyParser.Json.class)
	public static Result getClinicCalendarEventsJson(final Long docClinicId) {

		final String start = request().getQueryString("start");
		final String end = request().getQueryString("end");
		final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		final List<Appointment> appointments = new ArrayList<Appointment>();
		try{
			final Date startDate = sdf.parse(start);
			final Date endDate = sdf.parse(end);
			final DoctorClinicInfo docclinicInfo = DoctorClinicInfo.find.byId(docClinicId);
			final List<AppointmentStatus> statusList = new ArrayList<AppointmentStatus>();
			statusList.add(AppointmentStatus.APPROVED);
			statusList.add(AppointmentStatus.SERVED);
			appointments.addAll(
					Appointment.find.where()
					.eq("doctorClinicInfo", docclinicInfo)
					.in("appointmentStatus", statusList)
					.ge("appointmentTime", startDate)
					.le("appointmentTime", endDate).findList()
					);
		}
		catch(final Exception e){
			Logger.error("somethings wrong with start/end date");
		}
		final List<HashMap<String,String>> result = new ArrayList<HashMap<String,String>>();
		final Calendar cal = Calendar.getInstance();
		for (final Appointment appointment : appointments) {
			final HashMap<String,String> map = new HashMap<String,String>();
			map.put("id", appointment.id+"");
			map.put("title", appointment.requestedBy.name+" ("+appointment.requestedBy.getPatient().getSexAndAge()+") - "+appointment.problemStatement);
			map.put("start", new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").format(appointment.appointmentTime));
			cal.setTime(appointment.appointmentTime);
			cal.add(Calendar.MINUTE, appointment.doctorClinicInfo.slot);
			map.put("end", new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").format(cal.getTime()));
			if(appointment.appointmentStatus.equals(AppointmentStatus.APPROVED)){
				//map.put("url", routes.DoctorController.showPrescriptionForm(appointment.id).url());
				map.put("color", "#001F7A");
			}
			if(appointment.appointmentStatus.equals(AppointmentStatus.SERVED)){
				//map.put("url", routes.DoctorController.showPrescription(appointment.getPrescription().id).url());
			}
			result.add(map);
		}
		final JSONArray jsonArray = new JSONArray(result);
		return ok(jsonArray.toString());

	}
	/**
	 * @author lakshmi
	 * Action to render verifyAppuserForAppointment
	 * GET/secure-clinic/book-appointment-form/:docClinicId
	 */
	public static Result bookAppointmentForm(final Long docClinicInfoId){
		final DoctorClinicInfo doctorClinicInfo = DoctorClinicInfo.find.byId(docClinicInfoId);
		return ok(views.html.clinic.verifyAppuserForAppointment.render(doctorClinicInfo));
	}
	/**
	 * @author lakshmi
	 * Action to send verification code to the s
	 * GET/secure-clinic/verify-appUser/:emailId/:docClinicId
	 */
	@SuppressWarnings("unchecked")
	public static Result sendVerificationToAppUser(final String emailId,final Long docClinicInfoId){
		final AppUser appUser = AppUser.find.where().ieq("email", emailId.toLowerCase().trim()).findUnique();
		if((appUser == null)){
			return ok("1");
		}
		else if((appUser != null) && (appUser.role.equals(Role.PATIENT))){
			final String verifivcationCode = RandomStringUtils.randomNumeric(5).toLowerCase();
			final Long patientId = appUser.getPatient().id;
			SMSService.sendSMS(Long.toString(appUser.mobileNumber),"Your mobile confirmation code is "+verifivcationCode);
			Logger.info("Confirmation code sent to: "+appUser.mobileNumber+" code:"+verifivcationCode);
			@SuppressWarnings("rawtypes")
			final List list = new ArrayList();
			list.add(verifivcationCode);
			list.add(patientId);
			return ok(Json.toJson(list));
		}
		else{
			return ok("0");
		}

	}
	/**
	 * @author lakshmi
	 * Action to render addNewPatientFromClinic
	 * GET/secure-clinic/new-patient-form/:docClinicId
	 */
	public static Result getNewPatientForm(final Long docClinicId,final String email){
		return ok(views.html.clinic.addNewPatientFromClinic.render(null,DoctorClinicInfo.find.byId(docClinicId),email));
	}
	/**
	 * @author lakshmi
	 * Action to create new Patient from the Clinic by CLINIC_USER
	 * POST/secure-clinic/save-patient/:docClinicId
	 */
	public static Result savePatientProfile(final Long docClinicId){
		final Map<String,String[]> requestMap = request().body().asFormUrlEncoded();
		final AppUser appUser = new AppUser();
		if((requestMap.get("name")[0]!= null) && !(requestMap.get("name")[0].trim().isEmpty())){
			appUser.name = requestMap.get("name")[0];
		}
		if((requestMap.get("email")[0]!= null) && !(requestMap.get("email")[0].trim().isEmpty())){
			final int appUsers = AppUser.find.where().ieq("email", requestMap.get("email")[0].trim()).findRowCount();
			if(appUsers == 0){
				appUser.email = requestMap.get("email")[0].trim().toLowerCase();
			}else{
				flash().put("alert", new Alert("alert-info", requestMap.get("email")[0].trim()+" is already existed in the system. Try with another Email Id.").toString());
				return ok(views.html.clinic.addNewPatientFromClinic.render(null,DoctorClinicInfo.find.byId(docClinicId),requestMap.get("email")[0].trim()));
			}

		}
		if((requestMap.get("contactNo")[0]!= null) && !(requestMap.get("contactNo")[0].trim().isEmpty())){
			appUser.mobileNumber = Long.parseLong(requestMap.get("contactNo")[0]);
		}
		if((requestMap.get("sex")[0]!= null) && !(requestMap.get("sex")[0].trim().isEmpty())){
			appUser.sex = Enum.valueOf(Sex.class,(requestMap.get("sex")[0]));
		}
		if(requestMap.get("dob")[0]!=null ){
			try {
				appUser.dob = new SimpleDateFormat("dd-MM-yyyy").parse(requestMap.get("dob")[0].trim());
				Logger.debug(new SimpleDateFormat("dd-MM-yyyy").parse(requestMap.get("dob")[0].trim()).toString());
				Logger.debug(""+appUser.dob);
			} catch (final Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		final String pwd = RandomStringUtils.randomAlphanumeric(4).toLowerCase();
		try {
			final Random random = new SecureRandom();
			final byte[] saltArray = new byte[32];
			random.nextBytes(saltArray);
			final String randomSalt = Base64.encodeBase64String(saltArray);
			final String passwordWithSalt = pwd+randomSalt;
			final MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
			final byte[] passBytes = passwordWithSalt.getBytes();
			final String hashedPasswordWithSalt = Base64.encodeBase64String(sha256.digest(passBytes));

			appUser.salt = randomSalt;
			appUser.password = hashedPasswordWithSalt;

		} catch (final Exception e) {
			Logger.error("ERROR WHILE CREATING SHA2 HASH");
			e.printStackTrace();
		}

		appUser.role = Role.PATIENT;
		appUser.save();

		SMSService.sendSMS(Long.toString(appUser.mobileNumber), " An Account Has Been Created at mednetwork.in. Your Login Email ID Is "+appUser.email+" ,Password Is "+pwd);
		EmailService.sendConfirmationEmail(appUser);
		SMSService.sendConfirmationSMS(appUser);
		final Patient patient = new Patient();
		patient.appUser = appUser;
		patient.save();
		return ok(views.html.clinic.addNewPatientFromClinic.render(appUser,DoctorClinicInfo.find.byId(docClinicId),""));
	}
	/**
	 * @author lakshmi
	 * Action to verify mobile confirmation code of patient
	 * POST/secure-clinic/verify-confirmation-code/:appUserId/:docClinicId
	 */
	public static Result verifyMobileConfirmationCode(final Long appUserId,final Long docClinicId){
		final AppUser appUser = AppUser.find.byId(appUserId);
		if((request().body().asFormUrlEncoded().get("code")[0]) != null && !(request().body().asFormUrlEncoded().get("code")[0].trim().isEmpty())){
			if(appUser.mobileNumberConfirmationKey.equals(request().body().asFormUrlEncoded().get("code")[0])){
				return redirect(routes.ClinicController.getClinicPatientAppointmentForm(docClinicId, appUser.getPatient().id));
			}else{
				flash().put("alert", new Alert("alert-info", "Mobile Confirmation Code is not matched. Please enter correct Code.").toString());
				return ok(views.html.clinic.addNewPatientFromClinic.render(appUser,DoctorClinicInfo.find.byId(docClinicId),""));
			}
		}else{
			flash().put("alert", new Alert("alert-info", "Enter Correct Confirmation Code.").toString());
			return ok(views.html.clinic.addNewPatientFromClinic.render(appUser,DoctorClinicInfo.find.byId(docClinicId),""));
		}
	}

	/**
	 * @author lakshmi
	 * Action to get clinicPatientAppointment Form
	 * GET/secure-clinic/book-appUser-appointment/:docClinicId/:patientId
	 */
	public static Result getClinicPatientAppointmentForm(final Long docClinicId,final Long patientId){
		final DoctorClinicInfo doctorClinicInfo = DoctorClinicInfo.find.byId(docClinicId);
		return ok(views.html.clinic.clinicPatientAppointment.render(doctorClinicInfo,Patient.find.byId(patientId)));
	}
	/**
	 * @author lakshmi
	 * Action to process ClinicPatientAppointment
	 * POST	/secure-clinic/save-appointment/:appointmentId/:patientId
	 */
	@ConfirmAppUser
	public static Result processClinicPatientAppointment(final Long appointmentId,final Long patientId) {
		final Patient patient = Patient.find.byId(patientId);
		final ClinicUser clinicUser = LoginController.getLoggedInUser().getClinicUser();
		final String remark=request().body().asFormUrlEncoded().get("remark")[0];
		Logger.warn(remark);
		final Appointment appointment = Appointment.find.byId(appointmentId);
		appointment.appointmentStatus = AppointmentStatus.APPROVED;
		appointment.problemStatement = remark;
		appointment.requestedBy = patient.appUser;
		appointment.bookedOn = new Date();
		appointment.update();
		flash().put("alert", new Alert("alert-success", "An Appointment Created By ."+ clinicUser.appUser.name+" For The Patient "+patient.appUser.name+"  at "+appointment.doctorClinicInfo.clinic.name+" with Dr "+appointment.doctorClinicInfo.doctor.appUser.name+" Successfully.").toString());

		// Async Execution
		Promise.promise(new Function0<Integer>() {
			//@Override
			@Override
			public Integer apply() {
				int result = 0;
				if(!EmailService.sendAppointmentConformMail(appointment.requestedBy, appointment.doctorClinicInfo.doctor.appUser, appointment)){
					result=1;
				}

				return result;
			}
		});
		// End of async

		final StringBuilder smsMessage = new StringBuilder();
		smsMessage.append("You have an appointment with Dr. "+appointment.doctorClinicInfo.doctor.appUser.name+" on ");
		smsMessage.append( new SimpleDateFormat("dd-MMM-yyyy").format(appointment.appointmentTime));
		smsMessage.append(" at "+ new SimpleDateFormat("HH:mm").format(appointment.appointmentTime));
		smsMessage.append(" at "+appointment.doctorClinicInfo.clinic.name+", "+appointment.doctorClinicInfo.clinic.address.area);
		SMSService.sendSMS(appointment.requestedBy.mobileNumber.toString(), smsMessage.toString());
		return redirect(routes.ClinicController.getDoctors());
	}
}






