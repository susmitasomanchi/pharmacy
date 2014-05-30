package controllers;

import java.util.List;

import models.AppUser;
import models.Doctor;
import models.Patient;
import models.Pharmacist;
import play.Logger;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import utils.Constants;
import beans.LoginBean;

public class LoginController extends Controller {

	public static final Form<LoginBean> loginForm = Form.form(LoginBean.class);

	public static Result loginForm() {
		if (LoginController.isLoggedIn()) {
			//return redirect(routes.UserActions.dashboard());
			return ok("hii login");
		} else {
			//return ok("hii Not login");

			return ok(views.html.loginForm.render(loginForm));
		}
	}


	public static Result processLogin() {
		final Form<LoginBean> filledForm = loginForm.bindFromRequest();

		if (filledForm.hasErrors()) {

			return badRequest(views.html.loginForm.render(filledForm));
		} else {

			final LoginBean loginBean = filledForm.get();

			Logger.info(loginBean.toString());


			final List<Doctor> doctors = Doctor.find.where().eq("email", loginBean.email).
					eq("password", loginBean.password).findList();
			final List<Patient> patients = Patient.find.where().eq("email", loginBean.email)
					.eq("password", loginBean.password).findList();
			final List<Pharmacist> pharmacists  = Pharmacist.find.where().eq("email", loginBean.email)
					.eq("password", loginBean.password).findList();


			//Logger.info("found users " + appUsers.toString());
			final List<AppUser> appUsers=whichUserLogging(doctors, patients, pharmacists);

			if (appUsers.size()<=0) {

				// return invalid login/password

				return badRequest(views.html.loginForm.render(filledForm));
			} else if (doctors.size() == 1 || doctors.size() == 1 || doctors.size() == 1) {
				session().clear();
				session(Constants.LOGGED_IN_USER_ID, appUsers.get(0).id + "");
				//return redirect(routes.UserActions.dashboard());
				return ok("login successfull");
			} else {
				session().clear();
				session(Constants.LOGGED_IN_USER_ID, appUsers.get(0).id + "");
				Logger.info("more than one users exists with same email and passowrd");
				//return redirect(routes.UserActions.dashboard());
				return ok("login successfull");

			}

		}

	}

	//@BasicAuth
	public static Result processLogout() {
		session().clear();
		return ok(views.html.index.render("logout successful"));
	}

	public static List whichUserLogging(final List doctors,final List patients,final List pharmacists) {
		List user=null;
		if(doctors.size()>=1){
			user= doctors;
		}
		else if (patients.size()>=1) {
			user= patients;
		}
		else {
			user= pharmacists;
		}
		return user;
	}
	/*//Change Password
	@BasicAuth
	public static Result changePasswordForm(){
		return ok(views.html.mod.changePwdForm.render());
	}

	@BasicAuth
	public static Result processChangePassword(){
		final String oldpwd=Form.form().bindFromRequest().get("oldpwdname");
		final String newpwd=Form.form().bindFromRequest().get("newpwdname");
		final AppUser loggedUser=LoginController.getLoggedInUser();

		if(loggedUser.password.compareTo(oldpwd)==0){
			loggedUser.password=newpwd;
			loggedUser.update();
			flash().put("alert", new Alert("alert-success", "Password Changed Successfuly.").toString());			redirect(routes.LoginController.processLogout());
			return redirect(routes.LoginController.processLogout());

		}
		else{
			flash().put("alert", new Alert("alert-danger", "Old Password Is Incorrect.").toString());			redirect(routes.LoginController.processLogout());
			return redirect(routes.LoginController.processChangePassword());
		}

	}

	public static AppUser getLoggedInUser() {
		final String idStr = session(Constants.LOGGED_IN_USER_ID);
		final Long id = Long.parseLong(idStr);
		final AppUser user = AppUser.find.byId(id);
		return user;
	}*/

	public static Boolean isLoggedIn() {
		return session(Constants.LOGGED_IN_USER_ID) == null ? false : true;
	}

	/*@BasicAuth
	public static Result changePassword() {

		return TODO;
	}*/
}
