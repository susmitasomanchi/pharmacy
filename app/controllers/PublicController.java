package controllers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import models.Alert;
import models.AppUser;
import models.Feedback;
import models.FileEntity;
import models.Locality;
import models.PrimaryCity;
import models.Role;
import models.bloodBank.BloodBank;
import models.diagnostic.DiagnosticCentre;
import models.doctor.Appointment;
import models.doctor.Clinic;
import models.doctor.Day;
import models.doctor.DaySchedule;
import models.doctor.Doctor;
import models.doctor.DoctorClinicInfo;
import models.doctor.MasterSpecialization;
import models.patient.Patient;
import models.patient.PatientDoctorInfo;
import models.pharmacist.Pharmacy;

import org.json.JSONArray;

import play.Logger;
import play.data.Form;
import play.libs.F.Function0;
import play.libs.F.Promise;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import utils.Constants;
import utils.EmailService;
import actions.ConfirmAppUser;
import beans.LoginBean;

import com.avaje.ebean.ExpressionList;

public class PublicController extends Controller{
	public static final Form<LoginBean> loginForm = Form.form(LoginBean.class);
	/**
	 * Action to render search doctor page
	 * GET /doctor/search
	 */
	public static Result searchDoctorsPage(){
		return ok(views.html.doctor.searchedDoctors.render(false,"any",null,null, new HashSet<Doctor>()));
	}

	/**
	 * @author Mitesh
	 * Action to search Doctor and display it
	 * GET /doctor/search/:spez/:loc/:key
	 */
	public static Result processSearchDoctors(final String spez,final String locality, final String key) {
		MasterSpecialization specialization = null;
		Locality loc = null;
		if(session(Constants.CITY_ID) != null){
			final ExpressionList<DoctorClinicInfo> doctorClinicList = DoctorClinicInfo.find.where().eq("clinic.primaryCity", PrimaryCity.find.byId(Long.parseLong(session(Constants.CITY_ID))));
			final Set<Doctor> doctorClinicInfos = new HashSet<Doctor>();
			if(locality != null && !(locality.equalsIgnoreCase("0"))){
				loc = Locality.find.byId(Long.parseLong(locality));
				doctorClinicList.eq("clinic.address.locality", loc);
			}
			if((spez != null) && !(spez.equalsIgnoreCase("any")) && !(spez.trim().isEmpty())){
				specialization = MasterSpecialization.find.where().ieq("name", spez).findUnique();
				doctorClinicList.in("doctor.specializationList", specialization);
			}
			if((key!= null)&& !(key.equalsIgnoreCase("any")) && !(key.trim().isEmpty())){
				doctorClinicList.like("doctor.searchIndex","%"+key.trim().toLowerCase()+"%");
			}
			for (final DoctorClinicInfo doctorClinicInfo : doctorClinicList.findList()) {
				doctorClinicInfos.add(doctorClinicInfo.doctor);
			}
			return ok(views.html.doctor.searchedDoctors.render(true,key,loc,specialization, doctorClinicInfos));
		}else{
			flash().put("alert", new Alert("alert-info","Please Select City.").toString());
			return ok(views.html.doctor.searchedDoctors.render(false,key,loc,specialization, new HashSet<Doctor>()));
		}
	}
	/**
	 * @author Lakshmi
	 * Action to search Pharmacy by slug and display its profile page
	 * GET /pharmacy/:slugUrl
	 */
	public static Result getPharmacyWithSlug(final String slug) {
		final String cleanSlug = slug.trim().toLowerCase();
		final Pharmacy pharmacy = Pharmacy.find.where().eq("slugUrl",cleanSlug).findUnique();
		if(pharmacy != null){
			return ok(views.html.pharmacist.publicPharmacyProfile.render(pharmacy));
		}
		else{
			return ok("404");
		}
	}

	/**
	 * @author Mitesh
	 * Action to add doctor to Users's page
	 * GET /doctor/add-to-favorite/:docId
	 */
	public static Result addToFavDoctor(final Long docId) {
		if(!LoginController.isLoggedIn()){
			flash().put("alert", new Alert("alert-info","Please Login To Add Your Favorite Doctor").toString());
			return redirect(routes.Application.index());
		}
		else{
			final String loggedInRole=LoginController.getLoggedInUserRole();
			if(loggedInRole.equalsIgnoreCase(Role.PATIENT.toString())){
				final Patient patient=LoginController.getLoggedInUser().getPatient();
				final PatientDoctorInfo patientInfo=new PatientDoctorInfo();
				final Doctor doctor = Doctor.find.byId(docId);
				patientInfo.patient=patient;
				patientInfo.doctor=doctor;
				if(PatientDoctorInfo.find.where().eq("doctor", doctor).eq("patient",patient).findList().size()==0){
					patient.patientDoctorInfoList.add(patientInfo);
					patient.update();
					flash().put("alert", new Alert("alert-success","Added to Your Favorite Doctor").toString());
				}else{
					flash().put("alert", new Alert("alert-success","Already added to Your Favorite Doctor").toString());
				}
				return redirect(routes.UserActions.dashboard());
			}
			else if(loggedInRole.equalsIgnoreCase(Role.ADMIN_PHARMACIST.toString())){

			}
			else if(loggedInRole.equalsIgnoreCase(Role.ADMIN_DIAGREP.toString())){

			}
		}
		return ok();
	}

