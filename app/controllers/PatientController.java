package controllers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import models.Alert;
import models.AppUser;
import models.diagnostic.DiagnosticCentre;
import models.doctor.Appointment;
import models.doctor.AppointmentStatus;
import models.doctor.DaySchedule;
import models.doctor.Doctor;
import models.doctor.DoctorClinicInfo;
import models.doctor.QuestionAndAnswer;
import models.patient.Patient;
import models.patient.PatientDoctorInfo;
import models.pharmacist.Pharmacy;
import play.Logger;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import actions.BasicAuth;
import beans.QuestionAndAnswerBean;

@BasicAuth
public class PatientController extends Controller {

	public static Result scheduleAppointment() {
		final Map<Date, List<Appointment>> appointmentMap = null;
		return ok(views.html.patient.scheduleAppointment.render(appointmentMap,null));
	}

	/**
	 * @author Mitesh
	 * Action to display a form which has lists of appointment as per date is provided
	 *  GET/patient/display-appointment/:docClinicId/:timeMillis
	 */
	public static Result displayAppointment(final Long docClinId,Long timeMillis) {

		int slots=0;

		final Map<Date, List<Appointment>> appointmentMap = new LinkedHashMap<Date, List<Appointment>>();

		final DoctorClinicInfo doctorClinicInfo = DoctorClinicInfo.find.byId(docClinId);


		final Calendar calendarFrom = Calendar.getInstance();
		calendarFrom.setTime(new Date(timeMillis));

		Logger.warn(calendarFrom.getTime().toString());

		final Calendar calendarTo = Calendar.getInstance();
		calendarTo.setTime(new Date(timeMillis));

		final Calendar calendar1=Calendar.getInstance();
		final Calendar calendar2=Calendar.getInstance();
		final SimpleDateFormat dateFormat=new SimpleDateFormat("kk:mm");


		for (int i = 0; i <25; i++) {

			for (final DaySchedule schedule : doctorClinicInfo.scheduleDays) {
				try{
					calendar1.setTime(dateFormat.parse(schedule.fromTime));

					calendar2.setTime(dateFormat.parse(schedule.toTime));

				}
				catch(final ParseException exception){
					exception.printStackTrace();
				}
				Logger.warn("docclinic");


				calendarFrom.set(Calendar.HOUR_OF_DAY, calendar1.get(Calendar.HOUR_OF_DAY));
				calendarFrom.set(Calendar.MINUTE,calendar1.get(Calendar.MINUTE));
				calendarFrom.set(Calendar.SECOND, 0);
				calendarFrom.set(Calendar.MILLISECOND, 0);

				calendarTo.set(Calendar.HOUR_OF_DAY, calendar2.get(Calendar.HOUR_OF_DAY));
				calendarTo.set(Calendar.MINUTE,calendar2.get(Calendar.MINUTE));
				calendarTo.set(Calendar.SECOND, 0);
				calendarTo.set(Calendar.MILLISECOND, 0);

				Logger.info("from**"+calendarFrom.getTime().toString());
				Logger.info("to**"+calendarTo.getTime().toString());


				final List<Appointment> listAppointments = Appointment.getAvailableAppointmentList(doctorClinicInfo.id,calendarFrom.getTime(),calendarTo.getTime());
				if(listAppointments.size() != 0 ){
					appointmentMap.put(calendarFrom.getTime(), listAppointments);
					slots=Math.max(slots,listAppointments.size());

					Logger.info("from**"+calendarFrom.getTime().toString());
					Logger.info("to**"+calendarFrom.getTime().toString());
					Logger.info("");
				}


				calendarFrom.add(Calendar.DATE, 1);

				calendarTo.add(Calendar.DATE, 1);
			}

		}
		/*return ok(views.html.patient.scheduleAppointment.render(appointmentMap,
				 slots));*/
		Logger.warn(""+appointmentMap.size());

		return ok(views.html.patient.appointmentForm.render(appointmentMap,slots));
	}


	public static Form<Patient> form = Form.form(Patient.class);

	public static Form<AppUser> registrationForm = Form.form(AppUser.class);

	public static Form<QuestionAndAnswerBean> questionAndAnswerForm = Form
			.form(QuestionAndAnswerBean.class);

	public static Result register() {
		return ok(views.html.registerAppUser.render(registrationForm));
	}

