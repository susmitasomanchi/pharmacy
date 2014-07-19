
/*****

THIS IS AN AUTO GENERATED CODE
PLEASE DO NOT MODIFY IT BY HAND
 *****/
/*****

THIS IS AN AUTO GENERATED CODE
PLEASE DO NOT MODIFY IT BY HAND
 *****/
package controllers;

import models.Alert;
import models.AppUser;
import models.Patient;
import models.Role;
import models.diagnostic.DiagnosticRepresentative;
import models.doctor.Doctor;
import models.mr.MedicalRepresentative;
import models.mr.PharmaceuticalCompany;
import models.pharmacist.Pharmacist;
import models.pharmacist.Pharmacy;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import utils.Constants;
import beans.JoinUsBean;


public class UserController extends Controller {

	public static Form<JoinUsBean> joinUsForm = Form.form(JoinUsBean.class);
	public static Form<DiagnosticRepresentative> drForm = Form.form(DiagnosticRepresentative.class);
	public static Form<MedicalRepresentative> mrForm = Form.form(MedicalRepresentative.class);



	/**
	 * Action to render the joinUs page for Doctor
	 * GET    /doctor/join
	 */
	public static Result joinUsDoctor(){
		return ok(views.html.doctor.joinus.render());
	}


	/**
	 * Action to render the joinUs page for Pharmacy
	 * GET    /pharmacy/join
	 */
	public static Result joinUsPharmacy(){
		return ok(views.html.pharmacist.joinus.render());
	}



	/**
	 * Action to onboard a new AppUser along with its Role's entity
	 * POST   /join
	 */
	public static Result processJoinUs(){
		final AppUser appUser = new AppUser();
		appUser.name = request().body().asFormUrlEncoded().get("fullname")[0].trim();
		appUser.email = request().body().asFormUrlEncoded().get("email")[0].trim();
		appUser.password = request().body().asFormUrlEncoded().get("password")[0].trim();
		appUser.role = Role.valueOf(request().body().asFormUrlEncoded().get("role")[0]);

		if(AppUser.find.where().eq("email", appUser.email).findRowCount()>0){
			flash().put("alert", new Alert("alert-danger", "Sorry! User with email id "+appUser.email.trim()+" already exists!").toString());
			if(appUser.role == Role.DOCTOR){
				return redirect(routes.UserController.joinUsDoctor());
			}
			if(appUser.role == Role.ADMIN_PHARMACIST){
				return redirect(routes.UserController.joinUsPharmacy());
			}
		}

		appUser.save();

		if(appUser.role == Role.ADMIN_MR){

			final MedicalRepresentative medicalRepresentative = new MedicalRepresentative();
			final PharmaceuticalCompany pharmaCompany = new PharmaceuticalCompany();
			medicalRepresentative.appUser = appUser;
			medicalRepresentative.save();
			pharmaCompany.name = request().body().asFormUrlEncoded().get("pharmaceuticalCompanyName")[0].trim();
			pharmaCompany.mrList.add(medicalRepresentative);
			//pharmaCompany.appuserid=appUser.id;
			pharmaCompany.save();
			medicalRepresentative.pharmaceuticalCompany = pharmaCompany;
			medicalRepresentative.update();

		}


		if(appUser.role.equals(Role.DOCTOR)){
			final Doctor doctor = new Doctor();
			doctor.specialization = "Specialization";
			doctor.degree = "Degree";
			doctor.appUser = appUser;
			doctor.save();
		}

		if(appUser.role.equals(Role.ADMIN_PHARMACIST)){
			final Pharmacist pharmacist = new Pharmacist();
			pharmacist.appUser = appUser;
			pharmacist.save();

			final Pharmacy pharmacy = new Pharmacy();
			pharmacy.name = request().body().asFormUrlEncoded().get("pharmacyName")[0];
			pharmacy.adminPharmacist = pharmacist;
			pharmacy.save();
			pharmacist.pharmacy = pharmacy;
			pharmacist.update();
		}

		if(appUser.role.equals(Role.PATIENT)){
			final Patient patient= new Patient();
			patient.appUser = appUser;
			patient.save();
		}

		session().clear();
		session(Constants.LOGGED_IN_USER_ID, appUser.id + "");
		session(Constants.LOGGED_IN_USER_ROLE, appUser.role+ "");

		return redirect(routes.UserActions.dashboard());
	}

