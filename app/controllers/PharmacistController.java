package controllers;


import java.util.List;

import actions.BasicAuth;
import models.AppUser;
import models.Batch;
import models.Inventory;
import models.Pharmacist;
import models.Pharmacy;
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

	public static Form<Pharmacy> pharmacyForm = Form.form(Pharmacy.class);

	public static Form<Inventory> inventoryForm = Form.form(Inventory.class);

	public static Form<Batch> batchForm = Form.form(Batch.class);


	public static Result form() {
		return ok(views.html.pharmacist.createPharmacist.render(form));
		//return TODO;
	}

	public static Result process() {
		final Form<Pharmacist> filledForm = form.bindFromRequest();
		if(filledForm.hasErrors()) {
			Logger.info("bad request");

			return badRequest(views.html.pharmacist.createPharmacist.render(filledForm));
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



	public static Result pharmacyForm() {
		return ok(views.html.pharmacist.createPharmacy.render(pharmacyForm));
		//return TODO;
	}

	public static Result pharmacyProcess() {
		final Form<Pharmacy> pharmacyFilledForm = pharmacyForm.bindFromRequest();


		if(pharmacyFilledForm.hasErrors()) {
			Logger.info("bad request");

			return badRequest(views.html.pharmacist.createPharmacy.render(pharmacyFilledForm));
		}
		else {
			final Pharmacy pharmacy= pharmacyFilledForm.get();

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
		return ok(views.html.pharmacist.products.render(products, productForm));
	}




	public static Result editProduct(final Long id) {

		final Product product  = Product.find.byId(id);
		final Form<Product> editForm = productForm.fill(product);

		//		productForm.fill(product);
		return ok(views.html.pharmacist.createProduct.render(editForm));




	}

	public static Result addProductForm() {

		return ok(views.html.pharmacist.addProductToInventory.render(inventoryForm, batchForm ));
	}

	public static Result addToInventory() {

		final Form<Inventory> inventoryFilledForm = inventoryForm.bindFromRequest();

		final Form<Batch> batchFilledForm = batchForm.bindFromRequest();

		if(inventoryFilledForm.hasErrors() && batchFilledForm.hasErrors()) {
			Logger.info("bad request");

			return badRequest(views.html.pharmacist.addProductToInventory.render(inventoryForm, batchForm ));
		}
		else {
			final Inventory inventory= inventoryFilledForm.get();
			final Batch batch=batchFilledForm.get();

			if(inventory.id == null && batch.id == null) {
				inventory.save();
				batch.save();
			}
			else {
				inventory.update();
				batch.update();
			}

		}
		//return ok("User Created");
		//return TODO;
		//return ok(views.html.dashboard.render(appUser));
		return redirect(routes.UserActions.dashboard());
		//return TODO;


	}

}

