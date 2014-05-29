package controllers;

import models.Doctor;
import models.Pharmacist;
import play.Logger;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;

public class DoctorController extends Controller {

	public static Form<Doctor> form = Form.form(Doctor.class);

	public static Result form() {
		return ok(views.html.createDoctor.render(form));
		//return TODO;
	}

	public static Result process() {
		final Form<Doctor> filledForm = form.bindFromRequest();
		//Logger.info("enteredt");

		if(filledForm.hasErrors()) {
			Logger.info("bad request");
			//System.out.println("dsdshillllllll");
			return badRequest(views.html.createDoctor.render(filledForm));
		}
		else {
			final Doctor doctor= filledForm.get();

			if(doctor.id == null) {
				//System.out.println("hiiiii");
				doctor.save();
			}
			else {
				//System.out.println("heeellloooo");
				doctor.update();
			}
		}
		return TODO;
		//return redirect(routes.UserController.list());

	}


}