	/**
	 * **************************** DEPRICATED ON 14 JUL 2014 ****************************
	 * @deprecated Use UserController.processJoinUs() instead which is generic for all AppUser roles
	 * Action to onboard a new Doctor by creating a models.Doctor and a models.AppUser
	 * POST   /join
	 */
	@Deprecated
	public static Result processDoctorJoinUs(){
		final AppUser appUser = new AppUser();
		appUser.name = request().body().asFormUrlEncoded().get("fullname")[0];
		appUser.email = request().body().asFormUrlEncoded().get("email")[0];
		appUser.password = request().body().asFormUrlEncoded().get("password")[0];
		appUser.role = Role.DOCTOR;
		appUser.save();

		final Doctor doctor = new Doctor();
		doctor.specialization = "Specialization";
		doctor.degree = "Degree";
		doctor.appUser = appUser;
		doctor.save();

		session().clear();
		session(Constants.LOGGED_IN_USER_ID, appUser.id + "");
		session(Constants.LOGGED_IN_USER_ROLE, appUser.role+ "");

		return redirect(routes.UserActions.dashboard());
	}



























	/*
	public static Result processJoinUs(){
		final Form<JoinUsBean> filledForm = joinUsForm.bindFromRequest();
		final Form<MedicalRepresentative> mR = mrForm.bindFromRequest();
		final Form<DiagnosticRepresentative> dr = drForm.bindFromRequest();
		if(filledForm.hasErrors()) {
			Logger.info("Form Errors");
			Logger.error(filledForm.errors().toString());
			return badRequest(views.html.joinus.render(filledForm));
		}
		else {
			final AppUser appUser = filledForm.get().toAppUser();
			appUser.save();
			if(appUser.role == Role.PATIENT){
				final Patient patient = new Patient();
				patient.appUser = appUser;
				patient.save();
			}

			if(appUser.role == Role.DOCTOR){
				final Doctor doctor = new Doctor();
				doctor.appUser = appUser;
				doctor.save();

			}

			if(appUser.role == Role.ADMIN_PHARMACIST){
				final Pharmacist pharmacist = new Pharmacist();
				pharmacist.appUser = appUser;
				pharmacist.save();

				//final Pharmacy pharmacy = filledForm.get();
				final Pharmacy pharmacy = new Pharmacy();
				pharmacy.name=filledForm.get().pharmacyName;
				pharmacy.pharmacistList.add(pharmacist);
				pharmacy.save();
				pharmacist.pharmacy=pharmacy;
				pharmacist.update();
			}

			if(appUser.role == Role.ADMIN_MR){
				final MedicalRepresentative medicalRepresentative = new MedicalRepresentative();
				final PharmaceuticalCompany pharmaCompany = new PharmaceuticalCompany();
				medicalRepresentative.appUser = appUser;
				medicalRepresentative.save();
				pharmaCompany.name = filledForm.get().pharmaceuticalCompanyName;

				pharmaCompany.mrList.add(medicalRepresentative);
				pharmaCompany.save();
				medicalRepresentative.pharmaceuticalCompany = pharmaCompany;
				medicalRepresentative.update();

			}

			if(appUser.role == Role.MR){
				//				final MedicalRepresentative medicalRepresentative = new MedicalRepresentative();
				//				medicalRepresentative.appUser = appUser;
				//				medicalRepresentative.regionAlloted=mR.regionAlloted;
				//				medicalRepresentative.save();
				final MedicalRepresentative medicalRepresentative = mR.get();
				medicalRepresentative.appUser = appUser;
				medicalRepresentative.mrAdminId =  LoginController.getLoggedInUser().id;
				medicalRepresentative.save();
			}



			if(appUser.role == Role.ADMIN_DIAGREP){
				final DiagnosticRepresentative diagRep = new DiagnosticRepresentative();
				diagRep.appUser = appUser;
				diagRep.save();
				final DiagnosticCentre diagnosticCenter = new DiagnosticCentre();
				diagnosticCenter.name = filledForm.get().diagnosticCenterName;
				diagnosticCenter.diagnosticRepAdmin = diagRep;
				diagnosticCenter.save();
				diagRep.diagnosticCentre = diagnosticCenter;
				diagRep.update();

				Logger.info(diagRep.diagnosticCentre.name);

			}

			session().clear();
			session(Constants.LOGGED_IN_USER_ID, appUser.id + "");
			session(Constants.LOGGED_IN_USER_ROLE, appUser.role+ "");

			return redirect(routes.UserActions.dashboard());
		}


	}
	 */

}
