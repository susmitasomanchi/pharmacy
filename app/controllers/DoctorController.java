package controllers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import models.Alert;
import models.AppUser;
import models.LanguageAppUser;
import models.Patient;
import models.Role;
import models.doctor.Appointment;
import models.doctor.AppointmentStatus;
import models.doctor.Day;
import models.doctor.DaySchedule;
import models.doctor.Doctor;
import models.doctor.DoctorAward;
import models.doctor.DoctorClinicInfo;
import models.doctor.DoctorEducation;
import models.doctor.DoctorExperience;
import models.doctor.DoctorLanguage;
import models.doctor.DoctorPublication;
import models.doctor.DoctorSocialWork;
import models.doctor.QuestionAndAnswer;
import play.Logger;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import actions.BasicAuth;
import beans.DoctorClinicInfoBean;
import beans.DoctorDetailBean;
import beans.PatientBean;
import beans.QuestionAndAnswerBean;

import com.avaje.ebean.Ebean;


@BasicAuth
public class DoctorController extends Controller {

	public static Form<Doctor> form= Form.form(Doctor.class);
	public static Form<PatientBean> patientForm = Form.form(PatientBean.class);
	public static Form<DoctorClinicInfoBean> clinicForm = Form.form(DoctorClinicInfoBean.class);
	public static Form<DoctorExperience> experienceForm = Form.form(DoctorExperience.class);
	public static Form<DoctorEducation> educationForm = Form.form(DoctorEducation.class);
	public static Form<DoctorSocialWork> socialWorkForm = Form.form(DoctorSocialWork.class);
	public static Form<DoctorAward> awardForm = Form.form(DoctorAward.class);
	public static Form<DoctorDetailBean> languageForm = Form.form(DoctorDetailBean.class);
	public static Form<QuestionAndAnswerBean> questionAndAnswerForm = Form.form(QuestionAndAnswerBean.class);
	public static Form<DoctorClinicInfo> doctorClinicForm	=Form.form(DoctorClinicInfo.class);
	public static Form<DoctorPublication> publicationForm = Form.form(DoctorPublication.class);

	public static Doctor loggedIndoctor=LoginController.getLoggedInUser().getDoctor();

	public static Result requestAppointment(){


		final String param[] =request().body().asFormUrlEncoded().get("datetime");
		try{
			final Appointment appointment=Appointment.find.byId(Long.parseLong(param[1]));
			appointment.remarks=param[0];
			appointment.requestedBy=LoginController.getLoggedInUser();
			appointment.appointmentStatus=AppointmentStatus.APPROVED;
			appointment.update();
			return ok("0");
		}
		catch(final Exception e){
			e.printStackTrace();
			return ok("-1");
		}


	}


	public static Result doctorProfile(){
		return ok(views.html.doctor.doctorDashboard.render(loggedIndoctor));
	}




	/**
	 * @author Mitesh
	 * Action to render a page with form for adding new clinic of the loggedInDoctor
	 * GET /doctor/new-clinic
	 */
	public static Result newClinic(){
		return ok(views.html.doctor.newClinic.render(clinicForm));
	}


	/**
	 * @author Mitesh
	 * Action to process adding new clinic of the loggedInDoctor by creating a clinicInfo object
	 * and then calls DoctorController.createAppointment(clinicInfo) method to create requisite appointments
	 * POST /doctor/new-clinic
	 */
	public static Result processNewClinic(){
		final Form<DoctorClinicInfoBean> filledForm = clinicForm.bindFromRequest();
		if(filledForm.hasErrors()){
			return ok(views.html.doctor.newClinic.render(clinicForm));
		}
		else{
			final DoctorClinicInfo clinicInfo = filledForm.get().toDoctorClinicInfo();
			clinicInfo.doctor = LoginController.getLoggedInUser().getDoctor();
			clinicInfo.save();
			return DoctorController.createAppointment(clinicInfo);
		}
	}


