package controllers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map;

import models.Address;
import models.Alert;
import models.AppUser;
import models.BloodGroup;
import models.FileEntity;
import models.PrimaryCity;
import models.Role;
import models.State;
import models.bloodBank.BloodBank;
import models.bloodBank.BloodDonation;
import models.diagnostic.DiagnosticCentre;
import models.patient.Patient;
import play.Logger;
import play.mvc.Controller;
import play.mvc.Http.MultipartFormData.FilePart;
import play.mvc.Result;
import actions.BasicAuth;
import actions.ConfirmAppUser;

import com.avaje.ebean.ExpressionList;
import com.google.common.io.Files;

@BasicAuth
public class BloodBankController extends Controller{
	/**
	 * @author lakshmi
	 * Action to render bloodDonorsInPrimaryCity
	 * GET/secure-blood-bank/get-blood-donors
	 */
	@ConfirmAppUser
	public static Result getBloodDonorsInCityForm(){
		return ok(views.html.bloodBank.bloodDonorsInPrimaryCity.render(null));
	}
	/**
	 * @author lakshmi
	 * Action to find bloodDonorsInPrimaryCity
	 * POST/secure-blood-bank/list-blood-donors
	 */
	@ConfirmAppUser
	public static Result listBloodDonorsInCity(){

		final Map<String, String[]> requestMap = request().body().asFormUrlEncoded();
		/*Logger.info(""+requestMap.toString());
		Logger.info("vgytugij"+requestMap.get("bloodBankUserId")[0]);
		final BloodBankUser bloodBankUser = BloodBankUser.find.byId(Long.parseLong(requestMap.get("bloodBankUserId")[0]));
		// Server side validation
		if((LoginController.getLoggedInUser().getBloodBankAdmin().id.longValue()) != (bloodBankUser.id.longValue()) || (!LoginController.getLoggedInUser().role.equals(Role.BLOOD_BANK_ADMIN))){
			Logger.warn("COULD NOT VALIDATE LOGGED IN USER TO PERFORM THIS TASK");
			Logger.warn("logged in AppUser: "+LoginController.getLoggedInUser().id);
			Logger.warn("logged in BllodBankUser: "+LoginController.getLoggedInUser().getBloodBankAdmin().id);
			return redirect(routes.LoginController.processLogout());
		}*/
		final ExpressionList<Patient> patientQuery = Patient.find.where().eq("appUser.isBloodDonor", true);
		if(((requestMap.get("primaryCity")[0])!= null) && !((requestMap.get("primaryCity")[0]).trim().equalsIgnoreCase("any"))){
			Logger.info("inside primary city");
			patientQuery
			.eq("primaryCity", PrimaryCity.find.byId(Long.parseLong(requestMap.get("primaryCity")[0].trim())));
			Logger.info("inside primary city=="+patientQuery.findRowCount());
		}
		if(((requestMap.get("bloodGroup")[0])!= null) && !((requestMap.get("bloodGroup")[0]).trim().equalsIgnoreCase("any"))){
			Logger.info("inside blood Group");
			patientQuery
			.eq("appUser.bloodGroup",requestMap.get("bloodGroup")[0].trim());
			Logger.info("inside blood group=="+patientQuery.findRowCount());
		}
		if(((requestMap.get("sex")[0])!= null) && !((requestMap.get("sex")[0]).trim().equalsIgnoreCase("any"))){
			patientQuery
			.eq("sex",requestMap.get("sex")[0].trim());
		}
		//		Logger.info("size=="+patients.size());
		return ok(views.html.bloodBank.bloodDonorsInPrimaryCity.render(patientQuery.findList()));
	}
	/**
	 * @author lakshmi
	 * Action to render receivedBloodDonorFrom
	 * GET/secure-blood-bank/received-blood-info-form
	 */
	@ConfirmAppUser
	public static Result receivedBloodDonorFrom(){
		return ok(views.html.bloodBank.receivedBloodDonorFrom.render(null));
	}
	/**
	 * @author lakshmi
	 * Action to find blood donor data with emailId
	 * GET/secure-blood-bank/get-received-blood-info/:emailId
	 */
	@ConfirmAppUser
	public static Result getReceivedBloodDonorInfo(final String emailId,final Long appUserId){
		// Server side validation
		if((LoginController.getLoggedInUser().id.longValue()) != (appUserId.longValue()) || (!LoginController.getLoggedInUser().role.equals(Role.BLOOD_BANK_ADMIN))){
			Logger.warn("COULD NOT VALIDATE LOGGED IN USER TO PERFORM THIS TASK");
			Logger.warn("logged in AppUser: "+LoginController.getLoggedInUser().id);
			Logger.warn("logged in BllodBankUser: "+LoginController.getLoggedInUser().getBloodBankAdmin().id);
			return redirect(routes.LoginController.processLogout());
		}
		final AppUser appUser = AppUser.find.where().eq("email", emailId.trim()).findUnique();
		if((appUser.role.equals(Role.PATIENT))){
			final Patient patient = appUser.getPatient();
			return ok(views.html.bloodBank.receivedBloodDonorFrom.render(patient));
		}else{
			flash().put("alert", new Alert("alert-info", "Sorry. with "+emailId+"No Blood Donor Found.").toString());
		}
		return redirect(routes.BloodBankController.receivedBloodDonorFrom());
	}
	@ConfirmAppUser
	/**
	 * @author lakshmi
	 * Action to save BloodDonation data of AppUser
	 * POST/secure-blood-bank/save-received-blood-info/:patientId
	 */
	public static Result saveReceivedBloodDonorInfo(final Long patientId){
		final AppUser appUser = Patient.find.byId(patientId).appUser;
		final Map<String, String[]> requestMap = request().body().asFormUrlEncoded();
		final BloodDonation bloodDonation = new BloodDonation();
		bloodDonation.bloodBank = LoginController.getLoggedInUser().getBloodBankAdmin().bloodBank;
		if((requestMap.get("bloodGroup")[0]!= null) && !(requestMap.get("bloodGroup")[0].trim().isEmpty())){
			bloodDonation.bloodGroup = Enum.valueOf(BloodGroup.class,requestMap.get("bloodGroup")[0]);
		}
		else{
			flash().put("alert", new Alert("alert-danger", "Sorry. Blood Group Mandatory.").toString());
			return redirect(routes.BloodBankController.receivedBloodDonorFrom());
		}
		if((requestMap.get("quantity")[0]!= null) && !(requestMap.get("quantity")[0].trim().isEmpty())){
			bloodDonation.quantityDonated = Float.parseFloat(requestMap.get("quantity")[0]);
		}
		else{
			flash().put("alert", new Alert("alert-danger", "Sorry. Quantity Of Blood Donated Mandatory.").toString());
			return redirect(routes.BloodBankController.receivedBloodDonorFrom());
		}
		if((requestMap.get("date")[0]!= null) && !(requestMap.get("date")[0].trim().isEmpty())){
			final String date = requestMap.get("date")[0].replaceAll(" ","").trim();
			final SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
			try {
				bloodDonation.dateDonated =  sdf.parse(date);
			} catch (final ParseException e) {
				Logger.error("ERROR WHILE PARSING DATE");
				e.printStackTrace();
			}
		}
		else{
			flash().put("alert", new Alert("alert-danger", "Sorry. Date Of BloodDonation Mandatory.").toString());
			return redirect(routes.BloodBankController.receivedBloodDonorFrom());
		}
		if((requestMap.get("weight")[0]!= null) && !(requestMap.get("weight")[0].trim().isEmpty())){
			bloodDonation.weight = Float.parseFloat(requestMap.get("weight")[0]);
		}else{
			flash().put("alert", new Alert("alert-danger", "Sorry. Weight Mandatory.").toString());
			return redirect(routes.BloodBankController.receivedBloodDonorFrom());
		}
		if((requestMap.get("hemoglobin")[0]!= null) && !(requestMap.get("hemoglobin")[0].trim().isEmpty())){
			bloodDonation.hemoglobinLevel = Float.parseFloat(requestMap.get("hemoglobin")[0]);
		}
		appUser.bloodDonationList.add(bloodDonation);
		appUser.update();
		appUser.lastBloodDonatedDate = BloodDonation.find.where().eq("app_user_id", appUser.id).orderBy("dateDonated DESC").findList().get(0).dateDonated;
		appUser.update();
		flash().put("alert", new Alert("alert-success", "Successfully Stored Blood Donation Information of "+appUser.email).toString());
		return redirect(routes.BloodBankController.receivedBloodDonorFrom());
	}

