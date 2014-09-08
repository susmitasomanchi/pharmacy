package controllers;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

import models.Alert;
import models.AppUser;
import models.BloodGroup;
import models.PrimaryCity;
import models.Role;
import models.Sex;
import models.WeightTracker;
import models.diagnostic.DiagnosticCentre;
import models.diagnostic.DiagnosticRepresentative;
import models.doctor.Doctor;
import models.mr.MedicalRepresentative;
import models.patient.Patient;
import models.patient.SugarTracker;
import models.pharmacist.Pharmacist;
import models.pharmacist.Pharmacy;

import org.apache.commons.codec.binary.Base64;

import play.Logger;
import play.data.Form;
import play.libs.F.Function0;
import play.libs.F.Promise;
import play.mvc.Controller;
import play.mvc.Result;
import utils.Constants;
import utils.EmailService;
import utils.SMSService;
import utils.Util;
import actions.BasicAuth;
import actions.ConfirmAppUser;
import beans.JoinUsBean;


public class UserController extends Controller {

	public static Form<JoinUsBean> joinUsForm = Form.form(JoinUsBean.class);
	public static Form<DiagnosticRepresentative> drForm = Form.form(DiagnosticRepresentative.class);
	public static Form<MedicalRepresentative> mrForm = Form.form(MedicalRepresentative.class);



	/**
	 * Action to render the joinUs page for Doctor
	 * GET    /secure-doctor/join
	 */
	public static Result joinUsDoctor(){
		return ok(views.html.doctor.joinus.render());
	}


	/**
	 * Action to render the joinUs page for Pharmacy
	 * GET    /secure-pharmacy/join
	 */
	public static Result joinUsPharmacy(){
		return ok(views.html.pharmacist.joinus.render());
	}

	/**
	 * Action to render the joinUs page for Diagnostic Centre
	 * GET    /secure-diagnostic/join
	 */
	public static Result joinUsDiagnostic(){
		return ok(views.html.diagnostic.joinus.render());
	}

	/**
	 * Action to render the joinUs page for Patients("User")
	 * GET    /secure-user/join
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

		appUser.mobileNumber = 9999999999L;
		appUser.save();

		final PrimaryCity city = PrimaryCity.find.byId(Long.parseLong(requestMap.get("city")[0].trim()));

		if(appUser.role.equals(Role.DOCTOR)){
			final Doctor doctor = new Doctor();
			doctor.degree = "Degree";
			final Calendar cal = Calendar.getInstance();
			cal.setTime(new Date());
			doctor.experience = cal.get(Calendar.YEAR);
			doctor.registrationNumber = "00000";
			doctor.slugUrl = Util.simpleSlugify(appUser.name)+appUser.id;
			doctor.appUser = appUser;
			doctor.primaryCity = city;
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
			pharmacy.primaryCity = city;
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
			diagnosticCentre.primaryCity = city;
			diagnosticCentre.save();
			diagnosticRepresentative.diagnosticCentre = diagnosticCentre;
			diagnosticRepresentative.update();
		}

		if(appUser.role.equals(Role.PATIENT)){
			final Patient patient = new Patient();
			patient.appUser = appUser;
			patient.primaryCity = city;
			patient.save();
		}

		//session().clear();
		session(Constants.LOGGED_IN_USER_ID, appUser.id + "");
		session(Constants.LOGGED_IN_USER_ROLE, appUser.role+ "");
		// Async Execution
		Promise.promise(new Function0<Integer>() {
			//@Override
			@Override
			public Integer apply() {
				if(EmailService.sendConfirmationEmail(appUser)){
					flash().put("alert", new Alert("alert-success","A confirmation mail has been sent to your email id.").toString());
				}
				else{
					flash().put("alert", new Alert("alert-danger","Sorry! Confirmation mail could not be sent. Please try again.").toString());
					System.out.println("Send");
				}
				return 0;
			}
		});
		// End of async

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
	 * @author Lakshmi
	 * Action to render the page to edit emailId and mobileNumber of loggedInUser
	 * GET	   /edit-contact-details
	 */
	public static Result editAppUserProfile(){
		return ok(views.html.editAppUserDetails.render(LoginController.getLoggedInUser()));
	}