	/**
	 * Action to get images associated with a doctor
	 * GET  /doctor/get-image/:id/:type
	 */
	public static Result getImage(final Long id, final String type){
		final Doctor doctor = Doctor.find.byId(id);
		if(type.compareToIgnoreCase("backgroundImage") == 0){
			return ok(doctor.backgroundImage).as("image/jpeg");
		}
		if(type.compareToIgnoreCase("profileImage") == 0){
			return ok(doctor.profileImage).as("image/jpeg");
		}
		return ok().as("image/jpeg");
	}



	/**
	 * @author lakshmi
	 * GET /pharmacy/get-image/:pharmacyId/:fileId
	 * Action to get byte data as image of Pharmacy's Background and Profile Images
	 */
	public static Result getPharmacyImages(final Long pharmacyId,final Long imageId) {
		byte[] byteContent = null;
		if(imageId == 0){
			byteContent=Pharmacy.find.byId(pharmacyId).backgroundImage;
		}
		else{
			for (final FileEntity file : Pharmacy.find.byId(pharmacyId).profileImageList) {
				if(file.id == imageId){
					byteContent = file.byteContent;
				}
			}
		}
		return ok(byteContent).as("image/jpeg");
	}


	/**
	 * @author Mitesh
	 * Action to search Doctor and display his profile page
	 * GET /doctor/:slugUrl
	 */
	public static Result getDoctorWithSlug(final String slug) {
		final String cleanSlug = slug.trim().toLowerCase();
		final Doctor doctor = Doctor.find.where().eq("slugUrl",cleanSlug).findUnique();
		return ok(views.html.doctor.publicDoctorProfile.render(doctor));
	}
	/**
	 * @author lakshmi
	 * Action to render the searchedPharmacies scala template
	 * GET	/pharmacy/search
	 */
	public static Result searchPharmaciesPage(){
		return ok(views.html.pharmacist.searched_pharmacies.render(false,"any",null, new ArrayList<Pharmacy>()));
	}
	/**
	 * @author lakshmi
	 * Action to perform search operation for finding pharmacies based on the searchKey
	 * GET	/pharmacy/search/:searchKey/:loc
	 */
	public static Result processSearchPharmacies(final String searchKey,final String locality) {
		Locality loc = null;
		if(session(Constants.CITY_ID) != null){
			final PrimaryCity city = PrimaryCity.find.byId(Long.parseLong(session(Constants.CITY_ID)));
			final ExpressionList<Pharmacy> query =Pharmacy.find.where().eq("primaryCity", city);
			if((locality!= null) && !(locality.equalsIgnoreCase("0")) && !(locality.trim().isEmpty())){
				loc = Locality.find.byId(Long.parseLong(locality));
				query.eq("address.locality", loc);
			}
			if((searchKey!= null)&& !(searchKey.equalsIgnoreCase("any")) && !(searchKey.trim().isEmpty())){
				query.like("searchIndex","%"+searchKey.trim().toLowerCase()+"%");
			}
			return ok(views.html.pharmacist.searched_pharmacies.render(true,searchKey.trim(),loc,query.findList()));
		}else{
			flash().put("alert", new Alert("alert-info","Please Select City.").toString());
			return ok(views.html.pharmacist.searched_pharmacies.render(false,searchKey.trim(),loc,new ArrayList<Pharmacy>()));
		}
		/*final String searchStr = searchKey.toLowerCase().trim();
		if(searchStr.length() < 4){
			flash().put("alert", new Alert("alert-danger", "The searck key should contain atleast four charecters").toString());
			return ok(views.html.pharmacist.searched_pharmacies.render(false,searchKey,new ArrayList<Pharmacy>()));
		}
		else{
			Logger.info
			("City id..."+session(Constants.CITY_ID).toString());
			final PrimaryCity city = PrimaryCity.find.byId(Long.parseLong(session(Constants.CITY_ID)));
			final List<Pharmacy>pharmacyList = Pharmacy.find.where()
					.eq("primaryCity", city)
					.like("searchIndex","%"+searchStr+"%")
					.findList();
			return ok(views.html.pharmacist.searched_pharmacies.render(true,searchKey,pharmacyList));
		}*/
	}

