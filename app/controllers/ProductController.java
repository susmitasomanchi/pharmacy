package controllers;

import java.util.List;

import models.Product;
import models.Role;
import models.mr.MedicalRepresentative;
import models.mr.PharmaceuticalProduct;
import models.pharmacist.Pharmacist;
import play.Logger;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;

public class ProductController extends Controller{

	public static Form<PharmaceuticalProduct> pharmaceuticalProductForm = Form.form(PharmaceuticalProduct.class);

	public static Form<Product> pharmacyProductForm = Form.form(Product.class);
	/**
	 * @autor:
	 * 
	 * @url: GET    /new-product
	 * 
	 * @description: rendering to the createProduct scala to create a new product
	 */
	public static Result pharmaceuticalProductForm() {

		return ok(views.html.common.createPharmaceuticalProduct.render(pharmaceuticalProductForm));
	}

	/**
	 * @autor:
	 * @url: POST   /new-product
	 * @description: saving or updating a product pharmacy and pharmaceuticalCompany based on incoming id
	 */
	public static Result savePharmceuticalProduct(){
		//PharmaceuticalCompany pharmaceuticalCompany = new PharmaceuticalCompany();
		final MedicalRepresentative loggedInMr = LoginController.getLoggedInUser().getMedicalRepresentative();

		final Form<PharmaceuticalProduct> productFilledForm = pharmaceuticalProductForm.bindFromRequest();

		if(productFilledForm.hasErrors()) {
			Logger.info("bad request");
			return badRequest(views.html.common.createPharmaceuticalProduct.render(productFilledForm));
		}
		else {

			if(LoginController.getLoggedInUserRole().compareToIgnoreCase(Role.ADMIN_MR.toString()) == 0){
				final PharmaceuticalProduct filledProduct = productFilledForm.get();
				Logger.info("pharmaceutical company is : "+loggedInMr.pharmaceuticalCompany.name);
				if(filledProduct.id == null){
					//Logger.info("Saving product for MR");
					loggedInMr.pharmaceuticalCompany.productList.add(filledProduct);
					loggedInMr.pharmaceuticalCompany.update();
				}else{
					final Product product = Product.find.byId(filledProduct.id);
					product.update();
				}
				return redirect(routes.ProductController.displayProduct());
			}
			return TODO;

		}

	}

	public static Result pharmacyProductForm() {

		return ok(views.html.common.createPharmacyProduct.render(pharmacyProductForm));
	}

	public static Result savePharmacyProduct(){
		final Pharmacist loggedInPharmacist = LoginController.getLoggedInUser().getPharmacist();

		final Form<Product> productFilledForm = pharmacyProductForm.bindFromRequest();

		if(productFilledForm.hasErrors()) {
			Logger.info("bad request");
			return badRequest(views.html.common.createPharmacyProduct.render(productFilledForm));
		}else{
			if(LoginController.getLoggedInUserRole().compareToIgnoreCase(Role.ADMIN_PHARMACIST.toString()) == 0){
				final Product pharmacyFilledProduct = productFilledForm.get();
				Logger.info("pharma company is : "+loggedInPharmacist.pharmacy);
				if(pharmacyFilledProduct.id == null){
					Logger.info("Saving product for Pharmacy");
					loggedInPharmacist.pharmacy.productList.add(pharmacyFilledProduct);
					loggedInPharmacist.pharmacy.update();
				}else{
					final Product product=Product.find.byId(pharmacyFilledProduct.id);
					product.update();
				}
			}
			return redirect(routes.UserActions.dashboard());


		}
	}

	/**
	 * @autor:
	 * 
	 * @url: /view-products
	 * 
	 * @description: displays all the products in the pharmacy OR pharmaceuticalCompany based on id
	 */


	public static Result displayProduct() {
		final MedicalRepresentative loggedInMr = LoginController.getLoggedInUser().getMedicalRepresentative();
		final Pharmacist loggedInPharmacist = LoginController.getLoggedInUser().getPharmacist();
		final String role = LoginController.getLoggedInUserRole();
		if("ADMIN_MR".equals(role)){
			final List<PharmaceuticalProduct> productList = PharmaceuticalProduct.find.where().eq("pharmaceuticalCompany.id",loggedInMr.pharmaceuticalCompany.id).findList();
			return ok(views.html.common.pharmaceuticalProducts.render(productList));
		}
		if("ADMIN_PHARMACIST".equals(role)){
			final List<Product> productList = loggedInPharmacist.pharmacy.productList;
			return ok(views.html.common.pharmacyProducts.render(productList));
		}
		return TODO;

	}





}
