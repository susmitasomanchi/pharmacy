package controllers;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import models.AppUser;
import models.Appointment;
import models.AppointmentStatus;
import models.Clinic;
import models.DayOfTheWeek;
import models.Doctor;
import models.DoctorAward;
import models.DoctorClinicInfo;
import models.DoctorEducation;
import models.DoctorExperience;
import models.Patient;
import models.QuestionAndAnswer;
import play.Logger;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import actions.BasicAuth;
import beans.ClinicBean;
import beans.PatientBean;
import beans.QuestionAndAnswerBean;


@BasicAuth
public class DoctorController extends Controller {

	public static Form<Doctor> form = Form.form(Doctor.class);
	public static Form<PatientBean> patientForm = Form.form(PatientBean.class);
	public static Form<ClinicBean> clinicForm = Form.form(ClinicBean.class);
	public static Form<DoctorExperience> experienceForm = Form.form(DoctorExperience.class);
	public static Form<DoctorEducation> educationForm = Form.form(DoctorEducation.class);
	public static Form<DoctorAward> awardForm = Form.form(DoctorAward.class);
	public static Form<QuestionAndAnswerBean> questionAndAnswerForm = Form.form(QuestionAndAnswerBean.class);
	public static Form<DoctorClinicInfo> doctorClinicForm	=Form.form(DoctorClinicInfo.class);

	public static Doctor loggedIndoctor=LoginController.getLoggedInUser().getDoctor();

	public static Result requestAppointment(){


		String param[] =request().body().asFormUrlEncoded().get("datetime");
		try{
			Appointment appointment=Appointment.find.byId(Long.parseLong(param[1]));
			appointment.remarks=param[0];
			appointment.requestedBy=LoginController.getLoggedInUser();
			appointment.appointmentStatus=AppointmentStatus.APPROVED;
			appointment.update();
			return ok("0");
		}
		catch(Exception e){
			e.printStackTrace();
			return ok("-1");
		}


	}


	public static Result doctorProfile(){
		return ok(views.html.doctor.doctorDashboard.render("hello"));
	}



	public static Result newClinic(){
		return ok(views.html.doctor.newClinic.render(clinicForm));
	}


	public static Result doctorExperience(){
		return ok(views.html.doctor.doctorExperience.render(experienceForm));
	}

	public static Result processDoctorExperience() {
		final Form<DoctorExperience> filledForm = experienceForm.bindFromRequest();

		if (filledForm.hasErrors()) {
			// System.out.println(filledForm.errors());
			return badRequest(views.html.doctor.doctorExperience.render(filledForm));
		} else {
			final DoctorExperience doctorExperience = filledForm.get();

			if (doctorExperience.id == null) {
				doctorExperience.save();
			}
			else {

				doctorExperience.update();
			}
		}
		// return ok(views.html.scheduleAppointment.render("hello"));
		// return redirect(routes.UserController.list());
		return TODO;

	}


	public static Result doctorEducation(){
		return ok(views.html.doctor.doctorEducation.render(educationForm));
	}

	public static Result processDoctorEducation() {
		final Form<DoctorEducation> educationfilledForm = educationForm.bindFromRequest();

		if (educationfilledForm.hasErrors()) {
			// System.out.println(filledForm.errors());
			return badRequest(views.html.doctor.doctorEducation.render(educationfilledForm));
		} else {
			final DoctorEducation doctorEducation = educationfilledForm.get();

			if (doctorEducation.id == null) {


				doctorEducation.save();
			}
			else {

				doctorEducation.update();
			}
		}
		// return ok(views.html.scheduleAppointment.render("hello"));
		// return redirect(routes.UserController.list());
		return TODO;

	}

	public static Result doctorAward(){
		return ok(views.html.doctor.doctorAward.render(awardForm));
	}

	public static Result processDoctorAward() {
		final Form<DoctorAward> awardfilledForm = awardForm.bindFromRequest();

		if (awardfilledForm.hasErrors()) {
			// System.out.println(filledForm.errors());
			return badRequest(views.html.doctor.doctorAward.render(awardfilledForm));
		} else {
			final DoctorAward doctorAward = awardfilledForm.get();

			if (doctorAward.id == null) {


				doctorAward.save();
			}
			else {

				doctorAward.update();
			}
		}
		// return ok(views.html.scheduleAppointment.render("hello"));
		// return redirect(routes.UserController.list());
		return TODO;

	}

	public static Result processNewClinic(){
		final Form<ClinicBean> filledForm = clinicForm.bindFromRequest();
		DoctorClinicInfo doctorClinicInfo=null;
		if(filledForm.hasErrors()){
			return ok(views.html.doctor.newClinic.render(filledForm));
		}
		else{

			final Clinic clinic = filledForm.get().toEntity();
			clinic.save();
			final Doctor loggedInDoctor = LoginController.getLoggedInUser().getDoctor();

			doctorClinicInfo=filledForm.get().toDoctorClinicInfoList();

			if (doctorClinicInfo.id!=null) {
				return ok();
			}else{
				doctorClinicInfo.clinic=clinic;
				doctorClinicInfo.doctor=loggedInDoctor;
				doctorClinicInfo.save();
				loggedInDoctor.doctorClinicInfoList.add(doctorClinicInfo);
				loggedInDoctor.update();
				return DoctorController.createAppointment(doctorClinicInfo);
			}



		}

	}