	/**
	 * @author lakshmi
	 * Action to add a Pharmacy to loggedInUser
	 * GET /pharmacy/add-to-favourites
	 */
	public static Result addPharmacyToLoggedInUser(final Long pharmacyId) {
		if(!LoginController.isLoggedIn()){
			flash().put("alert", new Alert("alert-info","Please Login To Add Pharmacy.").toString());
			return redirect(routes.Application.index());
		}
		else{
			final String loggedInRole=LoginController.getLoggedInUserRole();
			if(loggedInRole.compareTo(Role.PATIENT.toString()) == 0){
				final Patient patient = LoginController.getLoggedInUser().getPatient();
				final Pharmacy pharmacy = Pharmacy.find.byId(pharmacyId);
				if(!patient.pharmacyList.contains(pharmacy)){
					patient.pharmacyList.add(pharmacy);
					flash().put("alert", new Alert("alert-info",pharmacy.name+" added to your pharmacy list.").toString());
					patient.update();
				}
				else{
					flash().put("alert", new Alert("alert-info",pharmacy.name+" is already in your pharmacy list.").toString());
				}
				return redirect(routes.PatientController.patientFavoritePharmacies());
			}

			if(loggedInRole.compareTo(Role.DOCTOR.toString()) == 0){
				final Doctor doctor = LoginController.getLoggedInUser().getDoctor();
				final Pharmacy pharmacy = Pharmacy.find.byId(pharmacyId);
				if(!doctor.pharmacyList.contains(pharmacy)){
					doctor.pharmacyList.add(pharmacy);
					doctor.update();
					flash().put("alert", new Alert("alert-info",pharmacy.name+" added to your pharmacy list.").toString());
				}
				else{
					flash().put("alert", new Alert("alert-info",pharmacy.name+" is already in your pharmacy list.").toString());
				}
				return redirect(routes.DoctorController.myFavoritePharmacies());
			}

			if(loggedInRole.compareTo(Role.ADMIN_DIAGREP.toString()) == 0){

			}

			return redirect(routes.UserActions.dashboard());
		}
	}
	/**
	 *@author lakshmi
	 * Action to add pharmacy to the Logged in user list
	 * 
	 */
	public static Result myFavoritePharmacy(final Long pharmacyId,final String searchKey){
		if(LoginController.getLoggedInUserRole().equals("DOCTOR")){
			return redirect(routes.DoctorController.addFavoritePharmacy(pharmacyId,searchKey));
		}
		else if(LoginController.getLoggedInUserRole().equals("PATIENT")){
			return redirect(routes.DoctorController.addFavoritePharmacy(pharmacyId,searchKey));
		}else{

			return redirect(routes.UserController.processJoinUs());
		}
	}

	/**
	 * @author Mitesh
	 * Action to show a forms which have Doctor and it will show the available and booked appointment
	 *  GET	/doctor/schedule-appointment/:docclinicid
	 */
	@ConfirmAppUser
	public static Result scheduleAppointment(final Long docclinicid) {
		final DoctorClinicInfo clinicInfo=DoctorClinicInfo.find.byId(docclinicid);
		return ok(views.html.patient.patientNewAppointment.render(clinicInfo));
	}

	/**
	 * @author Mitesh
	 * Action to display a form which has lists of appointment as per date is provided
	 *  GET/patient/display-appointment/:docClinicId/:timeMillis
	 */
	@ConfirmAppUser
	public static Result displayAppointment(final Long docClinId,final Long timeMillis) {

		int slots=0;
		Date date=new Date();

		final Map<Date, List<Appointment>> appointmentMap = new LinkedHashMap<Date, List<Appointment>>();

		final DoctorClinicInfo doctorClinicInfo = DoctorClinicInfo.find.byId(docClinId);

		Logger.debug(doctorClinicInfo.scheduleDays.size()+"");
		final Calendar calendarFrom = Calendar.getInstance();
		calendarFrom.setTime(new Date(timeMillis));

		final Calendar calendarTo = Calendar.getInstance();
		calendarTo.setTime(new Date(timeMillis));

		final Calendar calendar1=Calendar.getInstance();
		final Calendar calendar2=Calendar.getInstance();

		final Calendar futureCalendarfrom = Calendar.getInstance();
		final Calendar futureCalendarto	= Calendar.getInstance();
		final SimpleDateFormat dateFormat=new SimpleDateFormat("kk:mm");
		int j=0;
		for(int i=0;i<49;i++){
			if(j==7){
				break;
			}

			Logger.warn("i:"+i);
			for (final DaySchedule schedule : doctorClinicInfo.scheduleDays) {
				Logger.error("schedule"+schedule.day);
				Logger.error("calender"+Day.getDay(calendarFrom.get(Calendar.DAY_OF_WEEK)-1));
				if(schedule.day == Day.getDay(calendarFrom.get(Calendar.DAY_OF_WEEK)-1)){
					Logger.debug("schedule"+schedule.day);
					try{
						calendar1.setTime(dateFormat.parse(schedule.fromTime));

						calendar2.setTime(dateFormat.parse(schedule.toTime));
					}
					catch(final ParseException exception){
						exception.printStackTrace();
					}


					calendarFrom.set(Calendar.HOUR_OF_DAY, calendar1.get(Calendar.HOUR_OF_DAY));
					calendarFrom.set(Calendar.MINUTE,calendar1.get(Calendar.MINUTE));
					calendarFrom.set(Calendar.SECOND, 0);
					calendarFrom.set(Calendar.MILLISECOND, 0);

					calendarTo.set(Calendar.HOUR_OF_DAY, calendar2.get(Calendar.HOUR_OF_DAY));
					calendarTo.set(Calendar.MINUTE,calendar2.get(Calendar.MINUTE));
					calendarTo.set(Calendar.SECOND, 0);
					calendarTo.set(Calendar.MILLISECOND, 0);


					final List<Appointment> listAppointments = Appointment.getAvailableAppointmentList(doctorClinicInfo.id,calendarFrom.getTime(),calendarTo.getTime());
					if(listAppointments.size() != 0 ){
						Logger.debug("fetched",calendarFrom);
						appointmentMap.put(calendarFrom.getTime(), listAppointments);
						slots=Math.max(slots,listAppointments.size());
						date=calendarFrom.getTime();
						futureCalendarfrom.setTime(calendarFrom.getTime());
						j++;

					}

					Logger.warn("if end");
				}

				Logger.warn("schedule  end");
			}
			Logger.warn("i  end");

			calendarFrom.add(Calendar.DATE, 1);
			calendarTo.add(Calendar.DATE, 1);


		}
		futureCalendarfrom.add(Calendar.DATE, 1);
		futureCalendarto.setTime(calendarFrom.getTime());
		futureCalendarto.add(Calendar.DATE, 7);
		final int available= Appointment.find.where().eq("doctorClinicInfo.id", doctorClinicInfo.id).between("appointmentTime", futureCalendarfrom.getTime(), futureCalendarto.getTime()).order().asc("appointmentTime").findRowCount();


		/*return ok(views.html.patient.scheduleAppointment.render(appointmentMap,
				 slots));*/
		Logger.warn(""+appointmentMap.size());
		Logger.error(date+"");
		return ok(views.html.patient.appointmentForm.render(appointmentMap,slots,date.getTime(),available));
	}

