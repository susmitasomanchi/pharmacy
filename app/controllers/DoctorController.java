package controllers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import models.AppUser;
import models.Doctor;
import models.DDSummary;
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
	public static Form<DDSummary> form1 = Form.form(DDSummary.class);
	public static Form<PatientBean> patientForm = Form.form(PatientBean.class);
	public static Form<QuestionAndAnswerBean> questionAndAnswerForm = Form
			.form(QuestionAndAnswerBean.class);

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
	
	public static Result enter() {
		return ok(views.html.doctorDashboard.render("hello"));
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

	public static Result form1() {
		return ok(views.html.doctor.dDSummary.render(form1));
		//return TODO
	}
	
	
	public static Result process1() {
		final Form<DDSummary> filledForm = form1.bindFromRequest();
		//Logger.info("enteredt");

		if(filledForm.hasErrors()) {
			Logger.info("bad request");

			return badRequest(views.html.doctor.dDSummary.render(filledForm));
		}
		else {
			final DDSummary dDSummary= filledForm.get();

			if(dDSummary.id == null) {

				dDSummary.save();
			}
			else {

				dDSummary.update();
			}
		}
		return TODO;
		//return redirect(routes.UserController.list());

	}
	
	
	/* public static Result editProfile(Long id){
		 
	   	   Form<Doctor> taskForm = Form.form(Doctor.class).bindFromRequest();
	   
	        if (taskForm.hasErrors()) {
	     
	          return badRequest(views.html.doctorDashboard.render(Doctor.all(), taskForm));
	   
	        }
	 else {
	          
		 Doctor.update(id, taskForm.get());
	    
	          
	            
	   return redirect(routes.DoctorController.process());
	     
	      }
	          
	        }*/






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


}
