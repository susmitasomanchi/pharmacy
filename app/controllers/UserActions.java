package controllers;

import models.AppUser;
import play.mvc.Controller;
import play.mvc.Result;
import actions.BasicAuth;

@BasicAuth
public class UserActions extends Controller {

	public static Result dashboard() {
		final AppUser appUser = LoginController.getLoggedInUser();
		return ok(views.html.dashboard.render(appUser));
	}

}
