
/*****

THIS IS AN AUTO GENERATED CODE
PLEASE DO NOT MODIFY IT BY HAND

 *****/
package controllers;



import models.AppUser;


import models.diagnostic.DiagnosticCentre;
import models.diagnostic.DiagnosticRepresentative;
import models.MedicalRepresentative;
import models.Patient;
import models.PharmaceuticalCompany;
import models.Pharmacist;
import models.Pharmacy;
import models.Role;
import models.doctor.Doctor;
import play.Logger;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import utils.Constants;
import beans.JoinUsBean;


public class UserController extends Controller {

	public static Form<JoinUsBean> joinUsForm = Form.form(JoinUsBean.class);
	public static Form<DiagnosticRepresentative> drForm = Form.form(DiagnosticRepresentative.class);

	public static Result joinUs(){
		return ok(views.html.joinus.render(joinUsForm));
	}


	public static Result processJoinUs(){
		final Form<JoinUsBean> filledForm = joinUsForm.bindFromRequest();
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
				//pharmacy.adminPharmacist=pharmacist;
				pharmacy.adminPharmacist = pharmacist;
				pharmacy.save();
				//pharmacy.update();
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

}