	/**
	 * @author Lakshmi
	 * Action to edit emailId and mobileNumber of loggedInUser
	 * POST   /edit-contact-details
	 */
	public static Result updateAppUserProfile(){
		final Map<String, String[]> requestMap = request().body().asFormUrlEncoded();
		final Long appUserId = Long.parseLong(requestMap.get("appUserId")[0]);
		final AppUser loggedInUser = LoginController.getLoggedInUser();
		// server-side check
		if(appUserId.longValue() != loggedInUser.id.longValue()){
			//session().clear();
			session().remove(Constants.LOGGED_IN_USER_ID);
			session().remove(Constants.LOGGED_IN_USER_ROLE);

			return redirect(routes.LoginController.processLogout());
		}

		if(requestMap.get("name") != null && !requestMap.get("name")[0].trim().isEmpty()){
			loggedInUser.name = requestMap.get("name")[0];
		}

		if(requestMap.get("email")[0]!=null && requestMap.get("email")[0].trim()!=""){
			final String oldEmail = loggedInUser.email;
			if (oldEmail.trim().compareToIgnoreCase(requestMap.get("email")[0].trim()) != 0) {
				if(AppUser.find.where().ieq("email", requestMap.get("email")[0].trim()).findRowCount()>0){
					flash().put("alert", new Alert("alert-danger", "Sorry! Another User with email id "+requestMap.get("email")[0].trim()+" already exists!").toString());
					return redirect(routes.UserController.editAppUserProfile());
				}
				loggedInUser.email = requestMap.get("email")[0].trim().toLowerCase();;
				loggedInUser.emailConfirmed = false;
				//TODO: make it async
				EmailService.sendConfirmationEmail(loggedInUser);
			}
		}
		if(requestMap.get("contactNo")[0]!=null && requestMap.get("contactNo")[0].trim()!=""){
			final Long oldNumber = loggedInUser.mobileNumber;
			final Long newNumber = Long.parseLong(requestMap.get("contactNo")[0].trim());
			if (oldNumber == null || (oldNumber.longValue() != newNumber.longValue())) {
				loggedInUser.mobileNumber = newNumber;
				loggedInUser.mobileNumberConfirmed = false;
				//TODO: make it async
				SMSService.sendConfirmationSMS(loggedInUser);
			}
		}
		if(requestMap.get("bloodgroup")[0]!=null && requestMap.get("bloodgroup")[0].trim()!=""){
			final BloodGroup oldGroup = loggedInUser.bloodGroup;
			final String newGroup = requestMap.get("bloodgroup")[0].trim();
			if (oldGroup == null || (oldGroup.toString() != newGroup)) {
				loggedInUser.bloodGroup = BloodGroup.valueOf(newGroup);
				//TODO: make it async
				SMSService.sendConfirmationSMS(loggedInUser);
			}
		}
		if(requestMap.get("checkbox")[0]!=null && requestMap.get("checkbox")[0].trim()!=""){
			loggedInUser.isBloodDonor = Boolean.valueOf(requestMap.get("checkbox")[0]);
			//TODO: make it async
			SMSService.sendConfirmationSMS(loggedInUser);
		}
		if(requestMap.get("allergy")[0]!=null && requestMap.get("allergy")[0].trim()!=""){
			loggedInUser.allergy = requestMap.get("allergy")[0].trim();
		}
		Logger.info("sugar avilable  : "+requestMap.get("sugarAvilable")[0]);
		if(requestMap.get("sugarAvilable")[0]!=null && requestMap.get("sugarAvilable")[0].trim()!=""){
			SugarTracker sugarTracker = new SugarTracker();
			sugarTracker.sugarLevel= Float.parseFloat(requestMap.get("sugarAvilable")[0]);
			sugarTracker.save();
			loggedInUser.sugarTracker = sugarTracker;
		}
		if(requestMap.get("dob")[0]!=null ){
			try {
				loggedInUser.dob =new SimpleDateFormat("dd-mm-yyyy").parse(requestMap.get("dob")[0].trim());
				Logger.debug(new SimpleDateFormat("dd-mm-yyyy").parse(requestMap.get("dob")[0].trim()).toString());
				Logger.debug(""+loggedInUser.dob);
			} catch (final Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		loggedInUser.update();
		return redirect(routes.UserActions.dashboard());
	}


	/**
	 * @author Mitesh Action to send mobileNumberConfirmationKey to currently
	 *         logged in user's mobile GET /user/send-verificaion-code
	 */
	public static Result sendMobVerificationCode() {
		final AppUser appUser = LoginController.getLoggedInUser();
		SMSService.sendConfirmationSMS(appUser);
		flash().put(
				"alert",
				new Alert("alert-info","A confirmation code has been SMSed to your Mobile Number ("+ appUser.mobileNumber + ")").toString());
		return redirect(routes.UserController.confirmAppUserPage());

	}

	/**
	 * @author Mitesh Action to Display form to verify the mobile number of
	 *         currently logged in user GET /secure-user/verify-mobile-number
	 */
	public static Result displayMobVerificationForm() {
		return ok(views.html.common.verifyMobileNumber.render());
	}



	/**
	 * @author Mitesh Action to verify the mobileNumberConfirmationKey send to
	 *         currently logged in user'mobile POST /secure-user/verify-mobile-number
	 */
	public static Result verifyMobileNumberConfirmationKey() {

		final String key = request().body().asFormUrlEncoded()
				.get("mobileNumber")[0].trim();

		Logger.debug(key);

		final AppUser appUser = LoginController.getLoggedInUser();
		Logger.info(appUser.mobileNumberConfirmationKey);
		if (appUser.mobileNumberConfirmationKey == null) {
			flash().put(
					"alert",
					new Alert("alert-danger", "You hav'nt genrated a code yet")
					.toString());
			return redirect(routes.UserController.confirmAppUserPage());

		}
		if (key.compareToIgnoreCase(appUser.mobileNumberConfirmationKey) == 0) {
			flash().put(
					"alert",
					new Alert("alert-success", "Mobile number is verified")
					.toString());
			appUser.mobileNumberConfirmed = true;
			appUser.update();

			/**
			 * message to mobile after mobile verification
			 */

			SMSService.sendSMS(appUser.mobileNumber.toString(), "Thank you for mobile verification");
			return redirect(routes.UserActions.dashboard());
		} else {

			flash().put(
					"alert",
					new Alert("alert-danger",
							"Wrong code Please enter correct code").toString());
			return redirect(routes.UserController.confirmAppUserPage());
		}
	}



	/**
	 * @author Mitesh Action to send a verification email currently logged in
	 *         user'mobile GET /user/verify-email-number
	 */
	public static Result sendConformationEmail() {

		final boolean result;
		final AppUser loggedInUser = LoginController.getLoggedInUser();

		Promise.promise(new Function0<Integer>() {

			// @Override
			//@Override
			@Override
			public Integer apply() {
				final boolean result1 = EmailService
						.sendConfirmationEmail(loggedInUser);
				return 0;
			}
		});
		// End of async
		flash().put(
				"alert",
				new Alert("alert-success",
						"A confirmation email has been sent to you at "
								+ loggedInUser.email
								+ ". Kindly verify the same.").toString());
		return redirect(routes.UserController.confirmAppUserPage());

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
		doctor.degree = "Degree";
		doctor.appUser = appUser;
		doctor.save();

		//session().clear();
		session().remove(Constants.LOGGED_IN_USER_ID);
		session().remove(Constants.LOGGED_IN_USER_ROLE);

		session(Constants.LOGGED_IN_USER_ID, appUser.id + "");
		session(Constants.LOGGED_IN_USER_ROLE, appUser.role+ "");

		return redirect(routes.UserActions.dashboard());
	}



}
