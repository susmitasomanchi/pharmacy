package controllers;

import java.util.List;

import models.Alert;
import models.AppUser;
import models.mr.MedicalRepresentative;
import play.Logger;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import utils.Constants;

public class WebServices extends Controller{

	public static Result login(final String email , final String password) {

		//session().clear(); Cannot clear session() as its used to store Primary City Id
		session().remove(Constants.LOGGED_IN_USER_ID);
		session().remove(Constants.LOGGED_IN_USER_ROLE);

		final List<AppUser> appUsers = AppUser.find.where().eq("email",email.trim().toLowerCase()).findList();
		Logger.info("found appUsers: " + appUsers.size());
		Logger.info("email :  :"+email);
		Logger.info("password :"+password);
		if(appUsers.size() == 1) {
			if(appUsers.get(0).matchPassword(password.trim())){
				session(Constants.LOGGED_IN_USER_ID, appUsers.get(0).id + "");
				return ok("{\"status\":\"TRUE\"}");
			}else{
				Logger.error("Invalid password.");
				return ok("{\"status\":\"FALSE\"}");
			}

		}else{
			// return invalid login/password
			Logger.error("Invalid username.");
			flash().put("alert", new Alert("alert-danger", "Sorry! Invalid Username/Password.").toString());
			return ok("{\"status\":\"FALSE\"}");
		}
	}

	public static Result logout() {
		//session().clear();
		session().remove(Constants.LOGGED_IN_USER_ID);
		Logger.info("logout successful");
		return ok("{\"status\":\"TRUE\"}");
	}

	public static Result myDoctor(){
		final MedicalRepresentative loggedInMr = LoginController
				.getLoggedInUser().getMedicalRepresentative();
		Logger.info("server side method executed");
		return ok(Json.toJson(loggedInMr.doctorList));

		/*if(request.format.equals("json"))
			return ok(Json.toJson(loggedInMr.doctorList));
		else
			return ok(loggedInMr.doctorList);
		 *///return ok(Json.toJson(loggedInMr.doctorList));

	}
	public static Result myProduct(){
		final MedicalRepresentative loggedInMr = LoginController
				.getLoggedInUser().getMedicalRepresentative();
		return ok(Json.toJson(loggedInMr.pharmaceuticalCompany.pharmaceuticalProductList));
	}



}
