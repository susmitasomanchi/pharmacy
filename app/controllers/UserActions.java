package controllers;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import models.AppUser;
import models.Role;
import models.diagnostic.DiagnosticCentre;
import models.doctor.Doctor;
import models.patient.Patient;
import models.pharmacist.Pharmacy;
import play.Logger;
import play.mvc.Controller;
import play.mvc.Result;
import actions.BasicAuth;

@BasicAuth
public class UserActions extends Controller {


	/**
	 * @lastUpdateBy: Buta
	 * Action to render the respective dashboard of the loggedIn user based on his/her role
	 * GET	/secure-dashboard
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
			final Patient patient = appUser.getPatient();
			return ok(views.html.patient.fav_doctors.render(patient.patientDoctorInfoList));
		}


		if(appUser.role.equals(Role.MEDNETWORK_ADMIN)){

			final Calendar cal = Calendar.getInstance();
			cal.setTime(new Date());
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);

			final Map<String, Integer> todayMap = new HashMap<String, Integer>();
			todayMap.put("appUsers", AppUser.find.where().ge("createdOn", cal.getTime()).findRowCount());
			todayMap.put("patients", Patient.find.where().ge("createdOn", cal.getTime()).findRowCount());
			todayMap.put("doctors", Doctor.find.where().ge("createdOn", cal.getTime()).findRowCount());
			todayMap.put("pharmacies", Pharmacy.find.where().ge("createdOn", cal.getTime()).findRowCount());
			todayMap.put("dc", DiagnosticCentre.find.where().ge("createdOn", cal.getTime()).findRowCount());

			final Map<String, Integer> weekMap = new HashMap<String, Integer>();
			cal.add(Calendar.DATE, -7);
			weekMap.put("appUsers", AppUser.find.where().ge("createdOn", cal.getTime()).findRowCount());
			weekMap.put("patients", Patient.find.where().ge("createdOn", cal.getTime()).findRowCount());
			weekMap.put("doctors", Doctor.find.where().ge("createdOn", cal.getTime()).findRowCount());
			weekMap.put("pharmacies", Pharmacy.find.where().ge("createdOn", cal.getTime()).findRowCount());
			weekMap.put("dc", DiagnosticCentre.find.where().ge("createdOn", cal.getTime()).findRowCount());

			return ok(views.html.mednetAdmin.adminDashboard.render(todayMap, weekMap));
		}

		//@TODO: none should render the dashboard of patient
		return ok("Not implemented yet");

	}

}
