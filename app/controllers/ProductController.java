package controllers;

import java.util.List;

<<<<<<< HEAD
import models.Pharmacist;
import models.Product;
import models.Role;
import models.mr.MedicalRepresentative;
import play.Logger;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;

public class ProductController extends Controller{

	public static Form<Product> productForm = Form.form(Product.class);

	public static Result productForm() {

		return ok(views.html.common.createProduct.render(productForm));
	}

	public static Result saveProduct(){
		//PharmaceuticalCompany pharmaceuticalCompany = new PharmaceuticalCompany();
		final MedicalRepresentative loggedInMr = LoginController.getLoggedInUser().getMedicalRepresentative();
		final Pharmacist loggedInPharmacist = LoginController.getLoggedInUser().getPharmacist();
		final Form<Product> productFilledForm = productForm.bindFromRequest();

		if(productFilledForm.hasErrors()) {
			Logger.info("bad request");
			return badRequest(views.html.common.createProduct.render(productFilledForm));
		}
		else {
			final Product filledProduct = productFilledForm.get();
			if(LoginController.getLoggedInUserRole().compareToIgnoreCase(Role.ADMIN_MR.toString()) == 0){
				Logger.info("pharmaceutical company is : "+loggedInMr.pharmaceuticalCompany.name);
				if(filledProduct.id == null){
					//Logger.info("Saving product for MR");.
					loggedInMr.pharmaceuticalCompany.productList.add(filledProduct);
					loggedInMr.pharmaceuticalCompany.update();
				}else{
					final Product product = Product.find.byId(filledProduct.id);
					product.update();
				}
				return redirect(routes.ProductController.displayProduct());
			}
			else{
				if(LoginController.getLoggedInUserRole().compareToIgnoreCase(Role.ADMIN_PHARMACIST.toString()) == 0){
					if(filledProduct.id == null){
						Logger.info("Saving product for Pharmacy");
						loggedInPharmacist.pharmacy.productList.add(filledProduct);
						loggedInPharmacist.pharmacy.update();
					}else{
						final Product product=Product.find.byId(filledProduct.id);
						product.update();
					}
				}
				return redirect(routes.UserActions.dashboard());
			}

		}


		//return redirect(routes.ProductController.viewProducts());
	}
	public static Result displayProduct() {
		final MedicalRepresentative loggedInMr = LoginController.getLoggedInUser().getMedicalRepresentative();
		final Pharmacist loggedInPharmacist = LoginController.getLoggedInUser().getPharmacist();
		final String role = LoginController.getLoggedInUserRole();
		if("ADMIN_MR".equals(role)){
			final List<Product> productList = Product.find.where().eq("pharmaceuticalCompany.id",loggedInMr.pharmaceuticalCompany.id).findList();
			return ok(views.html.common.products.render(productList));
		}
		if("ADMIN_PHARMACIST".equals(role)){
			final List<Product> productList = Product.find.where().eq("pharmacy.id",loggedInPharmacist.pharmacy.id).findList();
			return ok(views.html.common.products.render(productList));
		}
		return TODO;

	}

	public static Result editProduct(final Long id) {

		final Product product  = Product.find.byId(id);
		final Form<Product> editForm = ProductController.productForm.fill(product);
		return ok(views.html.common.createProduct.render(editForm));

	}

	public static Result removeProduct(final Long id) {

		final Product product  = Product.find.byId(id);
		product.delete();
=======
import models.MedicalRepresentative;
import models.PharmaceuticalCompany;
import models.Pharmacist;
import models.Product;
import play.Logger;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;

public class ProductController extends Controller{

	public static Form<Product> productForm = Form.form(Product.class);

	public static Result productForm() {

		return ok(views.html.common.createProduct.render(productForm));
	}

	public static Result saveProduct(){

		//PharmaceuticalCompany pharmaceuticalCompany = new PharmaceuticalCompany();
		final MedicalRepresentative loggedInMr = LoginController.getLoggedInUser().getMedicalRepresentative();
		final Pharmacist loggedInPharmacist = LoginController.getLoggedInUser().getPharmacist();
		final Form<Product> productFilledForm = productForm.bindFromRequest();

		if(productFilledForm.hasErrors()) {
			Logger.info("bad request");

			return badRequest(views.html.common.createProduct.render(productFilledForm));
		}
		else {
			final Product filledProduct= productFilledForm.get();
			final String role = LoginController.getLoggedInUserRole();
			if("ADMIN_MR".equals(role)){

				if(filledProduct.id == null){
					loggedInMr.pharmaceuticalCompany.productList.add(filledProduct);
					loggedInMr.pharmaceuticalCompany.save();
				}else{
					final Product product=Product.find.byId(filledProduct.id);
					product.delete();
					loggedInMr.pharmaceuticalCompany.productList.remove(product);
					loggedInMr.pharmaceuticalCompany.productList.add(filledProduct);
					loggedInMr.pharmaceuticalCompany.update();
				}
			}else{
				if("ADMIN_PHARMACIST".equals(role)){
					if(filledProduct.id == null){
						//Logger.info(""+loggedInPharmacist.pharmacy.productList);
						loggedInPharmacist.pharmacy.productsList.add(filledProduct);
						loggedInPharmacist.pharmacy.save();
					}else{
						final Product product=Product.find.byId(filledProduct.id);
						product.delete();
						loggedInPharmacist.pharmacy.productsList.remove(product);
						loggedInPharmacist.pharmacy.productsList.add(filledProduct);
						loggedInPharmacist.pharmacy.update();
					}
				}
			}

		}
		return TODO;
		//return redirect(routes.ProductController.displayProduct());
		//return redirect(routes.ProductController.viewProducts());
	}
	public static Result displayProduct() {
		final MedicalRepresentative loggedInMr = LoginController.getLoggedInUser().getMedicalRepresentative();
		//loggedInMr.pharmaceuticalCompany.id;
		final List<Product> productList = Product.find.where().eq("pharmaceutical_company_id",loggedInMr.pharmaceuticalCompany.id).findList();

		return ok(views.html.common.products.render(productList));
	}

	public static Result editProduct(final Long id) {

		final Product product  = Product.find.byId(id);
		final Form<Product> editForm = ProductController.productForm.fill(product);
		return ok(views.html.common.createProduct.render(editForm));

	}

	public static Result removeProduct(final Long id) {

		final Product product  = Product.find.byId(id);

>>>>>>> branch 'master' of http://pharmacy.bz/green-software/mednetwork.git
		//		productForm.fill(product);
		return redirect(routes.ProductController.displayProduct());

	}


}
