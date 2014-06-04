package controllers;

import models.AppUser;
import models.Patient;
import play.mvc.Controller;
import play.mvc.Result;

//@BasicAuth
public class UserActions extends Controller {

	public static Result dashboard() {


		final AppUser appUser =LoginController.getLoggedInUser();
		final Patient patient=Patient.find.where().eq("appUser", appUser).findUnique();
		return ok("dashboard"+appUser);

	}


}
