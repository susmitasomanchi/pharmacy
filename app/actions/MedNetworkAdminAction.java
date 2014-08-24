package actions;

import models.Alert;
import models.Role;
import play.libs.F;
import play.mvc.Action;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.SimpleResult;
import utils.Constants;
import controllers.Application;
import controllers.LoginController;

public class MedNetworkAdminAction extends Action<Result> {
	@Override
	public F.Promise<SimpleResult> call(final Http.Context ctx) throws Throwable {
		if (LoginController.isLoggedIn() && LoginController.getLoggedInUser().role.equals(Role.MEDNETWORK_ADMIN)) {
			return this.delegate.call(ctx);
		}
		else{
			Controller.flash().put("alert", new Alert("alert-info", "You have been logged out. Please login to continue.").toString());
			Controller.session(Constants.URL_AFTER_LOGIN, ctx.request().uri());
			return F.Promise.pure((SimpleResult) Application.index());
		}
	}
}
