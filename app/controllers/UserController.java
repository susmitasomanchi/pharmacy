
/*****

THIS IS AN AUTO GENERATED CODE
PLEASE DO NOT MODIFY IT BY HAND

 *****/
package controllers;

import java.util.ArrayList;
import java.util.List;

import models.Alert;
import models.AppUser;
import models.KUnit;
import models.Notification;
import models.Role;
import models.UserPreference;
import play.Logger;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import actions.BasicAuth;
import beans.AppUserBean;
import beans.UserPreferenceBean;

@BasicAuth
public class UserController extends Controller {

	public static Form<AppUserBean> form = Form.form(AppUserBean.class);
	public static Form<UserPreferenceBean> prefForm = Form.form(UserPreferenceBean.class);

	public static Result form() {
		return ok(views.html.mod.userForm.render(form));
	}

	public static Result process() {
		final Form<AppUserBean> filledForm = form.bindFromRequest();
		if(filledForm.hasErrors()) {
			return badRequest(views.html.mod.userForm.render(filledForm));
		}
		else {
			final AppUserBean userBean = filledForm.get();

			if(userBean.id == null) {
				final AppUser user = userBean.toEntity();
				final UserPreference pref = new UserPreference();
				pref.save();
				user.userPreference = pref;
				user.save();
			}
			else {
				userBean.toEntity().update();
			}
		}
		return redirect(routes.UserController.list());
	}

	public static Result list() {
		List<AppUser> userList = new ArrayList<AppUser>();
		final AppUser appUser = LoginController.getLoggedInUser();
		if(appUser.role == Role.COORDINATOR){
			userList = appUser.location.userList();
		}
		else{
			userList = AppUser.find.all();
		}

		return ok(views.html.mod.userList.render(userList));
	}

	public static Result show(final Long id) {
		final AppUser appUser = AppUser.find.byId(id);
		return ok(views.html.gen.userShow.render(appUser));
	}

	public static Result edit(final Long id) {
		final AppUser appUser = AppUser.find.byId(id);
		final Form<AppUserBean> filledForm = form.fill(appUser.toBean());
		return ok(views.html.mod.userForm.render(filledForm));
	}

	public static Result delete(final Long id) {
		final AppUser appUser = AppUser.find.byId(id);
		appUser.delete();
		return redirect(routes.UserController.list());
	}

	public static Result search(final String searchStr) {
		return TODO;
	}


	public static Result preferences(){
		final AppUser appUser = LoginController.getLoggedInUser();
		final Form<UserPreferenceBean> filledForm;
		if(appUser.userPreference != null){
			filledForm = prefForm.fill(appUser.userPreference.toBean());
		}
		else{
			filledForm = prefForm.fill(new UserPreferenceBean());
		}
		return ok(views.html.mod.preferences.render(filledForm));
	}

	public static Result processPreferences(){

		final Form<UserPreferenceBean> filledForm = prefForm.bindFromRequest();
		if(filledForm.hasErrors()) {
			return badRequest(views.html.mod.preferences.render(filledForm));
		}
		else {
			final UserPreferenceBean prefBean = filledForm.get();
			final AppUser appUser = LoginController.getLoggedInUser();

			final UserPreference userPref = prefBean.toEntity();
			if(prefBean.id == null) {
				userPref.save();
			} else {
				userPref.update();
			}
			appUser.userPreference = userPref;
			appUser.update();
		}


		flash().put("alert", new Alert("alert-success", "Preferences Saved Successfuly.").toString());
		return redirect(routes.UserController.preferences());
	}


	public static Result removeNotification(final Long nId){
		final AppUser loggedInUser = LoginController.getLoggedInUser();
		final Notification n = Notification.find.byId(nId);
		loggedInUser.notificationList.remove(n);
		loggedInUser.update();
		n.delete();
		return ok();
	}

	public static Result addToFavourites(final Long kuId){
		try{
			final AppUser user = LoginController.getLoggedInUser();
			user.userPreference.favKUList.add(KUnit.find.byId(kuId));
			user.userPreference.update();
			return ok("0");
		}
		catch(final Exception e){
			Logger.info("Error while adding to favourites");
			e.printStackTrace();
			return ok("-1");
		}
	}

}

