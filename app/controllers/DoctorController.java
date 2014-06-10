package controllers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import models.AppUser;
import models.Clinic;
import models.Doctor;
import models.DoctorSchedule;
import models.Patient;
import models.QuestionAndAnswer;
import play.Logger;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import beans.PatientBean;
import beans.QuestionAndAnswerBean;

public class DoctorController extends Controller {

	public static Form<Doctor> form = Form.form(Doctor.class);
	public static Form<PatientBean> patientForm = Form.form(PatientBean.class);
	public static Form<QuestionAndAnswerBean> questionAndAnswerForm = Form
			.form(QuestionAndAnswerBean.class);

	public static Form<DoctorSchedule> docScheduleForm=Form.form(DoctorSchedule.class);
	
	public static Result requestAppointment(){
		final String datetime = request().body().asFormUrlEncoded().get("datetime")[0];
		final Long doctorId = Long.parseLong(request().body().asFormUrlEncoded().get("doctorId")[0]);
		final Doctor doctor = Doctor.find.byId(doctorId);
		final SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy~HH:mm");
		try {
			final Date date = sdf.parse(datetime);
			Logger.info("Date extracted: "+date);
			return ok("Date extracted: "+date+" Doctor: "+doctor.appUser.name);
		} catch (final ParseException e) {
			e.printStackTrace();
		}
		return ok();
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

	//doctor Action
	public static Result docStuff(){
		return ok(views.html.doctorStuff.render());
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
		Doctor doctor=user.getDoctor();
		List<QuestionAndAnswer> qaList=new ArrayList<QuestionAndAnswer>();
		if(doctor!=null){
			qaList = QuestionAndAnswer.find.where()
					.eq("answerBy.id", doctor.id).findList();
		}
		return ok(views.html.ansQuestion.render(qaList));

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
	
	//doctor schedule
		public static Result doctorSchedule(){
			final List<Clinic> clinicList=new ArrayList<Clinic>();
			return ok(views.html.doctorSchedule.render(docScheduleForm,clinicList));
		}

		//schedule proccess
		public static Result scheduleProccess(){
			final Form<DoctorSchedule> filledForm = docScheduleForm.bindFromRequest();
			//Logger.info("enteredt");

			if(filledForm.hasErrors()) {
				Logger.info("bad request");

				return ok();//badRequest(views.html.doctorSchedule.render(filledForm));
			}
			else {
				final DoctorSchedule docSchedule=filledForm.get();

				if((docSchedule.id==null)){

					docSchedule.save();

				}else{
					docSchedule.update();
				}

				return ok("doctor time scheduled");
			}


		}


}
