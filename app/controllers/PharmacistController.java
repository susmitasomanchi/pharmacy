package controllers;


import java.util.List;

import models.Batch;
import models.Inventory;
import models.Pharmacist;
import models.Pharmacy;
import models.Product;
import play.Logger;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import actions.BasicAuth;
import beans.AddProductToInventoryBean;

import com.avaje.ebean.Expr;

@BasicAuth
public class PharmacistController extends Controller{

	public static Form<Pharmacist> form = Form.form(Pharmacist.class);
	//public static Form<UserPreferenceBean> prefForm = Form.form(UserPreferenceBean.class);
	//public static Form<Product> productForm = Form.form(Product.class);

	public static Form<Pharmacy> pharmacyForm = Form.form(Pharmacy.class);

	public static Form<AddProductToInventoryBean> addProductForm = Form.form(AddProductToInventoryBean.class);



	public static Form<Batch> batchForm = Form.form(Batch.class);





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
		final Form<Product> editForm = ProductController.productForm.fill(product);

		//		productForm.fill(product);
		return ok(views.html.common.createProduct.render(editForm));




	}




	public static Result addProductForm(final Long id) {

		Logger.info(""+id);
		return ok(views.html.pharmacist.addProductToInventory.render(addProductForm,id));
		//return TODO;
	}





	public static Result addToInventory(final Long id) {

		final Form<AddProductToInventoryBean> filledForm = addProductForm.bindFromRequest();

		if(filledForm.hasErrors()) {
			Logger.info("bad request");
			//return TODO;
			return badRequest(views.html.pharmacist.addProductToInventory.render(addProductForm, id));
		}
		/*if(batchFilledForm.hasErrors()) {
			Logger.info("bad request");

			return badRequest(views.html.pharmacist.addProductToInventory.render(batchForm));
		}*/
		else {
			final Batch batch=filledForm.get().toBatchEntity();
			final Inventory inventory=filledForm.get().toInventoryEntity();

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


	public static Result orderRecord() {

		final List<Product> products=Product.find.all();
		return ok(views.html.pharmacist.orderEntry.render(products));

	}
	
	public static Result pharmacyProfile() {
		
		return ok(views.html.pharmacist.pharmacy_profile.render());
	}
	
	public static Result pharmacyPlaceOrder() {
		
		return ok(views.html.pharmacist.place_order.render());
	}

}

