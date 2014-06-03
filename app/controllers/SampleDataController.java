package controllers;
import java.util.List;

import models.AppUser;
import models.Doctor;
import models.Patient;
import play.mvc.Controller;
import play.mvc.Result;

public class SampleDataController extends Controller {

	public static Result populate() {

		final Doctor doctor = new Doctor();
		//doctor.name = "Test Admin";
		//doctor.role = Role.DOCTOR;
		//doctor.email = "admin@mednet.com";
		//doctor.password = "123456";
		//doctor.save();

		final AppUser user = new AppUser();
		user.name = "Test User";
		//user.role = Role.USER;
		user.email = "user@mednet.com";
		user.password = "123456";
		user.save();

		return ok("user created");

	}

	public static Result cleanUp() {

		final List<AppUser> users = AppUser.find.all();
		for (final AppUser user : users) {
			user.delete();
		}

		return ok();

	}
	public static Result temp() {
		final AppUser user=new AppUser();
		user.email="mitesh@ukate.com";
		user.password="123456";
		final Patient patient=new Patient();
		user.patient=patient;
		patient.save();
		user.save();

		return ok("created");
	}

}
