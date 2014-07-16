package controllers;

import java.util.List;
import java.util.Map;
import models.Alert;
import models.Product;
import models.Role;
import models.mr.MedicalRepresentative;
import models.pharmacist.Pharmacist;
import models.pharmacist.Pharmacy;
import models.pharmacist.PharmacyProduct;
import play.Logger;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;

public class ProductController extends Controller{

	public static Form<Product> productForm = Form.form(Product.class);
	/*
	 * @autor:
	 * 
	 * @url: /new-product
	 * 
	 * @description: rendering to the createProduct scala to create a new product
	 */
	public static Result productForm() {

		return ok(views.html.common.createProduct.render(productForm));
	}

	/*
	 * @autor:
	 * 
	 * @url: /new-product/save
	 * 
	 * @description: saving or updating a product pharmacy and pharmaceuticalCompany based on incoming id
	 */
	public static Result saveProduct(){
		//PharmaceuticalCompany pharmaceuticalCompany = new PharmaceuticalCompany();
		final MedicalRepresentative loggedInMr = LoginController.getLoggedInUser().getMedicalRepresentative();
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
					//Logger.info("Saving product for MR");
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
					try{
						final Map<String, String[]> requestMap = request().body().asFormUrlEncoded();
						final Pharmacy pharmacy = LoginController.getLoggedInUser().getPharmacist().pharmacy;
						final PharmacyProduct product =new PharmacyProduct();
						/*if(requestMap.get("productId")[0] == null){
							product = new Product();
							product.save();
						}
						else{
							product = Product.find.byId(Long.parseLong(requestMap.get("productId")[0]));
						}*/
						Logger.info("map size"+requestMap.toString());
						if(requestMap.get("medicineName") != null && (requestMap.get("medicineName")[0].trim().compareToIgnoreCase("")!=0)){
							product.medicineName = requestMap.get("medicineName")[0];
						}
						if(requestMap.get("brandName") != null && (requestMap.get("brandName")[0].trim().compareToIgnoreCase("")!=0)){
							product.brandName = requestMap.get("brandName")[0];
						}
						if(requestMap.get("salt") != null && (requestMap.get("salt")[0].trim().compareToIgnoreCase("")!=0)){
							product.salt = requestMap.get("salt")[0];
						}
						if(requestMap.get("strength") != null && (requestMap.get("strength")[0].trim().compareToIgnoreCase("")!=0)){
							product.strength = requestMap.get("strength")[0];
						}
						if(requestMap.get("typeOfMedicine") != null && (requestMap.get("typeOfMedicine")[0].trim().compareToIgnoreCase("")!=0)){
							product.typeOfMedicine = requestMap.get("typeOfMedicine")[0];
						}
						if(requestMap.get("description") != null && (requestMap.get("description")[0].trim().compareToIgnoreCase("")!=0)){
							product.description = requestMap.get("description")[0];
						}
						if(requestMap.get("unitsPerPack") != null && (requestMap.get("unitsPerPack")[0].trim().compareToIgnoreCase("")!=0)){
							product.unitsPerPack = Long.parseLong(requestMap.get("unitsPerPack")[0]);
						}
						if(requestMap.get("fullName") != null && (requestMap.get("fullName")[0].trim().compareToIgnoreCase("")!=0)){
							product.fullName = requestMap.get("fullName")[0];
						}
						//product.save();
						pharmacy.pharmacyProductList.add(product);
						pharmacy.update();
					}
					catch (final Exception e){
						e.printStackTrace();
						flash().put("alert", new Alert("alert-danger", "Sorry. Something went wrong. Please try again.").toString());
					}
				}
				return redirect(routes.UserActions.dashboard());
			}

		}


		//return redirect(routes.ProductController.viewProducts());
	}

	/*
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
			final List<Product> productList = Product.find.where().eq("pharmaceuticalCompany.id",loggedInMr.pharmaceuticalCompany.id).findList();
			return ok(views.html.common.products.render(productList));
		}
		if("ADMIN_PHARMACIST".equals(role)){
			final List<PharmacyProduct> productList = loggedInPharmacist.pharmacy.pharmacyProductList;
			//return ok(views.html.common.products.render(productList));
			return ok();
		}
		return TODO;

	}





}
