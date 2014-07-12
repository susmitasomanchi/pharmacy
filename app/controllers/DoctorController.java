package controllers;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import models.Alert;
import models.AppUser;
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
import models.doctor.DoctorPublication;
import models.doctor.DoctorSocialWork;
import models.doctor.QuestionAndAnswer;
import play.Logger;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Http.MultipartFormData;
import play.mvc.Result;
import actions.BasicAuth;
import beans.ClinicBean;
import beans.DoctorDetailBean;
import beans.PatientBean;
import beans.QuestionAndAnswerBean;

import com.avaje.ebean.Ebean;
import com.google.common.io.Files;


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



	/**
	 * Action to update basic field of doctor like name, specialization, degree etc.
	 * POST	/doctor/doctor-basic-update
	 */
	public static Result basicUpdate(){
		final Map<String, String[]> requestMap = request().body().asFormUrlEncoded();
		try{
			final Doctor doctor = Doctor.find.byId(Long.parseLong(requestMap.get("doctorId")[0]));

			// Server side validation
			if(doctor.id.longValue() != LoginController.getLoggedInUser().getDoctor().id.longValue()){
				return redirect(routes.LoginController.processLogout());
			}

			if(requestMap.get("fullname") != null && !(requestMap.get("fullname")[0].trim().isEmpty())){
				doctor.appUser.name = requestMap.get("fullname")[0];
			}

			if(requestMap.get("specialization") != null && !(requestMap.get("specialization")[0].trim().isEmpty())){
				doctor.specialization = requestMap.get("specialization")[0];
			}

			if(requestMap.get("degree") != null && !(requestMap.get("degree")[0].trim().isEmpty())){
				doctor.degree = requestMap.get("degree")[0];
			}

			if(requestMap.get("description") != null && !(requestMap.get("description")[0].trim().isEmpty())){
				doctor.description = requestMap.get("description")[0];
			}

			doctor.appUser.update();
			doctor.update();
			return ok("0");
		}
		catch (final Exception e){
			Logger.error("ERROR WHILE UPDATING BASIC DOCTOR INFO");
			e.printStackTrace();
			return ok("-1");
		}
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
	 * Action to update profile and background images of Doctor
	 * POST	/doctor/doctor-images-update
	 */
	public static Result imagesUpdate(){
		final MultipartFormData formData = request().body().asMultipartFormData();
		final Doctor doctor = Doctor.find.byId(Long.parseLong(formData.asFormUrlEncoded().get("doctorId")[0]));
		// Server side validation
		if(doctor.id.longValue() != LoginController.getLoggedInUser().getDoctor().id.longValue()){
			return redirect(routes.LoginController.processLogout());
		}
		try{
			if(formData.getFile("profileimage") != null){
				final File profileImageFile = formData.getFile("profileimage").getFile();
				doctor.profileImage = Files.toByteArray(profileImageFile);
			}
			if(formData.getFile("backgroundimage") != null){
				final File backgroundImageFile = formData.getFile("backgroundimage").getFile();
				doctor.backgroundImage = Files.toByteArray(backgroundImageFile);
			}
			doctor.update();
		}
		catch(final Exception e){
			Logger.error("ERROR WHILE UPDATING IMAGES OF DOCTOR");
			e.printStackTrace();
		}
		return redirect(routes.UserActions.dashboard());
	}



	/**
	 * Action to create or update DoctorExperience entity for the loggedIn Doctor
	 * POST	/doctor/add-work-experience
	 */
	public static Result addWorkExperience(){

		final Map<String, String[]> requestMap = request().body().asFormUrlEncoded();
		final Doctor doctor = Doctor.find.byId(Long.parseLong(requestMap.get("doctorId")[0]));
		// Server side validation
		if(doctor.id.longValue() != LoginController.getLoggedInUser().getDoctor().id.longValue()){
			return redirect(routes.LoginController.processLogout());
		}

		try{
			final DoctorExperience experience = new DoctorExperience();
			experience.institutionName = requestMap.get("name")[0];
			experience.workedFrom = Integer.parseInt(requestMap.get("from")[0]);

			if(requestMap.get("position")[0] != null && requestMap.get("position")[0].compareToIgnoreCase("")!= 0){
				experience.position = requestMap.get("position")[0];
			}

			if(requestMap.get("to")[0] != null && requestMap.get("to")[0].compareToIgnoreCase("")!= 0){
				experience.workedTo = Integer.parseInt(requestMap.get("to")[0]);
			}

			if(requestMap.get("to")[0] != null && requestMap.get("to")[0].compareToIgnoreCase("")== 0){
				experience.workedTo = 0;
			}

			if(requestMap.get("description")[0] != null && requestMap.get("description")[0].compareToIgnoreCase("")!= 0){
				experience.description = requestMap.get("description")[0];
			}
			if(requestMap.get("doctorExperienceId") != null && requestMap.get("doctorExperienceId")[0].compareToIgnoreCase("")!= 0){
				experience.id = Long.parseLong(requestMap.get("doctorExperienceId")[0]);
				experience.update();
			}
			else{
				doctor.doctorExperienceList.add(experience);
				doctor.update();
			}
			return redirect(routes.UserActions.dashboard());
		}
		catch(final NumberFormatException e){
			Logger.error("NumberFormatException WHILE PARSING DOCTOR'S WORK EXPERIENCE");
			e.printStackTrace();
			flash().put("alert", new Alert("alert-danger", "Year field(s) invalid.").toString());
			return redirect(routes.UserActions.dashboard());
		}
		catch(final Exception e){
			Logger.error("ERROR WHILE SAVING DOCTOR'S WORK EXPERIENCE");
			e.printStackTrace();
			flash().put("alert", new Alert("alert-danger", "Sorry! Something went wrong. Please try again.").toString());
			return redirect(routes.UserActions.dashboard());
		}
	}


	/**
	 * Action to remove a DoctorExperience entity for the loggedIn Doctor
	 * GET /doctor/remove-work-experience/:docId/:id
	 */
	public static Result removeWorkExperience(final Long docId, final Long id){
		final Doctor doctor = Doctor.find.byId(docId);
		// Server side validation
		if(doctor.id.longValue() != LoginController.getLoggedInUser().getDoctor().id.longValue()){
			return redirect(routes.LoginController.processLogout());
		}

		final DoctorExperience experience = DoctorExperience.find.byId(id);
		doctor.doctorExperienceList.remove(experience);
		experience.delete();
		doctor.update();
		return redirect(routes.UserActions.dashboard());
	}



	/**
	 * Action to create / edit an Award entity of the loggedIn Doctor
	 * POST	/doctor/add-award
	 */
	public static Result addAward(){
		final Map<String, String[]> requestMap = request().body().asFormUrlEncoded();
		final Doctor doctor = Doctor.find.byId(Long.parseLong(requestMap.get("doctorId")[0]));
		// Server side validation
		if(doctor.id.longValue() != LoginController.getLoggedInUser().getDoctor().id.longValue()){
			return redirect(routes.LoginController.processLogout());
		}

		try{
			final DoctorAward award = new DoctorAward();

			if(requestMap.get("name")[0] != null && requestMap.get("name")[0].compareToIgnoreCase("")!= 0){
				award.awardName = requestMap.get("name")[0];
			}

			if(requestMap.get("by")[0] != null && requestMap.get("by")[0].compareToIgnoreCase("")!= 0){
				award.awardedBy = requestMap.get("by")[0];
			}

			if(requestMap.get("year")[0] != null && requestMap.get("year")[0].compareToIgnoreCase("")!= 0){
				award.year = requestMap.get("year")[0];
			}

			if(requestMap.get("description")[0] != null && requestMap.get("description")[0].compareToIgnoreCase("")!= 0){
				award.description = requestMap.get("description")[0];
			}

			if(requestMap.get("doctorAwardId") != null && requestMap.get("doctorAwardId")[0].compareToIgnoreCase("")!= 0){
				award.id = Long.parseLong(requestMap.get("doctorAwardId")[0]);
				award.update();
			}
			else{
				doctor.doctorAwardList.add(award);
				doctor.update();
			}
			return redirect(routes.UserActions.dashboard());
		}
		catch(final Exception e){
			Logger.error("ERROR WHILE SAVING DOCTOR'S AWARD");
			e.printStackTrace();
			flash().put("alert", new Alert("alert-danger", "Sorry! Something went wrong. Please try again.").toString());
			return redirect(routes.UserActions.dashboard());
		}
	}


	/**
	 * Action to remove a Award entity from the loggedIn Doctor
	 * GET	/doctor/remove-award/:docId/:id
	 */
	public static Result removeAward(final Long docId, final Long id){
		final Doctor doctor = Doctor.find.byId(docId);
		// Server side validation
		if(doctor.id.longValue() != LoginController.getLoggedInUser().getDoctor().id.longValue()){
			return redirect(routes.LoginController.processLogout());
		}

		final DoctorAward award = DoctorAward.find.byId(id);
		doctor.doctorAwardList.remove(award);
		award.delete();
		doctor.update();
		return redirect(routes.UserActions.dashboard());
	}



	/**
	 * Action to create new/update DoctorEducation entity for the loggedIn Doctor
	 * POST	/doctor/add-education
	 */
	public static Result addEducation(){
		final Map<String, String[]> requestMap = request().body().asFormUrlEncoded();
		final Doctor doctor = Doctor.find.byId(Long.parseLong(requestMap.get("doctorId")[0]));
		// Server side validation
		if(doctor.id.longValue() != LoginController.getLoggedInUser().getDoctor().id.longValue()){
			return redirect(routes.LoginController.processLogout());
		}

		try{
			final DoctorEducation education = new DoctorEducation();
			education.institutionName = requestMap.get("name")[0];
			education.fromYear = Integer.parseInt(requestMap.get("from")[0]);

			if(requestMap.get("degree")[0] != null && requestMap.get("degree")[0].compareToIgnoreCase("")!= 0){
				education.degree = requestMap.get("degree")[0];
			}

			if(requestMap.get("to")[0] != null && requestMap.get("to")[0].compareToIgnoreCase("")!= 0){
				education.toYear = Integer.parseInt(requestMap.get("to")[0]);
			}
			if(requestMap.get("to")[0] != null && requestMap.get("to")[0].compareToIgnoreCase("")== 0){
				education.toYear = 0;
			}
			if(requestMap.get("description")[0] != null && requestMap.get("description")[0].compareToIgnoreCase("")!= 0){
				education.description = requestMap.get("description")[0];
			}

			if(requestMap.get("doctorEducationId") != null && requestMap.get("doctorEducationId")[0].compareToIgnoreCase("")!= 0){
				education.id = Long.parseLong(requestMap.get("doctorEducationId")[0]);
				education.update();
			}
			else{
				doctor.doctorEducationList.add(education);
				doctor.update();
			}

			return redirect(routes.UserActions.dashboard());
		}
		catch(final NumberFormatException e){
			Logger.error("NumberFormatException WHILE PARSING DOCTOR'S EDUCATION");
			e.printStackTrace();
			flash().put("alert", new Alert("alert-danger", "Year field(s) invalid.").toString());
			return redirect(routes.UserActions.dashboard());
		}
		catch(final Exception e){
			Logger.error("ERROR WHILE SAVING DOCTOR'S EDUCATION");
			e.printStackTrace();
			flash().put("alert", new Alert("alert-danger", "Sorry! Something went wrong. Please try again.").toString());
			return redirect(routes.UserActions.dashboard());
		}
	}


	/**
	 * Action to remove a DoctorEducation entity from the loggedIn Doctor
	 * GET	/doctor/remove-education/:docId/:id
	 */
	public static Result removeEducation(final Long docId, final Long id){
		final Doctor doctor = Doctor.find.byId(docId);
		// Server side validation
		if(doctor.id.longValue() != LoginController.getLoggedInUser().getDoctor().id.longValue()){
			return redirect(routes.LoginController.processLogout());
		}

		final DoctorEducation education = DoctorEducation.find.byId(id);
		doctor.doctorEducationList.remove(education);
		education.delete();
		doctor.update();
		return redirect(routes.UserActions.dashboard());
	}



	/**
	 * Action to create/update a SocialWork entity for the loggedIn Doctor
	 * POST	/doctor/add-social-work
	 */
	public static Result addSocialWork(){
		final Map<String, String[]> requestMap = request().body().asFormUrlEncoded();
		final Doctor doctor = Doctor.find.byId(Long.parseLong(requestMap.get("doctorId")[0]));
		// Server side validation
		if(doctor.id.longValue() != LoginController.getLoggedInUser().getDoctor().id.longValue()){
			return redirect(routes.LoginController.processLogout());
		}

		try{
			final DoctorSocialWork socialWork = new DoctorSocialWork();
			socialWork.title = requestMap.get("title")[0];
			socialWork.description = requestMap.get("description")[0];

			if(requestMap.get("doctorSocialId") != null && requestMap.get("doctorSocialId")[0].compareToIgnoreCase("")!= 0){
				socialWork.id = Long.parseLong(requestMap.get("doctorSocialId")[0]);
				socialWork.update();
			}
			else{
				doctor.getDoctorSocialWorkList().add(socialWork);
				doctor.update();
			}

			return redirect(routes.UserActions.dashboard());
		}
		catch(final Exception e){
			Logger.error("ERROR WHILE SAVING DOCTOR'S SOCIAL WORK");
			e.printStackTrace();
			flash().put("alert", new Alert("alert-danger", "Sorry! Something went wrong. Please try again.").toString());
			return redirect(routes.UserActions.dashboard());
		}
	}


	/**
	 * Action to remove a SocialWork entity from the loggedIn Doctor
	 * GET	/doctor/remove-social-work/:docId/:id
	 */
	public static Result removeSocialWork(final Long docId, final Long id){
		final Doctor doctor = Doctor.find.byId(docId);
		// Server side validation
		if(doctor.id.longValue() != LoginController.getLoggedInUser().getDoctor().id.longValue()){
			return redirect(routes.LoginController.processLogout());
		}

		final DoctorSocialWork socialWork = DoctorSocialWork.find.byId(id);
		doctor.doctorSocialWorkList.remove(socialWork);
		socialWork.delete();
		doctor.update();
		return redirect(routes.UserActions.dashboard());
	}





















	//Process new clinic timing Data
	public static Result processNewClinic(){

		final Form<ClinicBean> filledForm = clinicForm.bindFromRequest();
		if(filledForm.hasErrors()){

			return ok(views.html.doctor.newClinic.render(clinicForm));

		}
		else{
			final DoctorClinicInfo clinicInfo =filledForm.get().toDoctorClinicInfo();


			clinicInfo.doctor=LoginController.getLoggedInUser().getDoctor();
			clinicInfo.save();
			//Logger.info(""+clinicInfo.lat+" "+clinicInfo.lng);
			return DoctorController.createAppointment(clinicInfo);

		}
	}

	public static Result processUpdateClinicInfo() {
		final Form<ClinicBean> filledForm = clinicForm.bindFromRequest();

		if(filledForm.hasErrors()){

			return ok(views.html.doctor.editClinic.render(clinicForm,new ArrayList<String>(),new ArrayList<String>()));

		}
		else{
			final DoctorClinicInfo clinicInfo =filledForm.get().toDoctorClinicInfo();

			final DoctorClinicInfo clinicInfoPrevious=DoctorClinicInfo.find.byId(clinicInfo.id);

			clinicInfoPrevious.clinic.name = clinicInfo.clinic.name;
			clinicInfoPrevious.clinic.contactNo=clinicInfo.clinic.contactNo;
			clinicInfoPrevious.clinic.contactPersonName=clinicInfo.clinic.contactPersonName;
			clinicInfoPrevious.clinic.update();
			Logger.info(""+clinicInfo.address.addrressLine1);
			clinicInfoPrevious.address.addrressLine1=clinicInfo.address.addrressLine1;
			clinicInfoPrevious.address.addrressLine2=clinicInfo.address.addrressLine2;
			clinicInfoPrevious.address.state=clinicInfo.address.state;
			clinicInfoPrevious.address.city=clinicInfo.address.city;
			clinicInfoPrevious.address.latitude=clinicInfo.address.latitude;
			clinicInfoPrevious.address.longitude=clinicInfo.address.longitude;
			clinicInfoPrevious.address.update();
			clinicInfoPrevious.update();

			flash().put("alert", new Alert("alert-success","Successfully Updated").toString());
			return redirect(routes.DoctorController.myClinics());


		}
	}

	public static Result processUpdateClinicSchedule() {
		final Form<ClinicBean> filledForm = clinicForm.bindFromRequest();
		if(filledForm.hasErrors()){

			return ok(views.html.doctor.editClinic.render(clinicForm,new ArrayList<String>(),new ArrayList<String>()));

		}
		else{
			Logger.warn("enterd in method");

			final DoctorClinicInfo clinicInfo =filledForm.get().toDoctorClinicInfo();

			final DoctorClinicInfo clinicInfoPrevious=DoctorClinicInfo.find.byId(clinicInfo.id);
			for (final DaySchedule sc4: clinicInfo.schedulDays) {
				Logger.info("test day"+sc4.day);
			}
			Ebean.delete(clinicInfoPrevious.schedulDays);
			clinicInfoPrevious.schedulDays=clinicInfo.schedulDays;
			clinicInfoPrevious.slot=clinicInfo.slot;
			clinicInfoPrevious.slotmr=clinicInfo.slotmr;

			clinicInfoPrevious.update();
			flash().put("alert", new Alert("alert-success","Successfully Updated").toString());
			return DoctorController.reCreateAppointment(clinicInfoPrevious);

		}

	}
	//Deleting Clinic
	public static Result deleteClinic(final Long id) {

		final DoctorClinicInfo clinicInfo=DoctorClinicInfo.find.byId(id);
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
		 * Do whatever with regarding appointments
		 *
		 */


		Ebean.delete(approvedAppts);

		final List<Appointment> availableAppts=Appointment.find.where()
				.eq("doctor", clinicInfo.doctor)
				.eq("clinic", clinicInfo.clinic)
				.eq("appointmentStatus", AppointmentStatus.AVAILABLE).findList();

		Ebean.delete(availableAppts);

		clinicInfo.active=false;
		clinicInfo.doctor.update();
		clinicInfo.update();

		flash().put("alert", new Alert("alert-success","Successfully Deleted").toString());

		return redirect(routes.DoctorController.myClinics());

	}

	public static Result myClinics(){
		final Doctor loggedInDoctor = LoginController.getLoggedInUser().getDoctor();
		return ok(views.html.doctor.myClinics.render(loggedInDoctor.getActiveClinic()));

	}




	//Edit Or Manage Clinic
	public static Result manageClinic(final Long docClinicId) {
		final DoctorClinicInfo doctorClinicInfo=DoctorClinicInfo.find.byId(docClinicId);
		final ClinicBean bean=doctorClinicInfo.toBean();
		final Form<ClinicBean> filledForm = clinicForm.fill(doctorClinicInfo.toBean());
		for (final String	 from : bean.fromHrs) {
			Logger.warn(from);
		}
		return ok(views.html.doctor.editClinic.render(filledForm,bean.daysOfWeek,bean.daysOfWeekMr));
	}


	public static Result displayAnswer(){
		final AppUser user = LoginController.getLoggedInUser();
		final Doctor doctor=user.getDoctor();
		List<QuestionAndAnswer> qaList=new ArrayList<QuestionAndAnswer>();
		if(doctor!=null){
			qaList = QuestionAndAnswer.find.where().eq("answerBy.id", doctor.id).findList();
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

		final Calendar calendar1=Calendar.getInstance();
		final Calendar calendar2=Calendar.getInstance();
		final SimpleDateFormat dateFormat=new SimpleDateFormat("kk:mm");
		final Doctor doctor=LoginController.getLoggedInUser().getDoctor();
		final Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		for(int date=0;date<31;date++){
			for (final DaySchedule schedule : docclinicInfo.schedulDays) {
				if(schedule.day == Day.getDay(calendar.get(Calendar.DAY_OF_WEEK)-1)){
					Logger.info(" "+"Entered");
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
						for (int j2 = 0; j2 <(((hoursToClinic*60)+minutsToClinic)/docclinicInfo.slot); j2++) {
							if(Appointment.find.where().eq("doctor",doctor).eq("clinic",docclinicInfo.clinic).eq("appointmentTime", calendar.getTime()).findUnique()==null){
								Logger.info("  "+calendar.getTime());
								final Appointment appointment=new Appointment();
								appointment.appointmentStatus=AppointmentStatus.AVAILABLE;
								appointment.appointmentTime=calendar.getTime();
								appointment.clinic=docclinicInfo.clinic;
								appointment.doctor=doctor;
								appointment.save();
								calendar.add(Calendar.MINUTE, docclinicInfo.slot);
							}
							else{
								calendar.add(Calendar.MINUTE, docclinicInfo.slot);
							}
						}
					}else {
						for (int j2 = 0; j2 <(((hoursToClinic*60)+minutsToClinic)/docclinicInfo.slotmr); j2++) {
							if(Appointment.find.where().eq("doctor",doctor).eq("clinic",docclinicInfo.clinic).eq("appointmentTime", calendar.getTime()).findUnique()==null){
								Logger.info("  "+calendar.getTime());
								final Appointment appointment=new Appointment();
								appointment.appointmentStatus=AppointmentStatus.AVAILABLE;
								appointment.appointmentTime=calendar.getTime();
								appointment.clinic=docclinicInfo.clinic;
								appointment.doctor=doctor;
								appointment.save();
								calendar.add(Calendar.MINUTE,docclinicInfo.slotmr);
							}
							else{
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
	public static Result reCreateAppointment(final DoctorClinicInfo clinicInfo) {
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

		final Doctor loggedIndoctor = LoginController.getLoggedInUser().getDoctor();

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
