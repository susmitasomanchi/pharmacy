package controllers;

import models.Patient;
import play.Logger;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;

public class PatientController extends Controller {

	public static Form<Patient> form = Form.form(Patient.class);

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

				//System.out.println("hiiiii");
				patient.save();
			}
			else {
				//System.out.println("heeellloooo");
				patient.update();
			}
		}
		return TODO;
		//return redirect(routes.UserController.list());

	}


}
