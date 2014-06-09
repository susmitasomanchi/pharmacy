
/*****

THIS IS AN AUTO GENERATED CODE
PLEASE DO NOT MODIFY IT BY HAND

 *****/
package controllers;

import models.AppUser;
import models.DiagnosticRepresentative;
import models.Doctor;
import models.MedicalRepresentative;
import models.Patient;
import models.Pharmacist;
import models.Pharmacy;
import models.Role;
import play.Logger;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import utils.Constants;
import beans.JoinUsBean;


public class UserController extends Controller {

	public static Form<JoinUsBean> joinUsForm = Form.form(JoinUsBean.class);

	public static Result joinUs(){
		return ok(views.html.joinus.render(joinUsForm));
	}


	public static Result processJoinUs(){
		final Form<JoinUsBean> filledForm = joinUsForm.bindFromRequest();
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

			if(appUser.role == Role.PHARMACIST){
				final Pharmacist pharmacist = new Pharmacist();
				pharmacist.appUser = appUser;
				pharmacist.save();

				//final Pharmacy pharmacy = filledForm.get();
				final Pharmacy pharmacy=new Pharmacy();
				pharmacy.name=filledForm.get().pharmacyName;
				pharmacy.save();
			}

			if(appUser.role == Role.MR){
				final MedicalRepresentative mr = new MedicalRepresentative();
				mr.appUser = appUser;
				mr.save();
			}

			if(appUser.role == Role.DIAGREP){
				final DiagnosticRepresentative diagRep = new DiagnosticRepresentative();
				diagRep.appUser = appUser;
				diagRep.save();
			}

			session().clear();
			session(Constants.LOGGED_IN_USER_ID, appUser.id + "");

			return redirect(routes.UserActions.dashboard());
		}


	}

}