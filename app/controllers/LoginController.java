package controllers;

import java.util.List;

import models.AppUser;
import play.Logger;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import utils.Constants;
import beans.LoginBean;

public class LoginController extends Controller {

	public static final Form<LoginBean> loginForm = Form.form(LoginBean.class);

	//	public static Result loginForm() {
	//		if (LoginController.isLoggedIn()) {
	//			return redirect(routes.UserActions.dashboard());
	//		} else {
	//			return ok(views.html.loginForm.render(loginForm));
	//		}
	//	}


	public static Result processLogin() {
		final Form<LoginBean> filledForm = loginForm.bindFromRequest();
		if (filledForm.hasErrors()) {
			return badRequest(views.html.index.render(filledForm));
		} else {
			final LoginBean loginBean = filledForm.get();
			Logger.info(loginBean.toString());
			final List<AppUser> appUsers = AppUser.find.where().eq("email", loginBean.email).eq("password", loginBean.password).findList();
			Logger.info("found users " + appUsers.toString());
			if(appUsers.size() < 1) {
				// return invalid login/password
				return badRequest(views.html.index.render(filledForm));
			}
			if(appUsers.size() == 1) {
				session().clear();
				session(Constants.LOGGED_IN_USER_ID, appUsers.get(0).id + "");
				session(Constants.LOGGED_IN_USER_ROLE, appUsers.get(0).role+ "");
				return redirect(routes.UserActions.dashboard());

			}
			if(appUsers.size() > 1) {
				session().clear();
				session(Constants.LOGGED_IN_USER_ID, appUsers.get(0).id + "");
				session(Constants.LOGGED_IN_USER_ROLE, appUsers.get(0).role+ "");
				Logger.info("more than one users exists with same email and passowrd");
				return redirect(routes.UserActions.dashboard());
			}
			return null;
		}
	}

	//@BasicAuth
	public static Result processLogout() {
		session().clear();
		return ok(views.html.index.render(loginForm));
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

	}*/

	public static AppUser getLoggedInUser() {
		final String idStr = session(Constants.LOGGED_IN_USER_ID);
		final Long id = Long.parseLong(idStr);
		return AppUser.find.byId(id);
	}

	public static Boolean isLoggedIn() {
		return session(Constants.LOGGED_IN_USER_ID) == null ? false : true;
	}

	public static String getLoggedInUserRole() {
		return session(Constants.LOGGED_IN_USER_ROLE);
	}

}