	/**
	 * @author Mitesh
	 * Action to display user login page after conformation
	 *  GET /user/confirmation/:userId/:randomString
	 */
	public static Result emailConfirmation(final Long userId,final String randomString) {
		final AppUser appUser =AppUser.find.byId(userId);
		if(appUser != null){
			if(appUser.emailConfirmationKey.compareTo(randomString) == 0){
				Logger.debug("key matched");
				appUser.emailConfirmed = true;
				appUser.update();
				// Async Execution
				Promise.promise(new Function0<Integer>() {
					//@Override
					@Override
					public Integer apply() {
						int result = 0;
						if(!EmailService.sendVerificationConformMessage(appUser)){
							result=1;
						}

						return result;
					}
				});
				// End of async
				flash().put("alert", new Alert("alert-success","Thank you for confirming your email.").toString());
				if(LoginController.isLoggedIn()){
					return redirect(routes.UserActions.dashboard());
				}
				else{
					return redirect(routes.Application.index());
				}
			}
			else{
				Logger.debug("email confirmation key does not match");
				flash().put("alert", new Alert("alert-danger","Sorry. Your email could not be confirmed. Please try again.").toString());
				if(LoginController.isLoggedIn()){
					return redirect(routes.UserController.confirmAppUserPage());
				}
				else{
					return redirect(routes.Application.index());
				}
			}
		}
		else{
			Logger.debug("user is null");
			return badRequest();
		}
	}
	/**
	 * @author lakshmi
	 *  Action to get byteData as image of DiagnosticCentre
	 * GET/diagnostic/get-image/:diagnosticId/:fileId
	 */
	public static Result getDiagnosticImages(final Long diagnosticId,final Long imageId){
		byte[] byteContent = null;
		if(imageId == 0){
			byteContent=DiagnosticCentre.find.byId(diagnosticId).backgroudImage;
		}
		else{
			for (final FileEntity file : DiagnosticCentre.find.byId(diagnosticId).profileImageList) {
				if(file.id == imageId){
					byteContent = file.byteContent;
				}
			}
		}

		return ok(byteContent).as("image/jpeg");

	}
	/**
	 * @author lakshmi
	 * Action to get byteData as image of BloodBank
	 * GET/bloodBank/get-image/:bloodBankId/:fileId
	 */
	public static Result getBloodBankImages(final Long bloodBankId,final Long imageId){
		byte[] byteContent = null;
		if(imageId == 0){
			byteContent=BloodBank.find.byId(bloodBankId).backgroudImage;
		}
		else{
			for (final FileEntity file : BloodBank.find.byId(bloodBankId).profileImageList) {
				if(file.id == imageId){
					byteContent = file.byteContent;
				}
			}
		}

		return ok(byteContent).as("image/jpeg");

	}
	/**
	 * @author lakshmi
	 *  Action to get byteData as image of Clinic
	 * GET/bloodBank/get-image/:bloodBankId/:fileId
	 */
	public static Result getClinicImages(final Long bloodBankId,final Long imageId){
		byte[] byteContent = null;
		if(imageId == 0){
			byteContent=Clinic.find.byId(bloodBankId).backgroudImage;
		}
		else{
			for (final FileEntity file : Clinic.find.byId(bloodBankId).profileImageList) {
				if(file.id == imageId){
					byteContent = file.byteContent;
				}
			}
		}

		return ok(byteContent).as("image/jpeg");

	}
	/**
	 * @author lakshmi
	 * Action to get byteData as image of Clinic
	 * GET/bloodBank/get-image/:bloodBankId/:fileId
	 */
	public static Result getAppUserImage(final Long appUserId){
		final AppUser appUser = AppUser.find.byId(appUserId);
		return ok(appUser.image).as("image/jpeg");
	}

	/**
	 * @author Lakshmi
	 * Action to search DiagnosticCentre by slug and display its profile page
	 * GET /diagnostic/:slugUrl
	 */
	public static Result getDiagnosticCentreWithSlug(final String slug) {
		final String cleanSlug = slug.trim().toLowerCase();
		final DiagnosticCentre diagnosticCentre = DiagnosticCentre.find.where().eq("slugUrl",cleanSlug).findUnique();
		if(diagnosticCentre != null){
			return ok(views.html.diagnostic.publicDiagnosticProfile.render(diagnosticCentre));
		}
		else{
			return ok("404");
		}
	}

