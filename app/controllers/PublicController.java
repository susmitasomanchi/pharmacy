package controllers;

import java.util.ArrayList;
import java.util.List;

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
		final List<Doctor> doctors = Doctor.find.where().like("searchIndex","%"+cleanKey+"%").findList();
		return ok(views.html.doctor.searchedDoctors.render(true, key, doctors));
	}

	/**
	 * @author Mitesh
	 * Action to search Doctor and display his profile page
	 * GET /doctor/:slugUrl
	 */
	public static Result processSlugUrl(final String slugUrl) {
		final String cleanKey = slugUrl.trim().toLowerCase();
		final Doctor doctor = Doctor.find.where().like("searchIndex","%"+cleanKey+"%").findUnique();

		return ok(views.html.doctor.doctorProfile.render(doctor));
	}


}
