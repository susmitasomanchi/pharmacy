package controllers;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import models.AppUser;
import models.Appointment;
import models.AppointmentStatus;
import models.AppointmentType;
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
		// Logger.info("enteredt");

		if (filledForm.hasErrors()) {
			Logger.info("bad request");
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
		// Logger.info("enteredt");

		if (educationfilledForm.hasErrors()) {
			Logger.info("bad request");
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
		// Logger.info("enteredt");

		if (awardfilledForm.hasErrors()) {
			Logger.info("bad request");
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
		final DoctorClinicInfo dcInfo = new DoctorClinicInfo();

		if(filledForm.hasErrors()){
			Logger.info(filledForm.errors().toString());
			return ok(views.html.doctor.newClinic.render(filledForm));
		}
		else{
			final Clinic clinic = filledForm.get().toEntity();
			clinic.save();
			final Doctor loggedInDoctor = LoginController.getLoggedInUser().getDoctor();
			dcInfo.doctor = loggedInDoctor;
			dcInfo.clinic = clinic;
			dcInfo.fromHrs = filledForm.get().fromHrs;
			dcInfo.toHrs = filledForm.get().toHrs;
			dcInfo.daysOfWeek=filledForm.get().toDayOfTheWeek();
			dcInfo.save();
			loggedInDoctor.doctorClinicInfoList.add(dcInfo);
			loggedInDoctor.update();

		}
		return DoctorController.createAppointment(dcInfo);
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
		//Logger.info("enteredt");

		if(filledForm.hasErrors()) {
			Logger.info("bad request");

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



	//register patient by doctor

	public static Result registerPatient(){

		return ok(views.html.registerPatient.render(patientForm));
	}


	public static Result registerPatientProccess(){

		final Form<PatientBean> filledForm=patientForm.bindFromRequest();

		if(filledForm.hasErrors()){
			Logger.info("*******Bad Request ************");
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
	public static  Result createAppointment(DoctorClinicInfo clinicInfo) {

		final Doctor doctor=LoginController.getLoggedInUser().getDoctor();
		final List<DoctorClinicInfo> clinicInfos=doctor.doctorClinicInfoList;

		final Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.set(Calendar.HOUR_OF_DAY,0);
		calendar.set(Calendar.MINUTE,0);
		calendar.set(Calendar.SECOND,0);
		calendar.set(Calendar.MILLISECOND,0);

		List<Integer> days2= new ArrayList<Integer>();
		for (DayOfTheWeek dayOfTheWeek : clinicInfo.daysOfWeek) {
			days2.add(dayOfTheWeek.day.ordinal());
		}
		final int hourToClinic=clinicInfo.toHrs-clinicInfo.fromHrs;


		for(int days=0;days<30;days++){


			calendar.set(Calendar.HOUR_OF_DAY, clinicInfo.fromHrs);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND,0);
			calendar.set(Calendar.MILLISECOND,0);
			Logger.info(days2+"");


			for (int j2 = 0; j2 <((hourToClinic*60)/5); j2++) {

				if (days2.contains(calendar.get(Calendar.DAY_OF_WEEK)-1)) {
					final Appointment appointment=new Appointment();
					appointment.appointmentStatus=AppointmentStatus.AVAILABLE;
					appointment.appointmentTime=calendar.getTime();
					appointment.clinic=clinicInfo.clinic;
					appointment.doctor=doctor;
					appointment.appointmentType=AppointmentType.NORMAL;
					appointment.save();
					calendar.add(Calendar.MINUTE, 5);
				}

			}

			calendar.add(Calendar.DATE, 1);

		}

		return redirect(routes.DoctorController.myClinics());
	}
	public static  Result createMrSchedule() {
		return ok(views.html.doctor.setMrAppointment.render(doctorClinicForm));
	}
	public static Result createMrAppointment() {

		final Form<DoctorClinicInfo> filledForm = doctorClinicForm.bindFromRequest();
		if(filledForm.hasErrors()){
			return ok(views.html.doctor.setMrAppointment.render(doctorClinicForm));
		}
		else{
			final DoctorClinicInfo clinicInfo = filledForm.get();
			Calendar calendar=Calendar.getInstance();
			calendar.setTime(new Date());
			calendar.set(Calendar.HOUR_OF_DAY, clinicInfo.fromHrs);
			calendar.set(Calendar.MINUTE,0);
			calendar.set(Calendar.SECOND,0);
			calendar.set(Calendar.MILLISECOND,0);


			Calendar calendar2=Calendar.getInstance();
			calendar2.setTime(new Date());
			calendar2.set(Calendar.HOUR_OF_DAY, clinicInfo.fromHrs);
			calendar2.set(Calendar.MINUTE,29);
			calendar2.set(Calendar.SECOND,59);
			calendar2.set(Calendar.MILLISECOND,59);

			List<Appointment> appointments=Appointment.find.where().between("appointmentTime", calendar.getTime(), calendar2.getTime()).findList();
			for (Appointment appointment : appointments) {
				appointment.delete();
			}
			Appointment appointment=new Appointment();
			appointment.appointmentStatus=AppointmentStatus.AVAILABLE;
			appointment.appointmentTime=calendar.getTime();
			appointment.appointmentType=AppointmentType.SPECIAL;
			appointment.clinic=clinicInfo.clinic;
			appointment.doctor=loggedIndoctor;
			appointment.save();
		}
		//return redirect(routes.DoctorController.myClinics());

		return ok("Created Mr appointment");

	}

}
