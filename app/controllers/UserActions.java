package controllers;

import models.AppUser;
import models.Role;
import models.doctor.Doctor;
import play.mvc.Controller;
import play.mvc.Result;
import actions.BasicAuth;

@BasicAuth
public class UserActions extends Controller {

	public static Result dashboard() {


		final AppUser appUser = LoginController.getLoggedInUser();
		if(appUser.role.equals(Role.ADMIN_PHARMACIST)){
			return ok(views.html.pharmacist.pharmacy_profile.render(appUser.getPharmacist().pharmacy));
		}
		if(appUser.role.equals(Role.DOCTOR)){
			final Doctor doctor = LoginController.getLoggedInUser().getDoctor();
			return ok(views.html.doctor.doctorProfile.render(doctor));
		}
		return ok();
	}

}
