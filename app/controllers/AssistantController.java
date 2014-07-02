package controllers;

import models.doctor.Doctor;
import models.doctor.DoctorAssistant;
import play.Logger;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;

public class AssistantController extends Controller {

	public static Form<DoctorAssistant> form = Form.form(DoctorAssistant.class);

	public static Result form() {
		return ok(views.html.createAssistant.render(form));
		//return TODO;
	}

	public static Result process() {
		final Form<DoctorAssistant> filledForm = form.bindFromRequest();
		//Logger.info("enteredt");

		if(filledForm.hasErrors()) {
			Logger.info("bad request");

			return badRequest(views.html.createAssistant.render(filledForm));
		}
		else {
			final DoctorAssistant assistant= filledForm.get();

			if(assistant.id == null) {

				assistant.save();
			}
			else {

				assistant.update();
			}
		}
		return ok(views.html.assistantDashboard.render("Your new application is ready."));
		//return redirect(routes.UserController.list());

	}

	public static Result process2() {

		return TODO;

	}

}





