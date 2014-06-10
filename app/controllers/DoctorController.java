package controllers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import actions.BasicAuth;
import models.AppUser;
import models.Doctor;
import models.Patient;
import play.Logger;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import beans.PatientBean;

@BasicAuth
public class DoctorController extends Controller {



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

















	public static Form<Doctor> form = Form.form(Doctor.class);
	public static Form<PatientBean> patientForm = Form.form(PatientBean.class);

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


}
