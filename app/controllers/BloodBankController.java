package controllers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import com.avaje.ebean.ExpressionList;

import models.Alert;
import models.AppUser;
import models.BloodGroup;
import models.PrimaryCity;
import models.Role;
import models.bloodBank.BloodBankUser;
import models.bloodBank.BloodDonation;
import models.patient.Patient;
import play.Logger;
import play.mvc.Controller;
import play.mvc.Result;
import actions.BasicAuth;
import actions.ConfirmAppUser;

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


}
