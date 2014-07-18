package controllers;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import models.Alert;
import models.AppUser;
import models.Patient;
import models.diagnostic.DiagnosticCentre;
import models.doctor.Appointment;
import models.doctor.Doctor;
import models.doctor.QuestionAndAnswer;
import models.patient.PatientDoctorInfo;
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

	public static Result displayAppointment(final String id) {
		final List<Appointment> listAppointments = null;
		final int slots=1000;

		final Map<Date, List<Appointment>> appointmentMap = new LinkedHashMap<Date, List<Appointment>>();
		/*final Doctor doctor = Doctor.find.byId(Long.parseLong(id));
		final Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());

		for (int i = 0; i <4 ; i++) {
			for (DoctorClinicInfo clinicInfo: doctor.doctorClinicInfoList) {

				for (DaySchedule schedule : clinicInfo.schedulDays) {

					calendar.set(Calendar.HOUR_OF_DAY, schedule.fromTime);
					calendar.set(Calendar.MINUTE, 0);
					calendar.set(Calendar.SECOND, 0);
					calendar.set(Calendar.MILLISECOND, 0);
					listAppointments = Appointment.getAvailableAppointmentList(doctor,calendar.getTime(),schedule.toTime);
					if(listAppointments.size() != 0 ){
						appointmentMap.put(calendar.getTime(), listAppointments);
						slots=Math.min(slots,listAppointments.size());
					}


					calendar.add(Calendar.DATE, 1);

					System.out.print(calendar.getTime());
				}
			}
		}
		Logger.warn(""+listAppointments.size());
		 */		return ok(views.html.patient.scheduleAppointment.render(appointmentMap,
				 slots));
	}

	public static Result processAppointment() {
		return ok();
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

		return ok(views.html.doctor.searchedDoctors.render(doctors));
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
	 *  GET
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

}
