package actions;

import models.Alert;
import models.AppUser;
import play.libs.F;
import play.mvc.Action;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.SimpleResult;
import controllers.Application;
import controllers.LoginController;
import controllers.UserController;

public class ConfirmAppUserAction extends Action<Result>{

	@Override
	public F.Promise<SimpleResult> call(final Http.Context ctx) throws Throwable {
		if(!LoginController.isLoggedIn()){
			Controller.flash().put("alert", new Alert("alert-info", "Please login to continue.").toString());
			return F.Promise.pure((SimpleResult) Application.index());
		}
		final AppUser loggedInAppUser = LoginController.getLoggedInUser();
		if (!loggedInAppUser.emailConfirmed || !loggedInAppUser.mobileNumberConfirmed) {
			return F.Promise.pure((SimpleResult) UserController.confirmAppUserPage());
		}
		return this.delegate.call(ctx);
	}

}