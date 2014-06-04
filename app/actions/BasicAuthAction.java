package actions;

import models.Alert;
import play.libs.F;
import play.mvc.Action;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.SimpleResult;
import controllers.LoginController;

public class BasicAuthAction extends Action<Result> {
	public F.Promise<SimpleResult> call(Http.Context ctx) throws Throwable {
		if (LoginController.isLoggedIn() == false) {
			Controller.flash().put("alert", new Alert("alert-info", "You have been logged out. Please login to continue.").toString());
			return F.Promise.pure((SimpleResult) LoginController.loginForm());
        }
	    return delegate.call(ctx);
	  }
}