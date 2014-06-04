package controllers;
import java.util.List;

import models.AppUser;
import play.mvc.Controller;
import play.mvc.Result;

public class SampleDataController extends Controller {

	public static Result populate() {
		return TODO;

	}

	public static Result cleanUp() {

		final List<AppUser> users = AppUser.find.all();
		for (final AppUser user : users) {
			user.delete();
		}

		return ok();

	}
	public static Result temp() {
		/*final AppUser user=new AppUser();
		user.email="mitesh@ukate.com";
		user.password="123456";
		final Patient patient=new Patient();
		//user.patient=patient;
		patient.save();
		user.save();
		 */
		return ok("created");
	}

}