	public static Result myClinics(){
		final Doctor loggedInDoctor = LoginController.getLoggedInUser().getDoctor();
		return ok(views.html.doctor.myClinics.render(loggedInDoctor.doctorClinicInfoList));
	}


	public static Result newAssistant(){
		return ok(views.html.doctor.newAssistant.render());
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


	//Edit Or Manage Clinic
	public static Result manageClinic(Long docClinicId) {

		DoctorClinicInfo doctorClinicInfo=DoctorClinicInfo.find.byId(docClinicId);

		Logger.info(""+doctorClinicInfo.toBean().name);

		Form<ClinicBean> filledForm = clinicForm.fill(doctorClinicInfo.toBean());


		return ok(views.html.doctor.newClinic.render(filledForm));

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
	public static  Result createAppointment(DoctorClinicInfo docclinicInfo) {

		final Doctor doctor=LoginController.getLoggedInUser().getDoctor();

		final Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.set(Calendar.HOUR_OF_DAY,0);
		calendar.set(Calendar.MINUTE,0);
		calendar.set(Calendar.SECOND,0);
		calendar.set(Calendar.MILLISECOND,0);

		List<Integer> daysMr= new ArrayList<Integer>();
		List<Integer> days= new ArrayList<Integer>();

		for (DayOfTheWeek dayOfTheWeek : docclinicInfo.daysOfWeek) {
			Logger.info(""+dayOfTheWeek.day);
			days.add(dayOfTheWeek.day.ordinal());
		}

		for (DayOfTheWeek dayOfTheWeek : docclinicInfo.daysOfWeekMr) {
			Logger.info(""+dayOfTheWeek.day);
			daysMr.add(dayOfTheWeek.day.ordinal());
		}
		Logger.info(""+days);

		Logger.info(""+daysMr);

		for(int date=0;date<8;date++){



			if (days.contains(calendar.get(Calendar.DAY_OF_WEEK)-1)) {

				int hourToClinic=docclinicInfo.toHrs-docclinicInfo.fromHrs;

				calendar.set(Calendar.HOUR_OF_DAY, docclinicInfo.fromHrs);
				calendar.set(Calendar.MINUTE, 0);
				calendar.set(Calendar.SECOND,0);
				calendar.set(Calendar.MILLISECOND,0);

				for (int j2 = 0; j2 <((hourToClinic*60)/5); j2++) {
					Logger.info(calendar.getTime()+"testpatient");

					final Appointment appointment=new Appointment();
					appointment.appointmentStatus=AppointmentStatus.AVAILABLE;
					appointment.appointmentTime=calendar.getTime();
					appointment.clinic=docclinicInfo.clinic;
					appointment.doctor=doctor;
					appointment.save();
					calendar.add(Calendar.MINUTE, 5);
				}

			}


			if (daysMr.contains(calendar.get(Calendar.DAY_OF_WEEK)-1)) {

				int hourToClinicMr=docclinicInfo.toHrsMr-docclinicInfo.fromHrsMr;

				calendar.set(Calendar.HOUR_OF_DAY, docclinicInfo.fromHrsMr);
				calendar.set(Calendar.MINUTE, 0);
				calendar.set(Calendar.SECOND,0);
				calendar.set(Calendar.MILLISECOND,0);

				Logger.info(hourToClinicMr+"  "+(calendar.get(Calendar.DAY_OF_WEEK)-1));
				for (int j2 = 0; j2 <((hourToClinicMr*60)/5); j2++) {
					Logger.info(calendar.getTime()+"test");

					if(Appointment.find.where().eq("appointmentTime", calendar.getTime()).findList().size()==0){
						final Appointment appointment=new Appointment();
						appointment.appointmentStatus=AppointmentStatus.AVAILABLE;
						appointment.appointmentTime=calendar.getTime();
						appointment.clinic=docclinicInfo.clinic;
						appointment.doctor=doctor;
						appointment.save();
						calendar.add(Calendar.MINUTE, 5);
					}else{
						calendar.add(Calendar.MINUTE, 5);
					}
				}


			}

			calendar.add(Calendar.DATE, 1);

		}

		return redirect(routes.DoctorController.myClinics());
	}

	//Todays Appointment
	public static Result viewTodaysAppointment() {


		Calendar calendar=Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND,0);
		calendar.set(Calendar.MILLISECOND,0);


		List<Appointment> appointments=Appointment.find.where().eq("appointmentStatus", AppointmentStatus.APPROVED).eq("doctor", loggedIndoctor).ge("appointmentTime", calendar.getTime()).findList();



		return ok(views.html.doctor.viewTodaysAppointment.render(appointments));

	}


}
