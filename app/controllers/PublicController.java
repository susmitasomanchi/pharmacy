package controllers;

import java.util.ArrayList;
import java.util.List;

import models.Alert;
import models.doctor.Doctor;
import play.mvc.Controller;
import play.mvc.Result;

public class PublicController extends Controller{




	public static Result searchDoctorsPage(){
		return ok(views.html.doctor.searchedDoctors.render(false,"", new ArrayList<Doctor>()));
	}

	/**
	 * @author Mitesh
	 * Action to search Doctor and display it
	 * GET /doctor/search/:key
	 */
	public static Result processSearchDoctors(final String key) {
		final String cleanKey = key.trim().toLowerCase();
		if(cleanKey.length() < 4){
			flash().put("alert", new Alert("alert-info", "Searched word should be atleast 4 characters long.").toString());
			return ok(views.html.doctor.searchedDoctors.render(false,key, new ArrayList<Doctor>()));
		}
		else{
			final List<Doctor> doctors = Doctor.find.where().like("searchIndex","%"+cleanKey+"%").findList();
			return ok(views.html.doctor.searchedDoctors.render(true, key, doctors));
		}
	}

	/**
	 * @author Mitesh
	 * Action to search Doctor and display his profile page
	 * GET /doctor/:slugUrl
	 */
	public static Result getDoctorWithSlug(final String slug) {
		final String cleanSlug = slug.trim().toLowerCase();
		final Doctor doctor = Doctor.find.where().eq("slugUrl",cleanSlug).findUnique();
		return ok(views.html.doctor.publicDoctorProfile.render(doctor));
	}


}