	/**
	 * @author lakshmi
	 * Action to render the searched-_diagnostic_centres scala template
	 * GET	/diagnostic/search
	 */
	public static Result searchDiagnosticPage(){
		return ok(views.html.diagnostic.searched_diagnostic_Centres.render(false,"any",null, new ArrayList<DiagnosticCentre>()));
	}
	/**
	 * @author lakshmi
	 * Action to perform search operation for finding Diagnostic based on the searchKey
	 * GET	/diagnostic/search/:searchKey/:loc
	 */
	public static Result processSearchDiagnosticCentres(final String searchKey,final String localityId) {
		Locality loc = null;
		if(session(Constants.CITY_ID) != null){
			final PrimaryCity city = PrimaryCity.find.byId(Long.parseLong(session(Constants.CITY_ID)));

			final ExpressionList<DiagnosticCentre> query =DiagnosticCentre.find.where().eq("primaryCity", city);
			if((localityId!= null) && !(localityId.equalsIgnoreCase("0")) && !(localityId.trim().isEmpty())){
				loc = Locality.find.byId(Long.parseLong(localityId));
				query.eq("address.locality", loc);
			}
			if((searchKey!= null)&& !(searchKey.equalsIgnoreCase("any")) && !(searchKey.trim().isEmpty())){
				query.like("searchIndex","%"+searchKey.trim().toLowerCase()+"%");
			}
			return ok(views.html.diagnostic.searched_diagnostic_Centres.render(true,searchKey,loc,query.findList()));
		}else{
			flash().put("alert", new Alert("alert-info","Please Select City.").toString());
			return ok(views.html.diagnostic.searched_diagnostic_Centres.render(false,searchKey,loc,new ArrayList<DiagnosticCentre>()));
		}

	}



	/**
	 * @author lakshmi
	 * Action to add a Diagnostic Centre to loggedInUser
	 * GET /diagnostic/add-to-favourites
	 */
	public static Result addDiagnosticCentreToLoggedInUser(final Long diagnosticId) {
		if(!LoginController.isLoggedIn()){
			flash().put("alert", new Alert("alert-info","Please Login To Add DiagnosticCentre.").toString());
			return redirect(routes.Application.index());
		}
		else{
			final String loggedInRole=LoginController.getLoggedInUserRole();
			if(loggedInRole.compareTo(Role.PATIENT.toString()) == 0){
				final Patient patient = LoginController.getLoggedInUser().getPatient();
				final DiagnosticCentre diagnosticCentre = DiagnosticCentre.find.byId(diagnosticId);
				if(!patient.diagnosticCenterList.contains(diagnosticCentre)){
					patient.diagnosticCenterList.add(diagnosticCentre);
					patient.update();
					flash().put("alert", new Alert("alert-info",diagnosticCentre.name+" added to your diagnostic centres list.").toString());
				}
				else{
					flash().put("alert", new Alert("alert-info",diagnosticCentre.name+" is already in your diagnostic centres list.").toString());
				}
				return redirect(routes.PatientController.patientFavoriteDiagnosticCentres());
			}

			if(loggedInRole.compareTo(Role.DOCTOR.toString()) == 0){
				final Doctor doctor = LoginController.getLoggedInUser().getDoctor();
				final DiagnosticCentre diagnosticCentre = DiagnosticCentre.find.byId(diagnosticId);
				if(!doctor.diagnosticCentreList.contains(diagnosticCentre)){
					doctor.diagnosticCentreList.add(diagnosticCentre);
					doctor.update();
					flash().put("alert", new Alert("alert-info",diagnosticCentre.name+" added to your diagnostic centres list.").toString());
				}
				else{
					flash().put("alert", new Alert("alert-info",diagnosticCentre.name+" is already in your diagnostic centres list.").toString());
				}
				return redirect(routes.DoctorController.myFavoriteDiagnosticCentres());
			}

			if(loggedInRole.compareTo(Role.ADMIN_DIAGREP.toString()) == 0){

			}
			return redirect(routes.UserActions.dashboard());
		}
	}





	/**
	 * @author lakshmi Action to remove Pharmacy from favorite pharmacies List
	 *         of Doctor of loggedin DOCTOR
	 *         GET/doctor/remove-favorite-diagnosticCentre/:doctorId/:pharmacyId
	 */
	public static Result removeFavoriteDiagnosticCentre(final Long diagnosticId) {
		if(!LoginController.isLoggedIn()){
			flash().put("alert", new Alert("alert-info","Please Login To Delete DiagnosticCentre.").toString());
			return redirect(routes.Application.index());
		}else{
			final String loggedInRole=LoginController.getLoggedInUserRole();
			if(loggedInRole.compareTo(Role.PATIENT.toString()) == 0){
				final Patient patient = LoginController.getLoggedInUser().getPatient();
				patient.diagnosticCenterList.remove(DiagnosticCentre.find.byId(diagnosticId));
				patient.update();
				return redirect(routes.PatientController.patientFavoriteDiagnosticCentres());
			}
			if(loggedInRole.compareTo(Role.DOCTOR.toString()) == 0){
				final Doctor doctor = LoginController.getLoggedInUser().getDoctor();
				doctor.diagnosticCentreList.remove(DiagnosticCentre.find.byId(diagnosticId));
				doctor.update();
				return redirect(routes.DoctorController.myFavoriteDiagnosticCentres());
			}
			return redirect(routes.UserActions.dashboard());
		}
	}


