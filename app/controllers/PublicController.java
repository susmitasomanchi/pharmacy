package controllers;

import java.util.ArrayList;
import java.util.List;

import models.Alert;
import models.Role;
import models.diagnostic.DiagnosticCentre;
import models.doctor.Doctor;
import models.patient.Patient;
import models.patient.PatientDoctorInfo;
import models.pharmacist.Pharmacy;
import play.Logger;
import play.mvc.Controller;
import play.mvc.Result;

public class PublicController extends Controller{




	/**
	 * Action to render search doctor page
	 * GET /doctor/search
	 */
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
	 * Action to search Doctor by slug and display his profile page
	 * GET /doctor/:slugUrl
	 */
	public static Result getDoctorWithSlug(final String slug) {
		final String cleanSlug = slug.trim().toLowerCase();
		final Doctor doctor = Doctor.find.where().eq("slugUrl",cleanSlug).findUnique();
		if(doctor != null){
			return ok(views.html.doctor.publicDoctorProfile.render(doctor));
		}
		else{
			return ok("404");
		}
	}

	/**
	 * @author Lakshmi
	 * Action to search Pharmacy by slug and display its profile page
	 * GET /pharmacy/:slugUrl
	 */
	public static Result getPharmacyWithSlug(final String slug) {
		final String cleanSlug = slug.trim().toLowerCase();
		final Pharmacy pharmacy = Pharmacy.find.where().eq("slugUrl",cleanSlug).findUnique();
		if(pharmacy != null){
			return ok(views.html.pharmacist.publicPharmacyProfile.render(pharmacy));
		}
		else{
			return ok("404");
		}
	}


	/**
	 * @author Mitesh
	 * Action to add a Doctor to loggedInUser
	 * GET /doctor/add-to-favourites
	 */
	public static Result addDoctorToLoggedInUser(final Long docId) {
		if(!LoginController.isLoggedIn()){
			flash().put("alert", new Alert("alert-info","Please Login To Add Doctor.").toString());
			return redirect(routes.LoginController.loginForm());
		}else{
			final String loggedInRole=LoginController.getLoggedInUserRole();
			if(loggedInRole.compareTo(Role.PATIENT.toString()) == 0){
				final Patient patient = LoginController.getLoggedInUser().getPatient();
				final PatientDoctorInfo patientInfo = new PatientDoctorInfo();
				patientInfo.patient = patient;
				patientInfo.doctor = Doctor.find.byId(docId);
				patient.patientDoctorInfoList.add(patientInfo);
				flash().put("alert", new Alert("alert-success","Added to My Doctors").toString());
				return redirect(routes.PatientController.myFavouriteDoctors());
			}
			if(loggedInRole.compareTo(Role.ADMIN_PHARMACIST.toString()) == 0){

			}
			if(loggedInRole.compareTo(Role.ADMIN_DIAGREP.toString()) == 0){

			}
		}
		return redirect(routes.PublicController.searchDoctorsPage());
	}



	/**
	 * @author lakshmi
	 * Action to render the searchedPharmacies scala template
	 * GET	/pharmacy/search
	 */
	public static Result searchPharmaciesPage(){
		return ok(views.html.pharmacist.searched_pharmacies.render(false,"", new ArrayList<Pharmacy>()));
	}

	/**
	 * @author lakshmi
	 * Action to perform search operation for finding pharmacies based on the searchKey
	 * GET	/pharmacy/search/:searchKey
	 */
	public static Result processSearchPharmacies(final String searchKey) {
		final String searchStr = searchKey.toLowerCase().trim();
		final List<Pharmacy> pharmacyList = new ArrayList<Pharmacy>();
		if(searchStr.length()>=4){
			Logger.info("key: "+searchStr);
			pharmacyList.addAll(Pharmacy.find.where().like("searchIndex","%"+searchStr+"%").findList());
			Logger.info("size: "+pharmacyList.size());
		}
		else{
			flash().put("alert", new Alert("alert-danger", "The searck key should contain atleast four charecters").toString());
		}
		return ok(views.html.pharmacist.searched_pharmacies.render(true,searchKey,pharmacyList));
	}



	/**
	 * @author lakshmi
	 * Action to add a Pharmacy to loggedInUser
	 * GET /pharmacy/add-to-favourites
	 */
	public static Result addPharmacyToLoggedInUser(final Long pharmacyId) {
		if(!LoginController.isLoggedIn()){
			flash().put("alert", new Alert("alert-info","Please Login To Add Pharmacy.").toString());
			return redirect(routes.LoginController.loginForm());
		}else{
			final String loggedInRole=LoginController.getLoggedInUserRole();
			if(loggedInRole.compareTo(Role.PATIENT.toString()) == 0){

			}
			if(loggedInRole.compareTo(Role.DOCTOR.toString()) == 0){
				final Doctor doctor = LoginController.getLoggedInUser().getDoctor();
				final Pharmacy pharmacy = Pharmacy.find.byId(pharmacyId);
				if(!doctor.pharmacyList.contains(pharmacy)){
					doctor.pharmacyList.add(pharmacy);
					doctor.update();
				}
				return redirect(routes.DoctorController.myFavoritePharmacies());
			}
			if(loggedInRole.compareTo(Role.ADMIN_DIAGREP.toString()) == 0){

			}
			return redirect(routes.UserActions.dashboard());
		}
	}


	/**
	 * @author Lakshmi
	 * Action to search DiagnosticCentre by slug and display its profile page
	 * GET /diagnostic/:slugUrl
	 */
	public static Result getDiagnosticCentreWithSlug(final String slug) {
		final String cleanSlug = slug.trim().toLowerCase();
		final DiagnosticCentre diagnosticCentre = DiagnosticCentre.find.where().eq("slugUrl",cleanSlug).findUnique();
		if(diagnosticCentre != null){
			return ok(views.html.diagnostic.publicDiagnosticProfile.render(diagnosticCentre));
		}
		else{
			return ok("404");
		}
	}


}
