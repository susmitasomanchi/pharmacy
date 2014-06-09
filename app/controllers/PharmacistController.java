package controllers;


import actions.BasicAuth;
import models.AppUser;
import models.Pharmacist;
import play.Logger;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;

@BasicAuth
public class PharmacistController extends Controller{

	public static Form<Pharmacist> form = Form.form(Pharmacist.class);
	//public static Form<UserPreferenceBean> prefForm = Form.form(UserPreferenceBean.class);

	public static Result form() {
		return ok(views.html.createPharmacist.render(form));
		//return TODO;
	}

	public static Result process() {
		final Form<Pharmacist> filledForm = form.bindFromRequest();
		if(filledForm.hasErrors()) {
			Logger.info("bad request");

			return badRequest(views.html.createPharmacist.render(filledForm));
		}
		else {
			final Pharmacist pharmacist= filledForm.get();

			if(pharmacist.id == null) {
				final AppUser appUser=new AppUser();
				pharmacist.appUser=appUser;
				appUser.save();
				pharmacist.save();

			}
			else {
				pharmacist.update();
			}
		}
		return ok("User Created");
		//return redirect(routes.UserController.list());
	}
}