	/**
	 * @author Mitesh
	 * Action to update one of loggedInDoctor's clinics (non-schedule) information like name, address etc.
	 * POST   /doctor/update-clinic
	 */
	public static Result processUpdateClinicInfo() {
		final Form<DoctorClinicInfoBean> filledForm = clinicForm.bindFromRequest();
		if(filledForm.hasErrors()){
			return ok(views.html.doctor.editClinic.render(clinicForm,new ArrayList<String>(),new ArrayList<String>()));
		}
		else{
			final DoctorClinicInfo clinicInfo = filledForm.get().toDoctorClinicInfo();
			//server-side check
			if(clinicInfo.doctor.id.longValue() != LoginController.getLoggedInUser().id.longValue()){
				return redirect(routes.LoginController.processLogout());
			}
			final DoctorClinicInfo clinicInfoPrevious=DoctorClinicInfo.find.byId(clinicInfo.id);
			clinicInfoPrevious.clinic.name = clinicInfo.clinic.name;
			clinicInfoPrevious.clinic.contactNo=clinicInfo.clinic.contactNo;
			clinicInfoPrevious.clinic.contactPersonName=clinicInfo.clinic.contactPersonName;
			//clinicInfoPrevious.clinic.update();
			clinicInfoPrevious.clinic.address.addrressLine1=clinicInfo.clinic.address.addrressLine1;
			clinicInfoPrevious.clinic.address.area=clinicInfo.clinic.address.area;
			clinicInfoPrevious.clinic.address.state=clinicInfo.clinic.address.state;
			clinicInfoPrevious.clinic.address.city=clinicInfo.clinic.address.city;
			clinicInfoPrevious.clinic.address.latitude=clinicInfo.clinic.address.latitude;
			clinicInfoPrevious.clinic.address.longitude=clinicInfo.clinic.address.longitude;
			clinicInfoPrevious.clinic.address.update();
			clinicInfoPrevious.update();
			flash().put("alert", new Alert("alert-success","Successfully Updated").toString());
			return redirect(routes.DoctorController.myClinics());
		}
	}

	public static Result doctorExperience(){
		Logger.info(loggedIndoctor.doctorExperienceList.size()+" size of list");
		return ok(views.html.doctor.doctorExperience.render(experienceForm));
	}

	public static Result processDoctorExperience() {
		final Form<DoctorExperience> experienceFilledForm = experienceForm.bindFromRequest();

		if (experienceFilledForm.hasErrors()) {
			// System.out.println(filledForm.errors());
			return badRequest(views.html.doctor.doctorExperience.render(experienceFilledForm));
		} else {

			final DoctorExperience doctorExperience=experienceFilledForm.get();
			loggedIndoctor.doctorExperienceList.add(doctorExperience);

			loggedIndoctor.update();
		}
		return redirect(routes.DoctorController.doctorProfile());
	}
	// return ok(views.html.scheduleAppointment.render("hello"));
	// return redirect(routes.UserController.list());



	public static Result doctorEducation(){
		Logger.info(loggedIndoctor.doctorEducationList.size()+" size of list");
		return ok(views.html.doctor.doctorEducation.render(educationForm));
	}

	public static Result processDoctorEducation() {
		final Form<DoctorEducation> educationfilledForm = educationForm.bindFromRequest();

		if (educationfilledForm.hasErrors()) {

			return badRequest(views.html.doctor.doctorEducation.render(educationfilledForm));
		} else {

			final DoctorEducation doctorEducation=educationfilledForm.get();

			loggedIndoctor.doctorEducationList.add(doctorEducation);

			loggedIndoctor.update();

		}

		return redirect(routes.DoctorController.doctorProfile());
	}


	public static Result doctorPublication(){
		Logger.info(loggedIndoctor.doctorPublicationList.size()+" size of list");

		return ok(views.html.doctor.doctorPublications.render(publicationForm));
	}


	public static Result processDoctorPublication() {
		final Form<DoctorPublication> publicationfilledForm = publicationForm.bindFromRequest();
		// Logger.info("enteredt");

		if (publicationfilledForm.hasErrors()) {
			Logger.info("bad request");
			// System.out.println(filledForm.errors());
			return badRequest(views.html.doctor.doctorPublications.render(publicationfilledForm));
		} else {
			final DoctorPublication doctorPublication=publicationfilledForm.get();

			loggedIndoctor.doctorPublicationList.add(doctorPublication);

			loggedIndoctor.update();
		}
		return redirect(routes.DoctorController.doctorProfile());

	}