	/**
	 * @author lakshmi Action to remove Pharmacy from favorite pharmacies List
	 *         of Doctor of loggedin DOCTOR
	 *         GET/doctor/remove-favorite-pharmacy/:patientId/:pharmacyId
	 */
	@ConfirmAppUser
	public static Result removeFavoritePharmacy(final Long pharmacyId) {
		if(!LoginController.isLoggedIn()){
			flash().put("alert", new Alert("alert-info","Please Login To Delete DiagnosticCentre.").toString());
			return redirect(routes.Application.index());
		}else{
			final String loggedInRole=LoginController.getLoggedInUserRole();
			if(loggedInRole.compareTo(Role.PATIENT.toString()) == 0){
				final Patient patient = LoginController.getLoggedInUser().getPatient();
				final Pharmacy pharmacy = Pharmacy.find.byId(pharmacyId);
				patient.pharmacyList.remove(pharmacy);
				patient.update();
				flash().put("alert", new Alert("alert-success",pharmacy.name+" Deleted From The Favorite Pharmacies").toString());
				return redirect(routes.PatientController.patientFavoritePharmacies());
			}
			if(loggedInRole.compareTo(Role.DOCTOR.toString()) == 0){
				final Doctor doctor = LoginController.getLoggedInUser().getDoctor();
				final Pharmacy pharmacy = Pharmacy.find.byId(pharmacyId);
				doctor.pharmacyList.remove(pharmacy);
				doctor.update();
				flash().put("alert", new Alert("alert-success",pharmacy.name+" Deleted From The Favorite Pharmacies").toString());
				return redirect(routes.DoctorController.myFavoritePharmacies());
			}
			return redirect(routes.UserActions.dashboard());
		}
	}
	/**
	 * @author lakshmi
	 * Action to render the feedback page
	 * GET/feedback
	 */
	public static Result feedBack(){

		//final AppUser appUser = LoginController.getLoggedInUser();
		return ok(views.html.feedback.render());
	}

	/**
	 * @author lakshmi
	 * Action to save the feedback from the user
	 * POST/feedback
	 */
	public static Result saveFeedBack(){
		final Map<String,String[]> requestData = request().body().asFormUrlEncoded();
		final Feedback feedback = new Feedback();

		if(LoginController.isLoggedIn()){
			feedback.appUser = LoginController.getLoggedInUser();
		}
		else{
			if(requestData.get("firstName") != null && requestData.get("firstName")[0].trim() != ""){
				feedback.name = requestData.get("firstName")[0];
			}
			if(requestData.get("role") != null && requestData.get("role")[0].trim() != ""){
				feedback.role = Enum.valueOf(Role.class,requestData.get("role")[0]);
			}
			if(requestData.get("email") != null && requestData.get("email")[0].trim() != ""){
				feedback.email = requestData.get("email")[0];
			}
		}

		if(requestData.get("remarks") != null && requestData.get("remarks")[0].trim() != ""){
			feedback.remarks = requestData.get("remarks")[0];
		}
		feedback.date = new Date();
		feedback.ipAddress = request().remoteAddress();
		feedback.save();
		final StringBuilder message = new StringBuilder();
		message.append("<html><body>");
		message.append("<p>Dear "+"Admin"+",<br><br>A feedback with following detail has been saved.");
		if(feedback.appUser !=null){
			message.append("<br><p>****** USER*******</p>");
			message.append("<br><p>Name:"+feedback.appUser.name+"</p>");
			message.append("<br><p>Email:"+feedback.appUser.email+"</p>");
			message.append("<br><p>Role:"+feedback.appUser.role+"</p>");

		}else{
			message.append("<br><p>******NOT USER*******</p>");
			message.append("<br><p>Name:"+feedback.name+"</p>");
			message.append("<br><p>Email:"+feedback.email+"</p>");
			message.append("<br><p>Role:"+feedback.role+"</p>");
		}
		message.append("<br><p>IP address:"+feedback.ipAddress+"</p>");
		message.append("<br><p>Remark:"+feedback.remarks+"</p>");
		message.append("<br><br>Best regards,<br>MedNetwork.in</p>");
		message.append("</body></html>");
		// Async Execution
		Promise.promise(new Function0<Integer>() {
			//@Override
			@Override
			public Integer apply() {
				int result = 0;
				if(!EmailService.sendSimpleHtmlEMail("admin@mednetwork.in", "A feedback saved", message.toString())){
					result=1;
				}

				return result;
			}
		});
		// End of async
		flash().put("alert", new Alert("alert-success", "Thank you for your feedback.").toString());
		if(LoginController.isLoggedIn()){
			return redirect(routes.UserActions.dashboard());
		}
		return redirect(routes.Application.index());
	}


