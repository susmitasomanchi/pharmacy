package controllers;


import models.AppUser;
import models.Pharmacist;
import play.Logger;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;

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
			final Pharmacist pharmaciest= filledForm.get();

			if(pharmaciest.id == null) {
				pharmaciest.save();
			}
			else {
				pharmaciest.update();
			}
		}
		return TODO;
		//return redirect(routes.UserController.list());
	}
}