	public static Result registerProccess() {

		final Form<AppUser> filledForm = registrationForm.bindFromRequest();

		if (filledForm.hasErrors()) {
			return badRequest(views.html.registerAppUser.render(filledForm));
		} else {
			final AppUser appUser = filledForm.get();
			final Patient patient = new Patient();
			// appUser.patient=patient;
			appUser.save();
			patient.appUser = appUser;
			patient.save();
			return ok("Registerd ");
		}

	}

	public static Result form() {

		return ok(views.html.createPatient.render(form));
		// return TODO;
	}

	public static Result enterForm() {
		return ok(views.html.enter.render("hello"));
		// return TODO;
	}

	public static Result process() {
		final Form<Patient> filledForm = form.bindFromRequest();

		if (filledForm.hasErrors()) {
			// System.out.println(filledForm.errors());
			return badRequest(views.html.createPatient.render(filledForm));
		} else {
			final Patient patient = filledForm.get();

			if (patient.id == null) {

				patient.save();
			} else {

				patient.update();
			}
		}
		// return ok(views.html.scheduleAppointment.render("hello"));
		// return redirect(routes.UserController.list());
		return TODO;

	}

	/**
	 * @author Mitesh
	 * Action to search Doctor and display it
	 *  GET /patient/delete-fav-doc/:id
	 */
	public static Result searchDoctors(final String search) {

		// final List<Patient> patients=Patient.find.where().eq("appUser.email",
		// "mitesh@greensoftware.in").findList();


		final List<Doctor> doctors = Doctor.find
				.where().like("searchIndex","%"+search+"%").findList();

		return ok(views.html.doctor.searchedDoctors.render(true, search, doctors));
	}


	public static Result displayQuestion() {
		return ok(views.html.patient.askQuestion.render(questionAndAnswerForm));
	}

	// Question Asked By A Patients
	public static final Result askQuestion() {
		try {
			final QuestionAndAnswerBean questionAndAnswerBean = questionAndAnswerForm
					.bindFromRequest().get();
			final QuestionAndAnswer questionAndAnswer = questionAndAnswerBean
					.toEntity();
			questionAndAnswer.questionBy = AppUser.find.byId(LoginController
					.getLoggedInUser().id);

			questionAndAnswer.questionDate = new Date();

			questionAndAnswer.save();

			return ok("0");
		} catch (final Exception e) {
			e.printStackTrace();
			return ok("-1");
		}
	}

	/*
	 * displaying all diagnostic centers
	 */
	public static Result diagnosticList() {
		final List<DiagnosticCentre> allList = DiagnosticCentre.find.all();

		return ok(views.html.diagnostic.patientDiagnosticCenterList.render(allList));

	}

	/*
	 * saving diagnostic center in patient favorite list
	 */
	public static Result saveDiagnosticCenter(final Long id) {
		final DiagnosticCentre dc = DiagnosticCentre.find.byId(id);
		final Patient patient = LoginController.getLoggedInUser().getPatient();

		patient.diagnosticCenterList.add(dc);
		patient.update();

		return ok("diagnostic center persisted in patient table");

	}

	/*
	 * displaying diagnostic centers which are there in patient favorite list
	 */

	public static Result myDiagnosticCenters() {
		final Long id = LoginController.getLoggedInUser().getPatient().id;
		final Patient diagnoCenterList = Patient.find.where().eq("id", id)
				.findUnique();
		final List<DiagnosticCentre> list = diagnoCenterList.diagnosticCenterList;

		return ok(views.html.patient.myDiagnoList.render(list));

	}

	/*
	 * removing diagnostic center from patient favorite diagnostic center list
	 */

	public static Result removePatientDiagnoCenter(final Long id) {

		final Patient patient = LoginController.getLoggedInUser().getPatient();
		final DiagnosticCentre centre = DiagnosticCentre.find.byId(id);
		patient.diagnosticCenterList.remove(centre);
		patient.update();

		return redirect(routes.PatientController.myDiagnosticCenters());

	}
	/**
	 * @author Mitesh
	 * Action to display currently logged in Patient'Doctor List
	 *  GET /patient/favdoctors
	 */
	public static Result patientMyFavDoctors() {
		final Patient patient=LoginController.getLoggedInUser().getPatient();
		return ok(views.html.patient.fav_doctors.render(patient.patientDoctorInfos));
	}

	/**
	 * @author Mitesh
	 * Action to Delete one of the doctor from currently logged in Patient
	 *  GET /patient/delete-fav-doc/:id
	 */
	public static Result deleteMyFavDoctors(final Long patDocid) {

		final PatientDoctorInfo patientDoctorInfo=PatientDoctorInfo.find.byId(patDocid);
		patientDoctorInfo.delete();
		flash().put("alert", new Alert("alert-success","Successfully Deleted:"+patientDoctorInfo.doctor.appUser.name).toString());
		return redirect(routes.PatientController.patientMyFavDoctors());
	}

