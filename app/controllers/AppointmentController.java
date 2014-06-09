package controllers;

import models.Appointment;
import play.Logger;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;

public class AppointmentController extends Controller {

	public static Form<Appointment> form = Form.form(Appointment.class);

	public static Result form() {
		return ok(views.html.scheduleAppointmentTest.render(form));
		//return TODO;
	}
	public static Result process() {
		final Form<Appointment> filledForm = form.bindFromRequest();


		if(filledForm.hasErrors()) {
			Logger.info("bad request");
			System.out.println(filledForm.errors());

			return badRequest(views.html.scheduleAppointmentTest.render(filledForm));
		}
		else {
			final Appointment appointment= filledForm.get();

			if(appointment.id == null) {

				appointment.save();
			}
			else {

				appointment.update();
			}
		}
		return TODO;
		//return redirect(routes.UserController.list());

	}

}