	/**
	 * @author lakshmi
	 * Action to save the feedback from the user
	 * POST/feedback
	 */
	public static Result saveLocalityFeedBack(final String locality,final String city){
		final Feedback feedback = new Feedback();
		feedback.appUser = LoginController.getLoggedInUser();
		feedback.remarks = "Suggested "+locality +"(Locality) at "+ city +"(city)";
		feedback.date = new Date();
		feedback.ipAddress = request().remoteAddress();
		feedback.save();
		final StringBuilder message = new StringBuilder();
		message.append("<html><body>");
		message.append("<p>Dear "+"Admin"+",<br><br>A feedback with following detail has been saved.");
		if(feedback.appUser !=null){
			message.append("<br><p>****** USER*******</p>");
			message.append("<br><p>Name:"+feedback.appUser.name+"</p>");
			message.append("<br><p>Email:"+feedback.appUser.email+"</p>");
			message.append("<br><p>Role:"+feedback.appUser.role+"</p>");

		}
		message.append("<br><p>IP address:"+feedback.ipAddress+"</p>");
		message.append("<br><p>Remark:"+feedback.remarks+"</p>");
		message.append("<br><br>Best regards,<br>MedNetwork.in</p>");
		message.append("</body></html>");
		// Async Execution
		Promise.promise(new Function0<Integer>() {
			//@Override
			@Override
			public Integer apply() {
				int result = 0;
				if(!EmailService.sendSimpleHtmlEMail("admin@mednetwork.in", "A feedback saved", message.toString())){
					result=1;
				}

				return result;
			}
		});
		// End of async

		//flash().put("alert", new Alert("alert-success", "Thank you for your suggetion.").toString());
		//if(LoginController.isLoggedIn()){
		//	return redirect(routes.UserActions.dashboard());
		//}
		//return redirect(routes.Application.index());

		return ok("0");
	}

	/**
	 * @author lakshmi
	 * Action to save the feedback from the user
	 * POST/feedback
	 */
	public static Result saveDocFeedBack(final String locality,final String city){
		final Feedback feedback = new Feedback();
		feedback.appUser = LoginController.getLoggedInUser();
		feedback.remarks = "Suggested "+locality +"(Locality) at "+ city +"(city)";
		feedback.date = new Date();
		feedback.ipAddress = request().remoteAddress();
		feedback.save();
		return ok("1");

	}



	/**
	 * @author Prathyusha
	 * Action to render the help document page
	 * GET/feedback
	 */
	public static Result helpDocument(){

		//final AppUser appUser = LoginController.getLoggedInUser();
		return ok(views.html.helpdocument.render());
	}


	/**
	 * Action to add Primary City to Session
	 * GET	/add-primary-city-to-session/:cityId
	 */
	public static Result addPrimaryCityToSession(final Long cityId){
		session(Constants.CITY_ID, cityId+"");
		return ok(PrimaryCity.find.byId(cityId).name);
	}

	/**
	 * @author lakshmi
	 * Action to search Clinic and display his profile page
	 * GET /clinic/:slugUrl
	 */
	public static Result getClinicWithSlug(final String slug) {
		final String cleanSlug = slug.trim().toLowerCase();
		final Clinic clinic = Clinic.find.where().eq("slugUrl",cleanSlug).findUnique();
		return ok(views.html.clinic.publicClinicProfile.render(clinic));
	}
	/**
	 * @author lakshmi
	 * Action to search BloodBank and display his profile page
	 * GET /blood-bank/:slugUrl
	 */
	public static Result getBloodBankWithSlug(final String slug) {
		final String cleanSlug = slug.trim().toLowerCase();
		final BloodBank bloodBank = BloodBank.find.where().eq("slugUrl",cleanSlug).findUnique();
		return ok(views.html.bloodBank.publicBloodBankProfile.render(bloodBank));
	}

