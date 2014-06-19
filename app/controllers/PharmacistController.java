package controllers;


import java.util.List;

import beans.AddProductToInventoryBean;

import com.avaje.ebean.Expr;

import actions.BasicAuth;
import models.AppUser;
import models.Batch;
import models.Inventory;
import models.Patient;
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

	public static Form<AddProductToInventoryBean> addProductForm = Form.form(AddProductToInventoryBean.class);


	public static Long  Pid;





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



	public static Result searchForm()
	{
		final List<Product> products=Product.find.all();
		return ok(views.html.pharmacist.searchProduct.render(products));
	}





	public static Result searchProduct(final String search) {

		// final List<Patient> patients=Patient.find.where().eq("appUser.email",
		// "mitesh@greensoftware.in").findList();

		final List<Product> products = Product.find
				.where()
				.or(Expr.like("medicineName", search + "%"),
						Expr.like("typeOfMedicine", search + "%")).findList();

		return ok(products.toString());
	}




	public static Result editProduct(final Long id) {

		final Product product  = Product.find.byId(id);
		final Form<Product> editForm = productForm.fill(product);

		//		productForm.fill(product);
		return ok(views.html.pharmacist.createProduct.render(editForm));




	}




	public static Result addProductForm(final Long id) {

		Pid=id;
		System.out.println("hjmdhsjhsjhfjjf"+id);
		return ok(views.html.pharmacist.addProductToInventory.render(addProductForm));
		//return TODO;
	}





	public static Result addToInventory() {

		final Form<AddProductToInventoryBean> filledForm = addProductForm.bindFromRequest();

		if(filledForm.hasErrors()) {
			Logger.info("bad request");

			return badRequest(views.html.pharmacist.addProductToInventory.render(addProductForm));
		}
		/*if(batchFilledForm.hasErrors()) {
			Logger.info("bad request");

			return badRequest(views.html.pharmacist.addProductToInventory.render(batchForm));
		}*/
		else {
			final Batch batch=filledForm.get().toBatchEntity();
			final Inventory inventory=filledForm.get().toInventoryEntity();
			batch.product.id=Pid;
			if((batch.id==null) && (inventory.id==null)){
				batch.save();
				inventory.save();

			}
			else{
				batch.update();
				inventory.update();
			}

		}
		//return ok("User Created");
		//return TODO;
		//return ok(views.html.dashboard.render(appUser));
		return redirect(routes.UserActions.dashboard());
		//return TODO;


	}

}

