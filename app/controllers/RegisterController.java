package controllers;

import models.AppUser;
import models.Patient;
import play.Logger;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;

public class RegisterController extends Controller {
	public static Form<AppUser> registrationForm=Form.form(AppUser.class);

	public static Result register(){
		return ok(views.html.registerAppUser.render(registrationForm));
	}

	public static Result registerProccess(){

		final Form<AppUser> filledForm=registrationForm.bindFromRequest();

		if(filledForm.hasErrors()) {
			Logger.info("*** user bad request");
			return badRequest(views.html.registerAppUser.render(filledForm));
		}
		else {
			final AppUser appUser  = filledForm.get();
			final Patient patient=new Patient();
			appUser.patient=patient;
			patient.appUser=appUser;
			patient.save();
			appUser.save();
			Logger.info("*** user object ");
			return ok("Registerd ");
		}

	}

}