	public static Result doctorAward(){
		Logger.info(loggedIndoctor.doctorAwardList.size()+" size of list");
		return ok(views.html.doctor.doctorAward.render(awardForm));
	}



	public static Result processDoctorAward() {
		final Form<DoctorAward> awardfilledForm = awardForm.bindFromRequest();

		if (awardfilledForm.hasErrors()) {
			// System.out.println(filledForm.errors());
			return badRequest(views.html.doctor.doctorAward.render(awardfilledForm));
		} else {
			final DoctorAward doctorAward=awardfilledForm.get();

			loggedIndoctor.doctorAwardList.add(doctorAward);

			loggedIndoctor.update();
		}
		return redirect(routes.DoctorController.doctorProfile());

	}


	public static Result doctorLanguage(){
		return ok(views.html.doctor.doctorLanguage.render(languageForm));
	}



	public static Result processDoctorLanguage(){
		final Form<DoctorDetailBean> languagefilledForm = languageForm.bindFromRequest();
		final Doctor doctor=LoginController.getLoggedInUser().getDoctor();

		// Logger.info("enteredt");
		if (languagefilledForm.hasErrors()) {
			Logger.info("bad request");
			// System.out.println(filledForm.errors());
			return badRequest(views.html.doctor.doctorLanguage.render(languagefilledForm));
		} else {
			final DoctorLanguage docLan=new DoctorLanguage();


			final List<LanguageAppUser> doctorLanguage = languagefilledForm.get().toLanguageAppUser();
			//doctor.doctorLanguageList=doctorLanguage;
			doctor.update();
			docLan.languageAppUsers=doctorLanguage;

			docLan.save();
		}
		return redirect(routes.DoctorController.doctorProfile());

	}


	public static Result doctorSocialWork(){
		return ok(views.html.doctor.doctorSocialWork.render(socialWorkForm));
	}



	public static Result processDoctorSocialWork() {
		final Form<DoctorSocialWork> socialWorkfilledForm = socialWorkForm.bindFromRequest();
		// Logger.info("enteredt");

		if (socialWorkfilledForm.hasErrors()) {
			Logger.info("bad request");
			// System.out.println(filledForm.errors());
			return badRequest(views.html.doctor.doctorSocialWork.render(socialWorkfilledForm));
		} else {
			final DoctorSocialWork doctorSocialWork=socialWorkfilledForm.get();

			loggedIndoctor.doctorSocialWorkList.add(doctorSocialWork);

			loggedIndoctor.update();

		}

		return redirect(routes.DoctorController.doctorProfile());

	}




	/**
	 * @author Mitesh
	 * Action to update one of loggedInDoctor's clinics appointments/schedule information
	 * POST   /doctor/update-clinic-schedule
	 */
	public static Result processUpdateClinicSchedule() {
		final Form<DoctorClinicInfoBean> filledForm = clinicForm.bindFromRequest();
		if(filledForm.hasErrors()){
			return ok(views.html.doctor.editClinic.render(clinicForm,new ArrayList<String>(),new ArrayList<String>()));
		}
		else{
			final DoctorClinicInfo clinicInfo = filledForm.get().toDoctorClinicInfo();
			//server-side check
			if(clinicInfo.doctor.id.longValue() != LoginController.getLoggedInUser().id.longValue()){
				return redirect(routes.LoginController.processLogout());
			}
			final DoctorClinicInfo clinicInfoPrevious=DoctorClinicInfo.find.byId(clinicInfo.id);
			for (final DaySchedule sc4: clinicInfo.scheduleDays) {
				Logger.info("test day"+sc4.day);
			}
			Ebean.delete(clinicInfoPrevious.scheduleDays);
			clinicInfoPrevious.scheduleDays=clinicInfo.scheduleDays;
			clinicInfoPrevious.slot=clinicInfo.slot;
			clinicInfoPrevious.slotmr=clinicInfo.slotmr;
			clinicInfoPrevious.update();
			flash().put("alert", new Alert("alert-success","Successfully Updated").toString());
			return DoctorController.reCreateAppointment(clinicInfoPrevious);
		}
	}


