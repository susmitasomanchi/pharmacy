package controllers;

import models.AppUser;
import models.Patient;
import play.Logger;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;

public class PatientController extends Controller {

	public static Form<Patient> form = Form.form(Patient.class);

	public static Form<AppUser> registrationForm=Form.form(AppUser.class);

	public static Result register(){
		return ok(views.html.registerAppUser.render(registrationForm));
	}

	public static Result registerProccess(){

		final Form<AppUser> filledForm=registrationForm.bindFromRequest();

		if(filledForm.hasErrors()) {
			Logger.info("*** user bad request");
			return badRequest(views.html.registerAppUser.render(filledForm));
		}
		else {
			final AppUser appUser  = filledForm.get();
			final Patient patient=new Patient();
			//appUser.patient=patient;
			patient.appUser=appUser;
			patient.save();
			appUser.save();
			Logger.info("*** user object ");
			return ok("Registerd ");
		}

	}



	public static Result form() {
		return ok(views.html.createPatient.render(form));
		//return TODO;
	}
	public static Result enterForm() {
		return ok(views.html.enter.render("hello"));
		//return TODO;
	}


	public static Result process() {
		final Form<Patient> filledForm = form.bindFromRequest();
		//Logger.info("enteredt");

		if(filledForm.hasErrors()) {
			Logger.info("bad request");
			//System.out.println("dsdshillllllll");
			System.out.println(filledForm.errors());
			return badRequest(views.html.createPatient.render(filledForm));
		}
		else {
			final Patient patient= filledForm.get();

			if(patient.id == null) {


				patient.save();
			}
			else {
			
				patient.update();
			}
		}
		return ok(views.html.scheduleAppointment.render("hello"));
		//return redirect(routes.UserController.list());

	}


}
