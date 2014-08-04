package controllers;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.codec.binary.Base64;

import models.Alert;
import models.AppUser;
import models.Role;
import models.diagnostic.DiagnosticCentre;
import models.diagnostic.DiagnosticRepresentative;
import play.Logger;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import utils.Constants;
import actions.BasicAuth;
import beans.LoginBean;

public class LoginController extends Controller {

	public static final Form<LoginBean> loginForm = Form.form(LoginBean.class);


	/**
	 * 
	 */
	//public static Result blogAdminLoginForm(){
	//	return ok(views.html.adminlogin.render(loginForm));
	//}



	public static Result loginForm() {
		if (LoginController.isLoggedIn()) {
			return redirect(routes.UserActions.dashboard());
		} else {
			//return ok(views.html.loginForm.render(loginForm));
			return ok(views.html.adminlogin.render(loginForm));
		}
	}


	/**
	 *	Action to process login and redirecting to respective user's dashboard
	 *	POST   /login
	 */

	public static Result processLogin() {
		session().clear();
		final Form<LoginBean> filledForm = loginForm.bindFromRequest();
		if (filledForm.hasErrors()) {
			return badRequest(views.html.adminlogin.render(filledForm));
		}
		else {
			final LoginBean loginBean = filledForm.get();
			final List<AppUser> appUsers = AppUser.find.where().eq("email", loginBean.email.trim().toLowerCase()).findList();
			Logger.info("found users " + appUsers.toString());
			if(appUsers.size() < 1) {
				// return invalid login/password
				Logger.error("Invalid username/password");
				flash().put("alert", new Alert("alert-danger", "Sorry! Invalid Username/Password.").toString());
				return redirect(routes.LoginController.loginForm());
			}
			if(appUsers.size() == 1) {
				if(!(appUsers.get(0).matchPassword(loginBean.password.trim()))){
					Logger.error("Invalid username/password");
					flash().put("alert", new Alert("alert-danger", "Sorry! Invalid Username/Password.").toString());
					return redirect(routes.LoginController.loginForm());
				}
				if(appUsers.get(0).role.equals(Role.BLOG_ADMIN)){
					session(Constants.LOGGED_IN_USER_ID, appUsers.get(0).id + "");
					session(Constants.LOGGED_IN_USER_ROLE, appUsers.get(0).role+ "");
					return redirect(routes.BlogController.categories());
				}
				session(Constants.LOGGED_IN_USER_ID, appUsers.get(0).id + "");
				session(Constants.LOGGED_IN_USER_ROLE, appUsers.get(0).role+ "");
				return redirect(routes.UserActions.dashboard());
			}
			if(appUsers.size() > 1) {
				session(Constants.LOGGED_IN_USER_ID, appUsers.get(0).id + "");
				session(Constants.LOGGED_IN_USER_ROLE, appUsers.get(0).role+ "");
				Logger.info("More than one AppUser exists with same email");
				return redirect("/");
			}
			return null;
		}
	}

	//@BasicAuth
	public static Result processLogout() {
		session().clear();
		return redirect(routes.Application.index());
	}

	//Change Password
	@BasicAuth
	public static Result editPassword(){
		return ok(views.html.changePassword.render());
	}

	@BasicAuth
	public static Result updatePassword(){
		final Map<String, String[]> requestMap = request().body().asFormUrlEncoded();
		final String oldPassword = requestMap.get("oldPassword")[0].trim();
		final String newPassword = requestMap.get("newPassword")[0].trim();
		final String confirmNewPassword = requestMap.get("confirmNewPassword")[0].trim();
		final Long appUserId = Long.parseLong(requestMap.get("appUserId")[0]);
		final AppUser loggedInUser = LoginController.getLoggedInUser();

		if(appUserId.longValue() != loggedInUser.id.longValue()){
			session().clear();
			return redirect(routes.LoginController.processLogout());
		}

		if(newPassword.compareTo(confirmNewPassword) != 0){
			flash().put("alert", new Alert("alert-danger", "Sorry! Passwords do not match.").toString());
			return redirect(routes.LoginController.editPassword());
		}

		if(!loggedInUser.matchPassword(oldPassword)){
			flash().put("alert", new Alert("alert-danger", "Sorry! Old Password is incorrect.").toString());
			return redirect(routes.LoginController.editPassword());
		}
		else{
			try {
				final Random random = new SecureRandom();
				final byte[] saltArray = new byte[32];
				random.nextBytes(saltArray);
				final String randomSalt = Base64.encodeBase64String(saltArray);

				final String passwordWithSalt = newPassword+randomSalt;
				final MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
				final byte[] passBytes = passwordWithSalt.getBytes();
				final String hashedPasswordWithSalt = Base64.encodeBase64String(sha256.digest(passBytes));

				loggedInUser.salt = randomSalt;
				loggedInUser.password = hashedPasswordWithSalt;

			} catch (final Exception e) {
				Logger.error("ERROR WHILE CREATING SHA2 HASH");
				e.printStackTrace();
				flash().put("alert", new Alert("alert-danger", "Sorry. Something went wrong. Please try again.").toString());
				return redirect(routes.LoginController.editPassword());
			}
			loggedInUser.update();
			Logger.info("Password Changed Successfully By AppUser: "+loggedInUser.id);
			flash().put("alert", new Alert("alert-info", "Password has been changed. Please login with the new password.").toString());
			session().clear();
			return redirect(routes.LoginController.loginForm());
		}

	}

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

	public static boolean isLoggedInUserBlogAdmin() {
		final String role = session(Constants.LOGGED_IN_USER_ROLE);
		if(role.equalsIgnoreCase(Role.BLOG_ADMIN.toString())){
			return true;
		}
		return false;
	}
	/**
	 * Action to render the page to edit emailId and mobileNumber of loggedInUser
	 * GET	   /edit-login-details
	 */
	public static Result editLoginDetails(){
		
		return ok(views.html.editLoginDetails.render(LoginController.getLoggedInUser()));
	}
	/**
	 * Action to edit emailId and mobileNumber of loggedInUser
	 * POST /edit-login-details
	 */
	public static Result editLoginDetailsProcess(){
		Map<String, String[]> requestMap = request().body().asFormUrlEncoded();
		final Long appUserId = Long.parseLong(requestMap.get("appUserId")[0]);
		final AppUser loggedInUser = LoginController.getLoggedInUser();
		if(appUserId.longValue() != loggedInUser.id.longValue()){
			session().clear();
			return redirect(routes.LoginController.processLogout());
		}
		if(requestMap.get("email")[0]!=null && requestMap.get("email")[0].trim()!=""){
			loggedInUser.email = requestMap.get("email")[0];
		}
		if(requestMap.get("contactNo")[0]!=null && requestMap.get("contactNo")[0].trim()!=""){
			loggedInUser.mobileNumber = Long.parseLong(requestMap.get("contactNo")[0]);
		}
		loggedInUser.update();
		return redirect(routes.UserActions.dashboard());
	}

}
