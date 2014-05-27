package controllers;
import java.util.List;

import models.AppUser;
import play.mvc.Controller;
import play.mvc.Result;

public class SampleDataController extends Controller {

	public static Result populate() {

		final AppUser admin = new AppUser();
		admin.name = "Test Admin";
		//admin.role = Role.ADMIN;
		admin.email = "admin@mednet.com";
		admin.password = "123456";
		admin.save();

		final AppUser user = new AppUser();
		user.name = "Test User";
		//user.role = Role.USER;
		user.email = "user@mednet.com";
		user.password = "123456";
		user.save();

		//return ok("user created");
		return redirect(routes.LoginController.loginForm());

	}

	public static Result cleanUp() {

		final List<AppUser> users = AppUser.find.all();
		for (final AppUser user : users) {
			user.delete();
		}

		return ok();
	}

}
