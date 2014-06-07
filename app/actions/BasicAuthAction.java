package actions;

import models.Alert;
import play.libs.F;
import play.mvc.Action;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.SimpleResult;
import controllers.Application;
import controllers.LoginController;

public class BasicAuthAction extends Action<Result> {
	@Override
	public F.Promise<SimpleResult> call(final Http.Context ctx) throws Throwable {
		if (LoginController.isLoggedIn() == false) {
			Controller.flash().put("alert", new Alert("alert-info", "You have been logged out. Please login to continue.").toString());
			return F.Promise.pure((SimpleResult) Application.index());
		}
		return this.delegate.call(ctx);
	}
}