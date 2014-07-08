package controllers;

import models.doctor.Doctor;
import play.mvc.Controller;
import play.mvc.Result;
import actions.BasicAuth;

@BasicAuth
public class UserActions extends Controller {

	public static Result dashboard() {
		final Doctor doctor = LoginController.getLoggedInUser().getDoctor();
		return ok(views.html.doctor.doctor_profile.render(doctor));
	}

}