	public static Result staticPatientMyFavDoctors() {
		return ok(views.html.patient.static_fav_doctors.render());
	}

	public static Result staticPatientNewAppointment() {
		return ok(views.html.patient.static_patient_new_appointment.render());
	}

	/**
	 * @author Mitesh
	 * Action to show a forms which have Doctor and it will show the available and booked appointment
	 *  GET /patient/new-appointment/:docclinicid
	 */
	public static Result patientNewAppointment(final Long docclinicid) {
		final DoctorClinicInfo clinicInfo=DoctorClinicInfo.find.byId(docclinicid);
		return ok(views.html.patient.patientNewAppointment.render(clinicInfo));
	}

	public static Result staticPatientViewAppointments(){
		return ok(views.html.patient.static_patient_view_appointments.render());
	}
	/**
	 * @author lakshmi
	 * Action to add favorite pharmacy of the Doctor to the list of Patient of loggedin PATIENT
	 * GET/patient/add-favorite-pharmacy/:pharmacyId/:str
	 */
	public static Result addFavoritePharmacy(final Long pharmacyId,final String searchStr) {
		final Patient patient = LoginController.getLoggedInUser().getPatient();
		final Pharmacy pharmacy = Pharmacy.find.byId(pharmacyId);
		if(patient.pharmacyList.contains(pharmacy)!= true){
			patient.pharmacyList.add(pharmacy);
			patient.update();
		}
		else{
			flash().put("alert", new Alert("alert-info", pharmacy.name+" Already existed in the Favorite List.").toString());
		}
		final List<Pharmacy> pharmacyList = new ArrayList<Pharmacy>();
		for (final Pharmacy pharmacy2 : Pharmacy.find.where().like("searchIndex","%"+searchStr+"%").findList()) {
			if(patient.pharmacyList.contains(pharmacy2) != true){
				pharmacyList.add(pharmacy2);
			}
		}
		return ok(views.html.pharmacist.searched_pharmacies.render(true,searchStr,pharmacyList));
		//		return redirect(routes.UserActions.dashboard());
	}

	/**
	 * @author lakshmi
	 * Action to list out favorite pharmacies of Patient of loggedin PATIENT
	 * GET/patient/my-favorite-pharmacies
	 */
	public static Result myFavoritePharmacies() {
		final Patient patient = LoginController.getLoggedInUser().getPatient();
		return ok(views.html.favorite_pharmacy_list.render(patient.pharmacyList,0L,patient.id));
	}
	/**
	 * @author lakshmi
	 * Action to remove pharmacy from  favorite pharmacies List of Patient of loggedin PATIENT
	 * GET/patient/remove-favorite-pharmacy/:patientId/:pharmacyId
	 */
	public static Result removeFavoritePharmacy(final Long patientId,final Long pharmacyId) {
		final Patient patient = Patient.find.byId(patientId);
		Logger.info("before delete list size()==="+patient.pharmacyList.size());
		patient.pharmacyList.remove(Pharmacy.find.byId(pharmacyId));
		patient.update();
		Logger.info("after delete list size()==="+patient.pharmacyList.size());
		//return redirect(routes.UserActions.dashboard());
		return ok(views.html.favorite_pharmacy_list.render(patient.pharmacyList,0L,patient.id));
	}

	/**
	 * @author Mitesh
	 * Action to show a forms which have currently logged in patient requested appointments
	 *  GET		/patient/my-appointments
	 */
	public static Result viewMyAppointments(){
		AppUser patient=LoginController.getLoggedInUser();

		List<Appointment> patientApppointments=Appointment.find.where().eq("requestedBy", patient).findList();
		return ok(views.html.patient.patientViewAppointments.render(patientApppointments));
	}

	/**
	 * @author Mitesh
	 * Action to process requested appointments
	 *  POST 	/patient/process-appointment
	 */
	public static Result processAppointment(Long apptId) {
		String remark=request().body().asFormUrlEncoded().get("remark")[0];

		Logger.warn(remark);
		Appointment appointment=Appointment.find.byId(apptId);
		appointment.appointmentStatus=AppointmentStatus.APPROVED;
		appointment.remarks=remark;
		appointment.requestedBy=LoginController.getLoggedInUser();
		return ok("appointment save");
	}

}
