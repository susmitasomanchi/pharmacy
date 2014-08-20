package controllers;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.List;
import java.util.Map;
import java.util.Random;

import models.Alert;
import models.AppUser;
import models.Role;

import org.apache.commons.codec.binary.Base64;

import play.Logger;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import utils.Constants;
import utils.EmailService;
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


		//return ok(views.html.home.render(loginForm));
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
				return redirect(routes.Application.index());
			}
			if(appUsers.size() == 1) {
				if(!(appUsers.get(0).matchPassword(loginBean.password.trim()))){
					Logger.error("Invalid username/password");
					flash().put("alert", new Alert("alert-danger", "Sorry! Invalid Username/Password.").toString());
					return redirect(routes.Application.index());
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
			return redirect(routes.Application.index());
		}

	}


	//Forgot Password
	/**
	 *	Action to render a page where unauthorized user can enter his email id
	 *	GET	/forgot-password
	 */
	public static Result forgotPassword(){
		return ok(views.html.forgotPassword.render());
	}


	/**
	 *	Action to check whether an appUser exists with the provided email id
	 *	and to mail a link to change password page
	 *	POST	/forgot-password
	 */
	public static Result processForgotPassword(){
		final String email = request().body().asFormUrlEncoded().get("email")[0].trim();
		final List<AppUser> appUserList = AppUser.find.where().ieq("email", email).findList();
		if(appUserList.size() == 1){
			final AppUser appUser = appUserList.get(0);
			EmailService.sendForgotPasswordEmail(appUser);
		}
		flash().put("alert", new Alert("alert-info", "Instructions to reset your password has been sent to "+email+".").toString());
		return redirect(routes.Application.index());
	}


	/**
	 *	Action to render a page to appUser to change his forgotten password
	 *	GET	/forgot-reset-password/:userId/:forgotPasswordKey
	 */
	public static Result editForgotPassword(final Long userId, final String forgotPasswordKey){
		final AppUser appUser = AppUser.find.byId(userId);
		if(appUser.forgotPasswordConfirmationKey.compareTo(forgotPasswordKey)!=0){
			flash().put("alert", new Alert("alert-danger", "Your email could not be verified. Please try again.").toString());
			return redirect(routes.LoginController.processLogout());
		}
		return ok(views.html.editForgotPassword.render(appUser.id,appUser.forgotPasswordConfirmationKey));
	}


	/**
	 *	Action to reset AppUser's forgotten password and redirect to home page
	 *	POST	/forgot-reset-password
	 */
	public static Result updateForgotPassword(){
		final Map<String, String[]> requestMap = request().body().asFormUrlEncoded();
		final AppUser appUser = AppUser.find.byId(Long.parseLong(requestMap.get("appUserId")[0].trim()));
		final String forgotPasswordKey = requestMap.get("key")[0].trim();

		if(appUser.forgotPasswordConfirmationKey.compareTo(forgotPasswordKey) != 0){
			flash().put("alert", new Alert("alert-danger", "Your email could not be verified. Please try again.").toString());
			return redirect(routes.LoginController.processLogout());
		}
		final String newPassword = requestMap.get("newPassword")[0].trim();
		final String confirmNewPassword = requestMap.get("confirmNewPassword")[0].trim();

		if(newPassword.compareTo(confirmNewPassword) != 0){
			flash().put("alert", new Alert("alert-danger", "Sorry! Passwords do not match.").toString());
			return redirect(routes.LoginController.editForgotPassword(appUser.id, appUser.forgotPasswordConfirmationKey));
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

				appUser.salt = randomSalt;
				appUser.password = hashedPasswordWithSalt;

			} catch (final Exception e) {
				Logger.error("ERROR WHILE CREATING SHA2 HASH");
				e.printStackTrace();
				flash().put("alert", new Alert("alert-danger", "Sorry. Something went wrong. Please try again.").toString());
				return redirect(routes.LoginController.editForgotPassword(appUser.id, appUser.forgotPasswordConfirmationKey));
			}

			// Changing the key to make sure the forgot password link doesn't work again
			final Random random = new SecureRandom();
			final String randomString = new BigInteger(130,random).toString(32);
			appUser.forgotPasswordConfirmationKey = randomString;

			appUser.update();
			Logger.info("Forgotten Password Changed Successfully By AppUser: "+appUser.id);
			flash().put("alert", new Alert("alert-info", "Password has been changed. Please login with the new password.").toString());
			session().clear();
			return redirect(routes.Application.index());
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

	public static boolean isLoggedInMedNetworkAdmin() {
		final String role = session(Constants.LOGGED_IN_USER_ROLE);
		if(role.equalsIgnoreCase(Role.MEDNETWORK_ADMIN.toString())){
			return true;
		}
		return false;
	}

}
