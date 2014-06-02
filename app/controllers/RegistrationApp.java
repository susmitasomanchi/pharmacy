package controllers;

import models.RegisterAppUser;
import play.Logger;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import beans.RegisterAppUserBean;

public class RegistrationApp extends Controller{
	public static Form<RegisterAppUserBean> registrationForm=Form.form(RegisterAppUserBean.class);

	public static Result register(){
		return ok(views.html.registerAppUser.render(registrationForm));
	}

	public static Result registerProccess(){

		final Form<RegisterAppUserBean> filledForm=registrationForm.bindFromRequest();

		if(filledForm.hasErrors()) {
			Logger.info("*** user bad request");
			return badRequest(views.html.registerAppUser.render(filledForm));
		}
		else {
			final RegisterAppUserBean appUserBean  = filledForm.get();
			final RegisterAppUser appUser = appUserBean.toEntity();
			Logger.info("*** user object ");

			if(appUser.id == null) {
				//final AppUser appUser = salesRepForm.toEntity();
				appUser.save();
				final String message = flash("success");

			}
			else {
				appUser.update();
			}
		}


		return TODO;
	}

}
