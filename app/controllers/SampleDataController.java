package controllers;


import java.util.List;

import models.AppUser;
import models.Role;
import models.mr.MedicalRepresentative;
import models.mr.PharmaceuticalCompany;
import play.mvc.Controller;
import play.mvc.Result;

public class SampleDataController extends Controller {

	public static Result populate() {

		//final Doctor doctor = new Doctor();
		//doctor.name = "Test Admin";
		/*doctor.name = "Test Admin";
		//final Doctor doctor = new Doctor();
		//doctor.name = "Test Admin";
		/*doctor.name = "Test Admin";
		//doctor.role = Role.DOCTOR;
		//doctor.email = "admin@mednet.com";
		//doctor.password = "123456";
		//doctor.save();
		doctor.email = "admin@mednet.com";
		doctor.password = "123456";
		doctor.save();*/

		final AppUser user = new AppUser();
		user.name = "Test User";
		//user.role = Role.USER;
		user.email = "user@mednet.com";
		user.password = "123456";
		user.save();

		//return ok("user created");
		return redirect(routes.Application.index());

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
		user.save();*/

		return ok("created");
	}


	public static Result createBlogAdmin(){
		if(AppUser.find.where().eq("email", "blog@mednetwork.in").findList().size()>0){
			return redirect(routes.Application.index());
		}
		final AppUser appUser = new AppUser();
		appUser.name = "Blog Admin";
		appUser.role = Role.BLOG_ADMIN;
		appUser.email = "blog@mednetwork.in";
		appUser.password = "med2014blog";
		appUser.save();
		return redirect(routes.Application.index());
	}

	public static Result test(){
		final PharmaceuticalCompany company = PharmaceuticalCompany.find.byId(1L);
		for(int i=0; i<10; i++){
			final AppUser appUser = new AppUser();
			appUser.name = "Anand"+i;
			appUser.email = "anand"+i+"@gmail.com";
			appUser.password = "1111";
			appUser.role = Role.MR;
			appUser.save();
			final MedicalRepresentative mr = new MedicalRepresentative();
			mr.appUser = appUser;
			mr.pharmaceuticalCompany = company;
			mr.save();
			company.mrList.add(mr);
			company.update();
		}
		return ok();
	}

	public static Result mrSampleData(){
		final AppUser appUser = new AppUser();
		appUser.name = "anand";
		appUser.email = "anand@gmail.com";
		appUser.password = "123";
		appUser.role = Role.ADMIN_MR;
		appUser.save();
		final MedicalRepresentative mr = new MedicalRepresentative();
		mr.appUser = appUser;
		final PharmaceuticalCompany company = new PharmaceuticalCompany();
		company.name="green pharma";
		company.save();
		mr.pharmaceuticalCompany = company;
		mr.save();
		return ok();

	}

}



