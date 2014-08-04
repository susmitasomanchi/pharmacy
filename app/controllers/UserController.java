
/*****

THIS IS AN AUTO GENERATED CODE
PLEASE DO NOT MODIFY IT BY HAND
 *****/
/*****

THIS IS AN AUTO GENERATED CODE
PLEASE DO NOT MODIFY IT BY HAND
 *****/
package controllers;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.Random;

import models.Alert;
import models.AppUser;
import models.Role;
import models.Sex;
import models.diagnostic.DiagnosticCentre;
import models.diagnostic.DiagnosticRepresentative;
import models.doctor.Doctor;
import models.mr.MedicalRepresentative;
import models.patient.Patient;
import models.pharmacist.Pharmacist;
import models.pharmacist.Pharmacy;

import org.apache.commons.codec.binary.Base64;

import play.Logger;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import utils.Constants;
import utils.EmailService;
import utils.Util;
import actions.BasicAuth;
import beans.JoinUsBean;


public class UserController extends Controller {

	public static Form<JoinUsBean> joinUsForm = Form.form(JoinUsBean.class);
	public static Form<DiagnosticRepresentative> drForm = Form.form(DiagnosticRepresentative.class);
	public static Form<MedicalRepresentative> mrForm = Form.form(MedicalRepresentative.class);



	/**
	 * Action to render the joinUs page for Doctor
	 * GET    /doctor/join
	 */
	public static Result joinUsDoctor(){
		return ok(views.html.doctor.joinus.render());
	}


	/**
	 * Action to render the joinUs page for Pharmacy
	 * GET    /pharmacy/join
	 */
	public static Result joinUsPharmacy(){
		return ok(views.html.pharmacist.joinus.render());
	}

	/**
	 * Action to render the joinUs page for Diagnostic Centre
	 * GET    /diagnostic/join
	 */
	public static Result joinUsDiagnostic(){
		return ok(views.html.diagnostic.joinus.render());
	}

	/**
	 * Action to render the joinUs page for Patients("User")
	 * GET    /user/join
	 */
	public static Result joinUsPatient(){
		return ok(views.html.patient.joinus.render());
	}