	/**	/**
	 * @author Mitesh
	 * Action to delete (make inActive) one of loggedInDoctor's clinics
	 * GET	/doctor/delete-clinic/:id
	 */
	public static Result deleteClinic(final Long id) {
		final DoctorClinicInfo clinicInfo = DoctorClinicInfo.find.byId(id);
		//server-side check
		if(clinicInfo.doctor.id.longValue() != LoginController.getLoggedInUser().id.longValue()){
			return redirect(routes.LoginController.processLogout());
		}
		final Calendar calendar=Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.set(Calendar.HOUR_OF_DAY, 00);
		calendar.set(Calendar.MINUTE, 00);

		final List<Appointment> approvedAppts=Appointment.find.where()
				.eq("doctor", clinicInfo.doctor)
				.eq("clinic", clinicInfo.clinic)
				.eq("appointmentTime", calendar.getTime())
				.eq("appointmentStatus", AppointmentStatus.APPROVED).findList();

		/*
		 * @TODO:
		 * Notify patients / MRs / doctor (via mail, sms etc.) regarding cancelled appointments
		 * due to deletion of clinic
		 */

		Ebean.delete(approvedAppts);
		final List<Appointment> availableAppts=Appointment.find.where()
				.eq("doctor", clinicInfo.doctor)
				.eq("clinic", clinicInfo.clinic)
				.eq("appointmentStatus", AppointmentStatus.AVAILABLE).findList();

		Ebean.delete(availableAppts);
		clinicInfo.active = false;
		clinicInfo.doctor.update();
		clinicInfo.update();
		flash().put("alert", new Alert("alert-success","Successfully Deleted").toString());
		return redirect(routes.DoctorController.myClinics());
	}


	/**
	 * @author Mitesh
	 * Action to show all active clinics of the loggedIn Doctor
	 * GET /doctor/clinics
	 */
	public static Result myClinics(){
		final Doctor loggedInDoctor = LoginController.getLoggedInUser().getDoctor();
		return ok(views.html.doctor.myClinics.render(loggedInDoctor.getActiveClinic()));
	}


	public static Result newAssistant(){
		return ok(views.html.doctor.newAssistant.render());
	}





	/**
	 * @author Mitesh
	 * Action to show form to edit one of loggedIn doctor's clinic
	 * GET /doctor/edit-clinic/:id
	 */
	public static Result manageClinic(final Long docClinicId) {
		final DoctorClinicInfo doctorClinicInfo = DoctorClinicInfo.find.byId(docClinicId);
		//server-side check
		if(doctorClinicInfo.doctor.id.longValue() != LoginController.getLoggedInUser().id.longValue()){
			return redirect(routes.LoginController.processLogout());
		}
		final DoctorClinicInfoBean bean = doctorClinicInfo.toBean();
		final Form<DoctorClinicInfoBean> filledForm = clinicForm.fill(doctorClinicInfo.toBean());
		for (final String from : bean.fromHrs) {
			Logger.warn(from);
		}
		return ok(views.html.doctor.editClinic.render(filledForm,bean.daysOfWeek,bean.daysOfWeekMr));
	}



	//register patient by doctor

	public static Result registerPatient(){

		return ok(views.html.registerPatient.render(patientForm));
	}


	public static Result registerPatientProccess(){

		final Form<PatientBean> filledForm=patientForm.bindFromRequest();

		if(filledForm.hasErrors()){
			return ok(views.html.registerPatient.render(filledForm));

		}else{
			final AppUser appUser=filledForm.get().toAppUserEntity();
			final Patient patient=filledForm.get().toPatientEntity();

			if((appUser.id==null) && (patient.id==null)){
				appUser.save();
				patient.save();
			}else{
				appUser.update();
				patient.update();
			}
		}


		return ok("patient register successFully");
	}



