package controllers;


import models.Pharmacy;
import play.Logger;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;

public class PharmacyController extends Controller {

	public static Form<Pharmacy> form = Form.form(Pharmacy.class);


	public static Result form() {
		return ok(views.html.createPharmacy.render(form));
		//return TODO;
	}

	public static Result process() {
		final Form<Pharmacy> filledForm = form.bindFromRequest();


		if(filledForm.hasErrors()) {
			Logger.info("bad request");

			return badRequest(views.html.createPharmacy.render(filledForm));
		}
		else {
			final Pharmacy pharmacy= filledForm.get();

			if(pharmacy.id == null) {

				pharmacy.save();
			}
			else {

				pharmacy.update();
			}
		}
		return TODO;
		//return redirect(routes.UserController.list());

	}

}
