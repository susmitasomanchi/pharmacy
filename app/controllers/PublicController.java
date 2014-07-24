package controllers;

import java.util.ArrayList;
import java.util.List;

import models.Alert;
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
		return ok(views.html.pharmacist.searched_pharmacies.render(false,"", new ArrayList<Pharmacy>()));
	}

	/**
	 * @author lakshmi
	 * Action to perform search operation for finding pharmacies based on
	 * name and area
	 * GET/pharmacy/search/:searchString
	 */
	public static Result searchPharmacies(final String searchString) {
		final String searchStr = searchString.toLowerCase().trim();
		List<Pharmacy> pharmacyList = new ArrayList<Pharmacy>();
		if(searchStr.length()>=4){
			pharmacyList = Pharmacy.find.where().like("searchIndex","%"+searchStr+"%").findList();
		}
		else{
			flash().put("alert", new Alert("alert-danger", "The searck key should contain four charecters").toString());
		}
		return ok(views.html.pharmacist.searched_pharmacies.render(true,searchStr,pharmacyList));
	}
	/**
	 *@author lakshmi
	 * Action to add pharmacy to the Logged in user list
	 * 
	 */
	public static Result myFavoritePharmacy(final Long pharmacyId,final String searchKey){
		if(LoginController.getLoggedInUserRole().equals("DOCTOR")){
			return redirect(routes.DoctorController.addFavoritePharmacy(pharmacyId,searchKey));
		}
		else if(LoginController.getLoggedInUserRole().equals("PATIENT")){
			return redirect(routes.PatientController.addFavoritePharmacy(pharmacyId,searchKey));
		}else{

			return redirect(routes.UserController.processJoinUs());
		}


	}


}
