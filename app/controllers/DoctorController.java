package controllers;

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
import models.doctor.Clinic;
import models.doctor.Day;
import models.doctor.DayOfTheWeek;
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
import beans.ClinicBean;
import beans.DoctorDetailBean;
import beans.PatientBean;
import beans.QuestionAndAnswerBean;

import com.avaje.ebean.Ebean;


@BasicAuth
public class DoctorController extends Controller {

	public static Form<PatientBean> patientForm = Form.form(PatientBean.class);
	public static Form<ClinicBean> clinicForm = Form.form(ClinicBean.class);
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



	public static Result newClinic(){
		return ok(views.html.doctor.newClinic.render(clinicForm,new ArrayList<String>(),new ArrayList<String>()));
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

	//Process new clinic timing Data
	public static Result processNewClinic(){

		final Form<ClinicBean> filledForm = clinicForm.bindFromRequest();
		if(filledForm.hasErrors()){
			return ok(views.html.doctor.newClinic.render(clinicForm,new ArrayList<String>(),new ArrayList<String>()));
		}
		else{
			DoctorClinicInfo clinicInfo =filledForm.get().toDoctorClinicInfo();


			if (filledForm.get().id!=null) {
				final DoctorClinicInfo clinicInfoPrevious=DoctorClinicInfo.find.byId(clinicInfo.id);
				if(!clinicInfo.clinic.name.equals(clinicInfoPrevious.clinic.name)){
					Logger.info("name test"+clinicInfo.clinic.name);
					clinicInfoPrevious.clinic.name = clinicInfo.clinic.name;
					clinicInfoPrevious.clinic.update();
					clinicInfoPrevious.update();
				}
				if(!DoctorController.isListSame(clinicInfo.schedulDays, clinicInfoPrevious.schedulDays)||clinicInfo.slot != clinicInfoPrevious.slot || clinicInfo.slotmr!=clinicInfoPrevious.slotmr){

					//clinicInfoPrevious.schedulDays=null;
					Ebean.delete(clinicInfoPrevious.schedulDays);
					clinicInfoPrevious.schedulDays = clinicInfo.schedulDays;

					clinicInfoPrevious.slot = clinicInfo.slot;
					clinicInfoPrevious.slotmr = clinicInfo.slotmr;
					clinicInfoPrevious.update();


					return DoctorController.reCreateAppointment(clinicInfoPrevious);
				}
				return redirect(routes.DoctorController.myClinics());
			}else{
				clinicInfo.doctor=LoginController.getLoggedInUser().getDoctor();
				clinicInfo.save();

				return DoctorController.createAppointment(clinicInfo);
			}
		}
	}



	public static Result myClinics(){
		final Doctor loggedInDoctor = LoginController.getLoggedInUser().getDoctor();
		Logger.warn(loggedInDoctor.doctorClinicInfoList.size()+"");
		for (final DoctorClinicInfo clinicInfo : loggedInDoctor.doctorClinicInfoList) {
			Logger.warn(clinicInfo.clinic.name);
		}
		return ok(views.html.doctor.myClinics.render(loggedInDoctor.doctorClinicInfoList));

	}


	public static Result newAssistant(){
		return ok(views.html.doctor.newAssistant.render());
	}




	//Edit Or Manage Clinic
	public static Result manageClinic(final Long docClinicId) {

		final DoctorClinicInfo doctorClinicInfo=DoctorClinicInfo.find.byId(docClinicId);

		final ClinicBean bean=doctorClinicInfo.toBean();

		final Form<ClinicBean> filledForm = clinicForm.fill(doctorClinicInfo.toBean());

		//		Logger.info(doctorClinicInfo.toBean().daysOfWeek.size()+" "+doctorClinicInfo.toBean().daysOfWeekMr.size());

		return ok(views.html.doctor.newClinic.render(filledForm,bean.daysOfWeek,bean.daysOfWeekMr));

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

	//creating appointments
	public static  Result createAppointment(final DoctorClinicInfo docclinicInfo) {



		for (DaySchedule schedule : docclinicInfo.schedulDays) {
			Logger.info(schedule.fromTime.toString());
			Logger.info(schedule.toTime.toString());
			Logger.info(schedule.day.toString());

		}


		final Doctor doctor=LoginController.getLoggedInUser().getDoctor();

		final Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.set(Calendar.HOUR_OF_DAY,0);
		calendar.set(Calendar.MINUTE,0);
		calendar.set(Calendar.SECOND,0);
		calendar.set(Calendar.MILLISECOND,0);


		for(int date=0;date<31;date++){
			for (DaySchedule schedule : docclinicInfo.schedulDays) {

				Logger.info(schedule.day+ " "+ schedule.fromTime+" "+schedule.toTime);

				if(schedule.day == Day.getDay(calendar.get(Calendar.DAY_OF_WEEK)-1)){

					Logger.info(" "+"Entered");
					final int hourToClinic = schedule.toTime - schedule.fromTime;
					calendar.set(Calendar.HOUR_OF_DAY, schedule.fromTime);
					calendar.set(Calendar.MINUTE, 0);
					calendar.set(Calendar.SECOND,0);
					calendar.set(Calendar.MILLISECOND,0);
					if(schedule.requester.equals(Role.PATIENT)){
						for (int j2 = 0; j2 <((hourToClinic*60)/docclinicInfo.slot); j2++) {
							if(Appointment.find.where().eq("clinic",docclinicInfo.clinic).eq("appointmentTime", calendar.getTime()).findUnique()==null){
								Logger.info("  "+calendar.getTime());
								final Appointment appointment=new Appointment();
								appointment.appointmentStatus=AppointmentStatus.AVAILABLE;
								appointment.appointmentTime=calendar.getTime();
								appointment.clinic=docclinicInfo.clinic;
								appointment.doctor=doctor;
								appointment.save();
								calendar.add(Calendar.MINUTE, docclinicInfo.slot);
							}
							else
							{
								calendar.add(Calendar.MINUTE, docclinicInfo.slot);
							}
						}
					}else {
						for (int j2 = 0; j2 <((hourToClinic*60)/docclinicInfo.slotmr); j2++) {
							if(Appointment.find.where().eq("clinic",docclinicInfo.clinic).eq("appointmentTime", calendar.getTime()).findUnique()==null){
								Logger.info("  "+calendar.getTime());
								final Appointment appointment=new Appointment();
								appointment.appointmentStatus=AppointmentStatus.AVAILABLE;
								appointment.appointmentTime=calendar.getTime();
								appointment.clinic=docclinicInfo.clinic;
								appointment.doctor=doctor;
								appointment.save();
								calendar.add(Calendar.MINUTE,docclinicInfo.slotmr);
							}
							else
							{
								calendar.add(Calendar.MINUTE,docclinicInfo.slotmr);
							}
						}
					}

				}

			}
			Logger.info("***end of shedules");
			calendar.add(Calendar.DATE, 1);
		}
		Logger.info("***end of");
		return redirect(routes.DoctorController.myClinics());
	}


	// Re-Create Appointment
	public static Result reCreateAppointment(DoctorClinicInfo clinicInfo) {
		List<Appointment> appointments = Appointment.find.where()
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
			DaySchedule schedule=arrayList1.get(i);
			DaySchedule scheduleMr=arrayList2.get(i);
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

		return ok(views.html.doctor.doctor_appointments.render());
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


		return ok(views.html.doctor.viewTodaysAppointment.render(appointments));

	}

}
