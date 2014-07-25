package controllers;

import java.util.ArrayList;

import models.AppUser;
import models.Role;
import models.patient.PatientDoctorInfo;
import play.mvc.Controller;
import play.mvc.Result;
import actions.BasicAuth;

@BasicAuth
public class UserActions extends Controller {


	/**
	 * @lastUpdateBy: Buta
	 * Action to render the respective dashboard of the loggedIn user based on his/her role
	 * GET	/dashboard
	 */
	public static Result dashboard() {
		final AppUser appUser = LoginController.getLoggedInUser();
		if(appUser.role.equals(Role.ADMIN_PHARMACIST)){
			return ok(views.html.pharmacist.pharmacy_profile.render(appUser.getPharmacist().pharmacy));
		}
		if(appUser.role.equals(Role.ADMIN_DIAGREP)){
			return ok(views.html.diagnostic.diagnostic_centre_profile.render(appUser.getDiagnosticRepresentative().diagnosticCentre));
		}
		if(appUser.role.equals(Role.DOCTOR)){
			return ok(views.html.doctor.doctorProfile.render(appUser.getDoctor()));
		}
		if(appUser.role.equals(Role.PATIENT)){
			return ok(views.html.patient.fav_doctors.render(new ArrayList<PatientDoctorInfo>()));
		}

		//@TODO: none should render the dashboard of patient
		return ok("Not implemented yet");
	}

}
