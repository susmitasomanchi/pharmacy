package controllers;


import java.util.List;

import actions.BasicAuth;
import models.AppUser;
import models.Pharmacist;
import models.Product;
import play.Logger;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;

@BasicAuth
public class PharmacistController extends Controller{

	public static Form<Pharmacist> form = Form.form(Pharmacist.class);
	//public static Form<UserPreferenceBean> prefForm = Form.form(UserPreferenceBean.class);
	public static Form<Product> productForm = Form.form(Product.class);

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




	public static Result productForm() {

		return ok(views.html.pharmacist.createProduct.render(productForm));
		//return TODO;
	}

	public static Result saveProduct() {
		final Form<Product> productFilledForm = productForm.bindFromRequest();
		if(productFilledForm.hasErrors()) {
			Logger.info("bad request");

			return badRequest(views.html.pharmacist.createProduct.render(productFilledForm));
		}
		else {
			final Product product= productFilledForm.get();

			if(product.id == null) {
				product.save();

			}
			else {
				product.update();
			}

		}
		//return ok("User Created");
		//return TODO;
		//return ok(views.html.dashboard.render(appUser));
		return redirect(routes.UserActions.dashboard());
	}

	public static Result displayProducts() {
		final List<Product> products=Product.find.all();
		return ok(views.html.pharmacist.products.render(products));
	}
}
