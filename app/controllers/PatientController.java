package controllers;

import java.util.List;

import models.AppUser;
import models.Patient;
import play.Logger;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;

import com.avaje.ebean.Expr;

public class PatientController extends Controller {

	public static Form<Patient> form = Form.form(Patient.class);

	public static Result form() {

		Logger.info(""+AppUser.find.all().size());


		return ok(views.html.createPatient.render(form));
		// return TODO;
	}

	public static Result enterForm() {
		return ok(views.html.enter.render("hello"));
		// return TODO;
	}

	public static Result process() {
		final Form<Patient> filledForm = form.bindFromRequest();

		if (filledForm.hasErrors()) {
			Logger.info("bad request");
			System.out.println(filledForm.errors());
			return badRequest(views.html.createPatient.render(filledForm));
		} else {
			final Patient patient = filledForm.get();

			if (patient.id == null) {

				patient.save();
			} else {
				patient.update();
			}
		}
		return TODO;
		// return redirect(routes.UserController.list());

	}
	public static  Result search(final String search) {

		//final List<Patient> patients=Patient.find.where().eq("appUser.email", "mitesh@greensoftware.in").findList();

		final List<Patient> patients=Patient.find.where()
				.or(Expr.like("email", search+"%"),
						Expr.like("mobileno",search+"%"))
						.findList();

		return ok(patients.toString());
	}

}