	public static Result displayAnswer(){
		final AppUser user = LoginController.getLoggedInUser();
		final Doctor doctor=user.getDoctor();
		List<QuestionAndAnswer> qaList=new ArrayList<QuestionAndAnswer>();
		if(doctor!=null){
			qaList = QuestionAndAnswer.find.where()
					.eq("answerBy.id", doctor.id).findList();
		}
		return ok(views.html.doctor.ansQuestion.render(qaList));

	}
	//Question Answered By Doctor
	public static Result answerQuestion(final Long qaId) {
		final QuestionAndAnswerBean qaBean = questionAndAnswerForm.bindFromRequest().get();
		final QuestionAndAnswer qa = QuestionAndAnswer.find.byId(qaId);
		qa.answer = qaBean.answer;
		qa.answerDate = new Date();
		qa.update();
		flash().put("alert", "saved answer successfully");
		return redirect(routes.DoctorController.displayAnswer());


	}

	/**
	 * @author Mitesh
	 * Action to process adding new clinic of the loggedInDoctor by creating a clinicInfo object
	 * and then calls DoctorController.createAppointment(clinicInfo) method to create requisite appointments
	 * NO ROUTE - called internally from DoctorController.processNewClinic() and from
	 * DoctorController.reCreateAppointment(clinicInfo)
	 */
	private static Result createAppointment(final DoctorClinicInfo docClinicInfo) {
		// Server side validation
		if(docClinicInfo.doctor.id.longValue() != LoginController.getLoggedInUser().id.longValue()){
			return redirect(routes.LoginController.processLogout());
		}
		try{
			final Calendar calendar1=Calendar.getInstance();
			final Calendar calendar2=Calendar.getInstance();
			final SimpleDateFormat dateFormat=new SimpleDateFormat("kk:mm");
			final Doctor doctor=LoginController.getLoggedInUser().getDoctor();
			final Calendar calendar = Calendar.getInstance();
			calendar.setTime(new Date());
			for(int date=0;date<31;date++){
				for (final DaySchedule schedule : docClinicInfo.scheduleDays) {
					if(schedule.day == Day.getDay(calendar.get(Calendar.DAY_OF_WEEK)-1)){
						try {
							calendar1.setTime(dateFormat.parse(schedule.toTime));
							calendar2.setTime(dateFormat.parse(schedule.fromTime));
						} catch (final Exception e) {
							e.printStackTrace();
						}
						final int hoursToClinic=calendar1.get(Calendar.HOUR_OF_DAY)-calendar2.get(Calendar.HOUR_OF_DAY);
						final int minutsToClinic=calendar1.get(Calendar.MINUTE)-calendar2.get(Calendar.MINUTE);
						Logger.info("total minutes***"+calendar1.get(Calendar.MINUTE));
						calendar.set(Calendar.HOUR_OF_DAY,calendar2.get(Calendar.HOUR_OF_DAY));
						calendar.set(Calendar.MINUTE,calendar2.get(Calendar.MINUTE));
						calendar.set(Calendar.SECOND,0);
						calendar.set(Calendar.MILLISECOND,0);
						if(schedule.requester.equals(Role.PATIENT)){
							for (int j2 = 0; j2 <(((hoursToClinic*60)+minutsToClinic)/docClinicInfo.slot); j2++) {
								if(Appointment.find.where().eq("doctor",doctor).eq("clinic",docClinicInfo.clinic).eq("appointmentTime", calendar.getTime()).findUnique()==null){
									Logger.info("  "+calendar.getTime());
									final Appointment appointment=new Appointment();
									appointment.appointmentStatus=AppointmentStatus.AVAILABLE;
									appointment.appointmentTime=calendar.getTime();
									appointment.clinic=docClinicInfo.clinic;
									appointment.doctor=doctor;
									appointment.save();
									calendar.add(Calendar.MINUTE, docClinicInfo.slot);
								}
								else{
									calendar.add(Calendar.MINUTE, docClinicInfo.slot);
								}
							}
						}else {
							for (int j2 = 0; j2 <(((hoursToClinic*60)+minutsToClinic)/docClinicInfo.slotmr); j2++) {
								if(Appointment.find.where().eq("doctor",doctor).eq("clinic",docClinicInfo.clinic).eq("appointmentTime", calendar.getTime()).findUnique()==null){
									Logger.info("  "+calendar.getTime());
									final Appointment appointment=new Appointment();
									appointment.appointmentStatus=AppointmentStatus.AVAILABLE;
									appointment.appointmentTime=calendar.getTime();
									appointment.clinic=docClinicInfo.clinic;
									appointment.doctor=doctor;
									appointment.save();
									calendar.add(Calendar.MINUTE,docClinicInfo.slotmr);
								}
								else{
									calendar.add(Calendar.MINUTE,docClinicInfo.slotmr);
								}
							}
						}

					}

				}
				Logger.info("***end of shedules");
				calendar.add(Calendar.DATE, 1);
			}
			flash().put("alert", new Alert("alert-success", docClinicInfo.clinic.name+" created successfully.").toString());
			return redirect(routes.DoctorController.myClinics());
		}
		catch (final Exception e){
			Logger.error("ERROR WHILE CREATING APPOINTMENTS.");
			e.printStackTrace();
			flash().put("alert", new Alert("alert-danger", "Sorry. Something went wrong. Please try again.").toString());
			return redirect(routes.DoctorController.newClinic());
		}
	}



