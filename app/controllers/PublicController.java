package controllers;

import java.util.ArrayList;
import java.util.List;

import models.doctor.Doctor;
import models.pharmacist.Pharmacy;
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
	 * @author lakshmi
	 * Action to render the searchedPharmacies scala template
	 * GET/pharmacy/search
	 */
	public static Result searchPhamacy(){
		return ok(views.html.pharmacist.favourite_pharmacies.render(false,"", new ArrayList<Pharmacy>()));
	}

	/**
	 * @author lakshmi
	 * Action to perform search operation for finding pharmacies based on
	 * name and area
	 * GET/pharmacy/search/:searchString
	 */
	public static Result searchPharmacies(final String searchString) {
		final String searchStr = searchString.toLowerCase().trim();
		final List<Pharmacy> pharmacyList = Pharmacy.find.where().like("searchIndex","%"+searchStr+"%").findList();
		return ok(views.html.pharmacist.favourite_pharmacies.render(true,searchStr,pharmacyList));
	}


}
