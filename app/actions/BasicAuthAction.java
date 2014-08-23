package actions;

import models.Alert;
import play.libs.F;
import play.mvc.Action;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.SimpleResult;
import utils.Constants;
import controllers.Application;
import controllers.BlogController;
import controllers.LoginController;

public class BasicAuthAction extends Action<Result> {
	@Override
	public F.Promise<SimpleResult> call(final Http.Context ctx) throws Throwable {

		if(ctx.request().uri().startsWith("/blog/admin")){
			if(!LoginController.isLoggedIn()){
				return F.Promise.pure((SimpleResult) BlogController.blogHome());
			}
			if (!LoginController.isLoggedInUserBlogAdmin()) {
				Controller.flash().put("alert", new Alert("alert-info", "Blogger have been logged out. Please login to continue.").toString());
				return F.Promise.pure((SimpleResult) BlogController.blogHome());
			}
		}
		if (LoginController.isLoggedIn() == false) {
			Controller.flash().put("alert", new Alert("alert-info", "You have been logged out. Please login to continue.").toString());
			Controller.session(Constants.URL_AFTER_LOGIN, ctx.request().uri());
			return F.Promise.pure((SimpleResult) Application.index());
		}
		return this.delegate.call(ctx);
	}
}
