package controllers;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import models.DoctorPublication;
import models.LanguageAppUser;
import models.AppUser;
import models.Appointment;
import models.AppointmentStatus;
import models.AppointmentType;
import models.Clinic;
import models.DayOfTheWeek;
import models.Doctor;
import models.DoctorAward;
import models.DoctorClinicInfo;
import models.DoctorLanguage;
import models.DoctorEducation;
import models.DoctorExperience;
import models.DoctorSocialWork;
import models.Patient;
import models.Product;
import models.QuestionAndAnswer;
import play.Logger;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import play.mvc.Result;
import actions.BasicAuth;
import beans.ClinicBean;
import beans.PatientBean;
import beans.QuestionAndAnswerBean;
import beans.DoctorDetailBean;;

@BasicAuth
public class DoctorController extends Controller {

	public static Form<Doctor> form = Form.form(Doctor.class);
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
		return ok(views.html.doctor.doctorDashboard.render(loggedIndoctor));
	}



	public static Result newClinic(){
		return ok(views.html.doctor.newClinic.render(clinicForm));
	}


	public static Result doctorExperience(){
		Logger.info(loggedIndoctor.doctorExperienceList.size()+" size of list");
		return ok(views.html.doctor.doctorExperience.render(experienceForm));
	}

	public static Result processDoctorExperience() {
		final Form<DoctorExperience> experienceFilledForm = experienceForm.bindFromRequest();
		// Logger.info("enteredt");

		if (experienceFilledForm.hasErrors()) {
			Logger.info("bad request");
			// System.out.println(filledForm.errors());
			return badRequest(views.html.doctor.doctorExperience.render(experienceFilledForm));
		} else {
			
				DoctorExperience doctorExperience=experienceFilledForm.get();
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
			Logger.info("bad request");

			return badRequest(views.html.doctor.doctorEducation.render(educationfilledForm));
		} else {
			
			DoctorEducation doctorEducation=educationfilledForm.get();
			
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
			DoctorPublication doctorPublication=publicationfilledForm.get();
			
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
		// Logger.info("enteredt");

		if (awardfilledForm.hasErrors()) {
			Logger.info("bad request");
			// System.out.println(filledForm.errors());
			return badRequest(views.html.doctor.doctorAward.render(awardfilledForm));
		} else {
			DoctorAward doctorAward=awardfilledForm.get();
			
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
		Doctor doctor=LoginController.getLoggedInUser().getDoctor();
		
		// Logger.info("enteredt");
		if (languagefilledForm.hasErrors()) {
			Logger.info("bad request");
			// System.out.println(filledForm.errors());
			return badRequest(views.html.doctor.doctorLanguage.render(languagefilledForm));
		} else {
			DoctorLanguage docLan=new DoctorLanguage();
			
			
			 List<LanguageAppUser> doctorLanguage = languagefilledForm.get().toLanguageAppUser();
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
			DoctorSocialWork doctorSocialWork=socialWorkfilledForm.get();
			
			loggedIndoctor.doctorSocialWorkList.add(doctorSocialWork);
			
			loggedIndoctor.update();
			
		}
		
			 return redirect(routes.DoctorController.doctorProfile());

	}

	public static Result processNewClinic(){
		final Form<ClinicBean> filledForm = clinicForm.bindFromRequest();
		if(filledForm.hasErrors()){
			Logger.info(filledForm.errors().toString());
			return ok(views.html.doctor.newClinic.render(filledForm));
		}
		else{
			final Clinic clinic = filledForm.get().toEntity();
			clinic.save();
			final Doctor loggedInDoctor = LoginController.getLoggedInUser().getDoctor();
			final DoctorClinicInfo dcInfo = new DoctorClinicInfo();
			dcInfo.doctor = loggedInDoctor;
			dcInfo.clinic = clinic;
			dcInfo.fromHrs = filledForm.get().fromHrs;
			dcInfo.toHrs = filledForm.get().toHrs;
			dcInfo.daysOfWeek=filledForm.get().toDayOfTheWeek();
			dcInfo.save();
			loggedInDoctor.doctorClinicInfoList.add(dcInfo);
			loggedInDoctor.update();

		}
		return redirect(routes.DoctorController.myClinics());
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
	public static  Result createAppointment() {

		final Doctor doctor=LoginController.getLoggedInUser().getDoctor();
		final List<DoctorClinicInfo> clinicInfos=doctor.doctorClinicInfoList;

		final Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.set(Calendar.HOUR_OF_DAY,0);
		calendar.set(Calendar.MINUTE,0);
		calendar.set(Calendar.SECOND,0);
		calendar.set(Calendar.MILLISECOND,0);

		for(int days=0;days<30;days++){

			for (final DoctorClinicInfo doctorClinicInfo : clinicInfos) {

				List<Integer> days2= new ArrayList<Integer>();
				for (DayOfTheWeek dayOfTheWeek : doctorClinicInfo.daysOfWeek) {
					days2.add(dayOfTheWeek.day.ordinal());
				}
				Logger.info(days2+"");

				final int hourToClinic=doctorClinicInfo.toHrs-doctorClinicInfo.fromHrs;
				calendar.set(Calendar.HOUR_OF_DAY, doctorClinicInfo.fromHrs);
				calendar.set(Calendar.MINUTE, 0);
				calendar.set(Calendar.SECOND,0);
				calendar.set(Calendar.MILLISECOND,0);
				for (int j2 = 0; j2 <((hourToClinic*60)/5); j2++) {

					if (days2.contains(calendar.get(Calendar.DAY_OF_WEEK)-1)) {
						final Appointment appointment=new Appointment();
						appointment.appointmentStatus=AppointmentStatus.AVAILABLE;
						appointment.appointmentTime=calendar.getTime();
						appointment.clinic=doctorClinicInfo.clinic;
						appointment.doctor=doctor;
						appointment.appointmentType=AppointmentType.NORMAL;
						appointment.save();
						calendar.add(Calendar.MINUTE, 5);
					}

				}
			}


			calendar.add(Calendar.DATE, 1);

		}

		return ok("doctor time scheduled");
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