	/**
	 * @author Mitesh
	 * Action to update one of loggedIn doctor's clinic's schedule/appointments
	 * This action will delete non-booked appointments and will then call DoctorController.createAppointment(clinicInfo)
	 * to create new appointments with the changed timings.
	 * NO ROUTE - called internally from DoctorController.processUpdateClinicSchedule()
	 */
	private static Result reCreateAppointment(final DoctorClinicInfo clinicInfo) {
		final List<Appointment> appointments = Appointment.find.where()
				.eq("doctor",clinicInfo.doctor)
				.eq("clinic",clinicInfo.clinic)
				.eq("appointmentStatus",AppointmentStatus.AVAILABLE)
				.findList();
		Ebean.delete(appointments);
		return DoctorController.createAppointment(clinicInfo);
	}


	public static Result showPrescriptionForm(final Long appointmentId){
		//final Appointment appointment = Appointment.find.byId(appointmentId);
		return ok();
	}



	public static boolean isListSame(final List<DaySchedule> arrayList1,final List<DaySchedule> arrayList2) {
		if(arrayList1.size() != arrayList2.size()){
			Logger.info("if 1");
			return false;
		}
		for(int i=0;i<arrayList1.size();i++){
			final DaySchedule schedule=arrayList1.get(i);
			final DaySchedule scheduleMr=arrayList2.get(i);
			if(!schedule.equals(scheduleMr)){
				return schedule.equals(scheduleMr);
			}
		}
		return true;
	}



	public static Result processPrescriptionForm(){
		return ok();
	}

	public static Result doctorPrescription() {
		return ok(views.html.doctor.doctor_prescription.render());
	}

	public static Result doctorAppointments() {

		return ok(views.html.doctor.doctor_all_appointments.render());

	}

	public static Result doctorSearchAppointment() {

		return ok(views.html.doctor.doctor_search_appointments.render());

	}

	public static Result doctorViewAppointment() {

		return ok(views.html.doctor.doctor_view_appointment.render());
	}

	//Todays Appointment
	public static Result viewTodaysAppointment() {

		final Calendar calendar=Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND,0);
		calendar.set(Calendar.MILLISECOND,0);


		final List<Appointment> appointments=Appointment.find.where().eq("appointmentStatus", AppointmentStatus.APPROVED).eq("doctor", loggedIndoctor).ge("appointmentTime", calendar.getTime()).findList();

		Logger.warn(""+appointments.toString());
		return ok(views.html.doctor.doctor_appointments.render(appointments));

	}

	public static Result form() {
		return ok(views.html.createDoctor.render(form));
		//return TODO;
	}

	public static Result process() {
		final Form<Doctor> filledForm = form.bindFromRequest();

		if(filledForm.hasErrors()) {

			return badRequest(views.html.createDoctor.render(filledForm));
		}
		else {
			final Doctor doctor= filledForm.get();

			if(doctor.id == null) {

				doctor.save();
			}
			else {

				doctor.update();
			}
		}
		return TODO;
		//return redirect(routes.UserController.list());

	}


	public static Result doctorProfileS() {

		return ok(views.html.doctor.doctor_profile.render());
	}

	public static Result doctorAddClinic() {

		return ok(views.html.doctor.doctor_add_clinic.render());
	}
}
