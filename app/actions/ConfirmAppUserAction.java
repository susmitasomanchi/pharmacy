package actions;

import models.AppUser;
import play.libs.F;
import play.mvc.Action;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.SimpleResult;
import controllers.LoginController;
import controllers.UserController;

public class ConfirmAppUserAction extends Action<Result>{

	@Override
	public F.Promise<SimpleResult> call(final Http.Context ctx) throws Throwable {
		final AppUser loggedInAppUser = LoginController.getLoggedInUser();
		if (!loggedInAppUser.emailConfirmed || !loggedInAppUser.mobileNumberConfirmed) {
			return F.Promise.pure((SimpleResult) UserController.confirmAppUserPage());
		}
		return this.delegate.call(ctx);
	}

}