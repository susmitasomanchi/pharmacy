package controllers;

import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.codec.binary.Base64;

import com.google.common.io.Files;

import actions.BasicAuth;
import actions.ConfirmAppUser;
import models.Address;
import models.Alert;
import models.AppUser;
import models.FileEntity;
import models.Role;
import models.State;
import models.bloodBank.BloodBank;
import models.clinic.ClinicInvite;
import models.doctor.Clinic;
import models.doctor.Doctor;
import models.doctor.DoctorClinicInfo;
import models.pharmacist.Pharmacy;
import play.Logger;
import play.libs.F.Function0;
import play.libs.F.Promise;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Http.MultipartFormData.FilePart;
import utils.EmailService;

@BasicAuth
public class ClinicController extends Controller{

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
					flash().put("alert", new Alert("alert-info","An Invitation to "+email+" has already been sent on "+new SimpleDateFormat("dd-MMM-yyyy (hh:mm)").format(pastInvites.get(0).dateInvited)+".").toString());
					return ClinicController.searchDoctorsByEmail();
				}
				final Random random = new SecureRandom();
				final byte[] array = new byte[32];
				random.nextBytes(array);
				final String verificationCode = Base64.encodeBase64String(array);

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
	 * Action for BloodBank backgroundImage and profile
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
	 * Action to remove profileImage of BloodBank
	 * GET/secure-blood-bank/remove-image/:bloodBankId/:fileId
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
			if(requestMap.get("contactPerson") != null && (requestMap.get("contactPerson")[0].trim().compareToIgnoreCase("")!=0)){
				clinic.contactPersonName = requestMap.get("contactPerson")[0];
			}
			if(requestMap.get("addressLine1") != null && (requestMap.get("addressLine1")[0].trim().compareToIgnoreCase("")!=0)){
				clinic.address.addressLine1 = requestMap.get("addressLine1")[0];
			}
			if(requestMap.get("city") != null && (requestMap.get("city")[0].trim().compareToIgnoreCase("")!=0)){
				clinic.address.city = requestMap.get("city")[0];
			}
			if(requestMap.get("area") != null && (requestMap.get("area")[0].trim().compareToIgnoreCase("")!=0)){
				clinic.address.area = requestMap.get("area")[0];
			}
			if(requestMap.get("pincode") != null && (requestMap.get("pincode")[0].trim().compareToIgnoreCase("")!=0)){
				clinic.address.pinCode = requestMap.get("pincode")[0];
			}
			if(requestMap.get("state") != null && (requestMap.get("state")[0].trim().compareToIgnoreCase("")!=0)){
				clinic.address.state = Enum.valueOf(State.class,requestMap.get("state")[0]);
			}
			if(requestMap.get("latitude") != null && (requestMap.get("latitude")[0].trim().compareToIgnoreCase("")!=0)){
				clinic.address.latitude = requestMap.get("latitude")[0];
			}
			if(requestMap.get("longitude") != null && (requestMap.get("longitude")[0].trim().compareToIgnoreCase("")!=0)){
				clinic.address.longitude = requestMap.get("longitude")[0];
			}
			if(requestMap.get("contactNo") != null && (requestMap.get("contactNo")[0].trim().compareToIgnoreCase("")!=0)){
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

}