	/**
	 * @author lakshmi
	 * Action to  to render search Clinic page
	 *  GET /clinic/search
	 */
	public static Result searchClinics() {
		return ok(views.html.clinic.searchClinics.render("",null,false));
	}
	/**
	 * @author lakshmi
	 * Action to perform search operation for finding Diagnostic based on the searchKey
	 * GET	/clinic/search/:searchKey
	 */
	public static Result searchFavoriteClinics(final String key,final String locality) {
		Locality loc = null;
		if(session(Constants.CITY_ID) != null){
			final ExpressionList<Clinic> clinicList = Clinic.find.where().eq("primaryCity", PrimaryCity.find.byId(Long.parseLong(session(Constants.CITY_ID))));
			if(locality != null && !(locality.equalsIgnoreCase("0"))){
				loc = Locality.find.byId(Long.parseLong(locality));
				clinicList.eq("address.locality", loc);
			}
			if((key!= null)&& !(key.equalsIgnoreCase("any")) && !(key.trim().isEmpty())){
				clinicList.like("searchIndex","%"+key.trim().toLowerCase()+"%");
			}
			return ok(views.html.doctor.clinicList.render(clinicList.findList()));
		}else{
			flash().put("alert", new Alert("alert-info","Please Select City.").toString());
			return ok(views.html.clinic.searchClinics.render(key,loc,true));
		}
		/*if(searchStr.length() < 4){
			flash().put("alert", new Alert("alert-danger", "The searck key should contain atleast four charecters").toString());
			return ok(views.html.clinic.searchClinics.render(searchKey,new ArrayList<Clinic>(),false));
		}
		else{
			final PrimaryCity city = PrimaryCity.find.byId(Long.parseLong(session(Constants.CITY_ID)));
			final List<Clinic> clinicList =  Clinic.find.where()
					.eq("primaryCity", city)
					.like("searchIndex","%"+searchStr+"%")
					.findList();
			return ok(views.html.clinic.searchClinics.render(searchKey,clinicList,true));
		}*/
	}
	/**
	 * @author lakshmi
	 * Action to get All Localities of primaryCity.
	 * @return
	 */
	public static List<Locality> getPrimaryCityLocations(){
		if((session(Constants.CITY_ID) != null)){
			final PrimaryCity primaryCity = PrimaryCity.find.byId(Long.parseLong(session(Constants.CITY_ID)));
			return  Locality.find.where().eq("primaryCity", primaryCity).findList();

		}
		else{
			return new ArrayList<Locality>();
		}
	}
	/**
	 * @author lakshmi
	 * Action to get All Localities of primaryCity.
	 * @return
	 */
	public static Result getPrimaryCityLocations(final String cityId){
		List<Locality> localities = new ArrayList<Locality>();
		PrimaryCity primaryCity = null;
		if((cityId.isEmpty()) && (session(Constants.CITY_ID) != null)){
			primaryCity = PrimaryCity.find.byId(Long.parseLong(session(Constants.CITY_ID)));
			localities =  Locality.find.where().eq("primaryCity", primaryCity).findList();
		}
		else if(!(cityId.isEmpty())){
			primaryCity = PrimaryCity.find.byId(Long.parseLong(cityId));
			localities = Locality.find.where().eq("primaryCity", primaryCity).findList();
		}
		return ok(views.html.localities.render(primaryCity, localities));
	}
	/**
	 * @author lakshmi
	 * Action to get all Doctor's names
	 * GET/doctor/get-names-json/:loc/:spez
	 */
	@BodyParser.Of(BodyParser.Json.class)
	public static Result getAllDoctorsInCity(final String locality,final String spez) {
		String[] result = null;
		final Set<Doctor> doctors = new HashSet<Doctor>();
		if(session(Constants.CITY_ID) != null){
			final ExpressionList<DoctorClinicInfo> doctorClinicList = DoctorClinicInfo.find.where().eq("clinic.primaryCity", PrimaryCity.find.byId(Long.parseLong(session(Constants.CITY_ID))));
			if(locality != null && !(locality.equalsIgnoreCase("0"))){
				doctorClinicList.eq("clinic.address.locality", Locality.find.byId(Long.parseLong(locality)));
			}
			if((spez != null) && !(spez.equalsIgnoreCase("any")) && !(spez.trim().isEmpty())){
				final MasterSpecialization	specialization = MasterSpecialization.find.where().ieq("name", spez).findUnique();
				doctorClinicList.in("doctor.specializationList", specialization);
			}
			final int arrayLength = Doctor.find.findRowCount()+doctorClinicList.findList().size();
			result = new String[arrayLength];
			int i = 0;
			for (final DoctorClinicInfo doctorClinicInfo : doctorClinicList.findList()) {
				doctors.add(doctorClinicInfo.doctor);
			}
			for (final Doctor doctorClinicInfo : doctors) {
				result[i] = doctorClinicInfo.appUser.name;
				i++;
			}
		}else{
			result = new String[1];
		}

		final JSONArray jsonArray = new JSONArray(Arrays.asList(result));
		return ok(jsonArray.toString());
	}
	/**
	 * @author lakshmi
	 * Action to get all Pharmacy names
	 * GET/pharmacy/get-names-json/:loc
	 */
	@BodyParser.Of(BodyParser.Json.class)
	public static Result getAllPharmaciesInCity(final String locality) {
		String[] result = null;
		if(session(Constants.CITY_ID) != null){
			final ExpressionList<Pharmacy> pharmacyList = Pharmacy.find.where().eq("primaryCity", PrimaryCity.find.byId(Long.parseLong(session(Constants.CITY_ID))));
			if(locality != null && !(locality.equalsIgnoreCase("0"))){
				pharmacyList.eq("address.locality", Locality.find.byId(Long.parseLong(locality)));
			}
			final int arrayLength = Pharmacy.find.findRowCount()+pharmacyList.findList().size();
			result = new String[arrayLength];
			int i = 0;
			for (final Pharmacy pharmacy : pharmacyList.findList()) {
				result[i] = pharmacy.name;
				i++;
			}
		}else{
			result = new String[1];
		}
		final JSONArray jsonArray = new JSONArray(Arrays.asList(result));
		return ok(jsonArray.toString());
	}
	/**
	 * @author lakshmi
	 * Action to get all Diagnostic Centre names
	 * GET/diagnostic/get-names-json/:loc
	 */
	@BodyParser.Of(BodyParser.Json.class)
	public static Result getAllDiagnosticsInCity(final String locality) {
		String[] result = null;
		if(session(Constants.CITY_ID) != null){
			final ExpressionList<DiagnosticCentre> diagnosticList = DiagnosticCentre.find.where().eq("primaryCity", PrimaryCity.find.byId(Long.parseLong(session(Constants.CITY_ID))));
			if(locality != null && !(locality.equalsIgnoreCase("0"))){
				diagnosticList.eq("address.locality", Locality.find.byId(Long.parseLong(locality)));

			}
			final int arrayLength = DiagnosticCentre.find.findRowCount()+diagnosticList.findList().size();
			result = new String[arrayLength];
			int i = 0;
			for (final DiagnosticCentre diagnosticCentre : diagnosticList.findList()) {
				result[i] = diagnosticCentre.name;
				i++;
			}
		}else{
			result = new String[1];

		}
		final JSONArray jsonArray = new JSONArray(Arrays.asList(result));
		return ok(jsonArray.toString());
	}

}