	/**
	 * @author lakshmi
	 * Action for BloodBank backgroundImage and profile
	 * Images of of BloodBank of the loggedIn OOD_BANK_ADMIN
	 * POST	/secure-diagnostic/upload-diagnostic-images
	 */
	public static Result uploadBloodBankImageProcess() {
		try{
			final BloodBank bloodBank = BloodBank.find.byId(Long.parseLong(request().body().asMultipartFormData().asFormUrlEncoded().get("bloodBankId")[0]));
			// Server side validation
			if((bloodBank.id.longValue() != LoginController.getLoggedInUser().getBloodBankAdmin().bloodBank.id.longValue()) || (!LoginController.getLoggedInUser().role.equals(Role.BLOOD_BANK_ADMIN))){
				Logger.warn("COULD NOT VALIDATE LOGGED IN USER TO PERFORM THIS TASK");
				Logger.warn("update attempted for BloodBank id: "+bloodBank.id);
				Logger.warn("logged in AppUser: "+LoginController.getLoggedInUser().id);
				Logger.warn("logged in BloodBankUser: "+LoginController.getLoggedInUser().getBloodBankAdmin().id);
				return redirect(routes.LoginController.processLogout());
			}
			//final String pattern="([^\\s]+(\\.(?i)(JPEG|jpg|png|gif|bmp))$)";
			if (request().body().asMultipartFormData().getFile("backgroundImage") != null) {
				final FilePart image = request().body().asMultipartFormData().getFile("backgroundImage");
				if(image.getContentType().equalsIgnoreCase("image/bmp")||image.getContentType().equalsIgnoreCase("image/png")||image.getContentType().equalsIgnoreCase("image/jpeg")||image.getContentType().equalsIgnoreCase("image/gif")){
					bloodBank.backgroudImage = Files.toByteArray(image.getFile());
				}else{
					flash().put("alert", new Alert("alert-info", "Sorry. Images Should Be In The Following Formats .JPEG,.jpg,.png,.gif,.bmp").toString());
				}
				bloodBank.update();
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
					bloodBank.profileImageList.add(FileEntity.find.byId(imageId));
					bloodBank.update();
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
	public static Result removeBloodBankImage(final Long bloodBankId,final Long imageId){
		final BloodBank bloodBank = BloodBank.find.byId(bloodBankId);
		Logger.info("before list size="+bloodBank.profileImageList.size());
		final FileEntity image = FileEntity.find.byId(imageId);
		bloodBank.profileImageList.remove(image);
		bloodBank.update();
		//image.delete();
		Logger.info("after list size="+bloodBank.profileImageList.size());
		//		return ok(views.html.pharmacist.pharmacy_profile.render(pharmacy.inventoryList, pharmacy));
		return redirect(routes.UserActions.dashboard());
	}
	/**
	 * @author : lakshmi
	 * POST	/secure-blood-bank/basic-update
	 * Action to update the basic details(like name & brief description etc) of BloodBank
	 */

	public static Result bloodBankBasicUpdate() {
		try{
			final Map<String, String[]> requestMap = request().body().asFormUrlEncoded();
			final BloodBank bloodBank = BloodBank.find.byId(Long.parseLong(requestMap.get("bloodBankId")[0]));
			// Server side validation
			if((bloodBank.id.longValue() != LoginController.getLoggedInUser().getBloodBankAdmin().bloodBank.id.longValue()) || (!LoginController.getLoggedInUser().role.equals(Role.BLOOD_BANK_ADMIN))){
				Logger.warn("COULD NOT VALIDATE LOGGED IN USER TO PERFORM THIS TASK");
				Logger.warn("update attempted for BloodBank id: "+bloodBank.id);
				Logger.warn("logged in AppUser: "+LoginController.getLoggedInUser().id);
				Logger.warn("logged in DiagnosticRep: "+LoginController.getLoggedInUser().getBloodBankAdmin().bloodBank.id);
				return redirect(routes.LoginController.processLogout());
			}
			if(requestMap.get("name") != null && (requestMap.get("name")[0].trim().compareToIgnoreCase("")!=0)){
				bloodBank.name = requestMap.get("name")[0].trim();
			}
			if(requestMap.get("description") != null && (requestMap.get("description")[0].trim().compareToIgnoreCase("")!=0)){
				bloodBank.description = requestMap.get("description")[0].trim();
			}

			bloodBank.update();
		}
		catch (final Exception e){
			flash().put("alert", new Alert("alert-danger", "Sorry. Something went wrong. Please try again.").toString());
		}
		return redirect(routes.UserActions.dashboard());
	}
	/**
	 * @author : lakshmi
	 * POST	/secure-blood-bank/address-update
	 * Action to update the address details of BloodBank
	 */

	public static Result bloodBankAddressUpdate() {

		try{
			final Map<String, String[]> requestMap = request().body().asFormUrlEncoded();
			final BloodBank bloodBank = BloodBank.find.byId(Long.parseLong(requestMap.get("bloodBankId")[0]));
			Logger.info("map size"+requestMap.toString());
			if(bloodBank.address == null){
				final Address address = new Address();
				address.save();
				bloodBank.address = address;
			}
			if(requestMap.get("contactPerson") != null && (requestMap.get("contactPerson")[0].trim().compareToIgnoreCase("")!=0)){
				bloodBank.contactPersonName = requestMap.get("contactPerson")[0];
			}
			if(requestMap.get("addressLine1") != null && (requestMap.get("addressLine1")[0].trim().compareToIgnoreCase("")!=0)){
				bloodBank.address.addressLine1 = requestMap.get("addressLine1")[0];
			}
			if(requestMap.get("city") != null && (requestMap.get("city")[0].trim().compareToIgnoreCase("")!=0)){
				bloodBank.address.city = requestMap.get("city")[0];
			}
			if(requestMap.get("area") != null && (requestMap.get("area")[0].trim().compareToIgnoreCase("")!=0)){
				bloodBank.address.area = requestMap.get("area")[0];
			}
			if(requestMap.get("pincode") != null && (requestMap.get("pincode")[0].trim().compareToIgnoreCase("")!=0)){
				bloodBank.address.pinCode = requestMap.get("pincode")[0];
			}
			if(requestMap.get("state") != null && (requestMap.get("state")[0].trim().compareToIgnoreCase("")!=0)){
				bloodBank.address.state = Enum.valueOf(State.class,requestMap.get("state")[0]);
			}
			if(requestMap.get("latitude") != null && (requestMap.get("latitude")[0].trim().compareToIgnoreCase("")!=0)){
				bloodBank.address.latitude = requestMap.get("latitude")[0];
			}
			if(requestMap.get("longitude") != null && (requestMap.get("longitude")[0].trim().compareToIgnoreCase("")!=0)){
				bloodBank.address.longitude = requestMap.get("longitude")[0];
			}
			if(requestMap.get("contactNo") != null && (requestMap.get("contactNo")[0].trim().compareToIgnoreCase("")!=0)){
				bloodBank.contactNo = requestMap.get("contactNo")[0];
			}
			bloodBank.address.update();
			bloodBank.update();

		}
		catch (final Exception e){
			e.printStackTrace();
			flash().put("alert", new Alert("alert-danger", "Sorry. Something went wrong. Please try again.").toString());
		}
		return redirect(routes.UserActions.dashboard());
	}


}