	/**
	 * Action to onboard a new AppUser along with its Role's entity
	 * POST   /join
	 */
	public static Result processJoinUs(){
		final Map<String, String[]> requestMap = request().body().asFormUrlEncoded();
		final AppUser appUser = new AppUser();
		appUser.name = requestMap.get("fullname")[0].trim();
		appUser.email = requestMap.get("email")[0].toLowerCase().trim();
		appUser.role = Role.valueOf(requestMap.get("role")[0]);
		if(AppUser.find.where().eq("email", appUser.email).findRowCount()>0){
			flash().put("alert", new Alert("alert-danger", "Sorry! User with email id "+appUser.email.trim()+" already exists!").toString());
			if(appUser.role == Role.PATIENT){
				return redirect(routes.UserController.joinUsPatient());
			}
			if(appUser.role == Role.DOCTOR){
				return redirect(routes.UserController.joinUsDoctor());
			}
			if(appUser.role == Role.ADMIN_PHARMACIST){
				return redirect(routes.UserController.joinUsPharmacy());
			}
			if(appUser.role == Role.ADMIN_DIAGREP){
				return redirect(routes.UserController.joinUsDiagnostic());
			}
		}

		if(requestMap.get("sex") != null && !(requestMap.get("sex")[0].trim().isEmpty())){
			appUser.sex = Sex.valueOf(requestMap.get("sex")[0].trim());
		}

		if(requestMap.get("dob") != null && !(requestMap.get("dob")[0].trim().isEmpty())){
			final String dobStr = requestMap.get("dob")[0].replaceAll(" ","").trim();
			final SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
			try {
				appUser.dob =  sdf.parse(dobStr);
			} catch (final ParseException e) {
				Logger.error("ERROR WHILE PARSING DOB");
				e.printStackTrace();
			}
		}

		final String password = requestMap.get("password")[0].trim();
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

		appUser.save();

		if(appUser.role.equals(Role.DOCTOR)){
			final Doctor doctor = new Doctor();
			doctor.specialization = "Specialization";
			doctor.degree = "Degree";
			final Calendar cal = Calendar.getInstance();
			cal.setTime(new Date());
			doctor.experience = cal.get(Calendar.YEAR);
			doctor.registrationNumber = "00000";
			doctor.slugUrl = Util.simpleSlugify(appUser.name)+appUser.id;
			appUser.mobileNumber = 9999999999L;
			appUser.update();
			doctor.appUser = appUser;
			doctor.save();
		}

		if(appUser.role.equals(Role.ADMIN_PHARMACIST)){
			final Pharmacist pharmacist = new Pharmacist();
			pharmacist.appUser = appUser;
			pharmacist.save();

			final Pharmacy pharmacy = new Pharmacy();
			pharmacy.name = request().body().asFormUrlEncoded().get("pharmacyName")[0];
			pharmacy.adminPharmacist = pharmacist;
			pharmacy.slugUrl = Util.simpleSlugify(pharmacy.name)+pharmacist.id;
			pharmacy.save();
			pharmacist.pharmacy = pharmacy;
			pharmacist.update();
		}

		if(appUser.role.equals(Role.ADMIN_DIAGREP)){
			final DiagnosticRepresentative diagnosticRepresentative = new DiagnosticRepresentative();
			diagnosticRepresentative.appUser = appUser;
			diagnosticRepresentative.save();

			final DiagnosticCentre diagnosticCentre = new DiagnosticCentre();
			diagnosticCentre.name = request().body().asFormUrlEncoded().get("diagnosticCentreName")[0];
			diagnosticCentre.diagnosticRepAdmin = diagnosticRepresentative;
			diagnosticCentre.slugUrl = Util.simpleSlugify(diagnosticCentre.name)+diagnosticRepresentative.id;
			diagnosticCentre.save();
			diagnosticRepresentative.diagnosticCentre = diagnosticCentre;
			diagnosticRepresentative.update();
		}

		if(appUser.role.equals(Role.PATIENT)){
			final Patient patient = new Patient();
			patient.appUser = appUser;
			patient.save();
		}

		session().clear();
		session(Constants.LOGGED_IN_USER_ID, appUser.id + "");
		session(Constants.LOGGED_IN_USER_ROLE, appUser.role+ "");
		if(EmailService.sendConfirmationEmail(appUser)){
			flash().put("alert", new Alert("alert-success","A conformation messege has been send to you").toString());
		}
		else{
			flash().put("alert", new Alert("alert-danger","Sorry the message cant be sent").toString());
		}
		return redirect(routes.UserActions.dashboard());
	}




	/**
	 * Action to render a page asking loggedInAppUser to
	 * confirm his email and/or mobile number
	 * Uses a generic scala template: views.html.comfirmAppUser.scala.html
	 * LoggedInUser specific content needs to be specified in the template
	 * GET /not-confirmed-yet
	 */
	@BasicAuth
	public static Result confirmAppUserPage(){
		final AppUser appUser = LoginController.getLoggedInUser();
		// Redirect to dashboard id email AND mobile number are already confirmed
		if(appUser.emailConfirmed && appUser.mobileNumberConfirmed){
			return redirect(routes.UserActions.dashboard());
		}
		return ok(views.html.confirmAppUser.render(appUser));
	}














































	/**
	 * **************************** DEPRICATED ON 14 JUL 2014 ****************************
	 * @deprecated Use UserController.processJoinUs() instead which is generic for all AppUser roles
	 * Action to onboard a new Doctor by creating a models.Doctor and a models.AppUser
	 * POST   /join
	 */
	@Deprecated
	public static Result processDoctorJoinUs(){
		final AppUser appUser = new AppUser();
		appUser.name = request().body().asFormUrlEncoded().get("fullname")[0];
		appUser.email = request().body().asFormUrlEncoded().get("email")[0];
		appUser.password = request().body().asFormUrlEncoded().get("password")[0];
		appUser.role = Role.DOCTOR;
		appUser.save();

		final Doctor doctor = new Doctor();
		doctor.specialization = "Specialization";
		doctor.degree = "Degree";
		doctor.appUser = appUser;
		doctor.save();

		session().clear();
		session(Constants.LOGGED_IN_USER_ID, appUser.id + "");
		session(Constants.LOGGED_IN_USER_ROLE, appUser.role+ "");

		return redirect(routes.UserActions.dashboard());
	}




}