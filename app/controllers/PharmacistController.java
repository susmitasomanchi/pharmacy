package controllers;


import java.io.File;
import java.util.List;

import models.Address;
import models.Product;
import models.pharmacist.Batch;
import models.pharmacist.Inventory;
import models.pharmacist.OrderLineItem;
import models.pharmacist.Pharmacist;
import models.pharmacist.Pharmacy;
import play.Logger;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Http.MultipartFormData;
import play.mvc.Result;
import actions.BasicAuth;
import beans.AddProductToInventoryBean;
import beans.PharmacyBean;

import com.avaje.ebean.Expr;
import com.google.common.io.Files;

@BasicAuth
public class PharmacistController extends Controller{

	public static Form<PharmacyBean> pharmacyBean = Form.form(PharmacyBean.class);

	public static Form<Pharmacist> form = Form.form(Pharmacist.class);
	//public static Form<UserPreferenceBean> prefForm = Form.form(UserPreferenceBean.class);
	//public static Form<Product> productForm = Form.form(Product.class);

	public static Form<Pharmacy> pharmacyForm = Form.form(Pharmacy.class);

	public static Form<OrderLineItem> orderLineItemForm = Form.form(OrderLineItem.class);

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
	/*
	 * @author:
	 * 
	 * @url:/edit-product/:id
	 * 
	 * @description: editing a product details
	 */


	public static Result editProduct(final Long id) {

		final Product product  = Product.find.byId(id);
		final Form<Product> editForm = ProductController.productForm.fill(product);

		//		productForm.fill(product);
		return ok(views.html.common.createProduct.render(editForm));




	}

	/*
	 * @autor:
	 * 
	 * @url:/remove-product/:id
	 * 
	 * @description: removes the product from the pharmacy
	 */

	public static Result removeProduct(final Long id) {
		final Pharmacy pharmacy=LoginController.getLoggedInUser().getPharmacist().pharmacy;
		Logger.info("before=="+pharmacy.productList.size());
		final Product product  = Product.find.byId(id);
		pharmacy.productList.remove(product);
		pharmacy.update();

		//		productForm.fill(product);
		Logger.info("after=="+pharmacy.productList.size());
		return redirect(routes.ProductController.displayProduct());

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
		else {
			final Batch batch=filledForm.get().toBatchEntity();
			final Inventory inventory=filledForm.get().toInventoryEntity();

			if((batch.id==null) && (inventory.id==null)){
				batch.save();
				inventory.save();

			}
			else{
				final Pharmacy pharmacy=LoginController.getLoggedInUser().getPharmacist().pharmacy;
				inventory.batchList.add(batch);
				pharmacy.inventoryList.add(inventory);
				pharmacy.update();
			}
		}
		return redirect(routes.UserActions.dashboard());
		//return TODO;
	}


	public static Result orderRecord() {

		final List<Product> products=Product.find.all();
		return ok(views.html.pharmacist.orderEntry.render(products));

	}


	public static Result pharmacyPlaceOrder() {

		return ok(views.html.pharmacist.place_order.render());
	}

	public static Result placeProductOrder()
	{


		return ok(views.html.pharmacist.placeProductOrder.render(orderLineItemForm));
	}

	public static Result placeProductOrderProcess()
	{
		final Form<OrderLineItem> filledForm = orderLineItemForm.bindFromRequest();
		if (filledForm.hasErrors()) {
			return ok(views.html.pharmacist.placeProductOrder.render(filledForm));
		} else {
			final OrderLineItem orderLineItem = filledForm.get();
			//orderLineItem.save();

			final Inventory inventory=Inventory.find.where().eq("product.medicineName", orderLineItem.product.medicineName).findUnique();
			Logger.info("before Order"+inventory.productQuantity);
			if(inventory.productQuantity>orderLineItem.quantity){
				inventory.productQuantity=(int) (inventory.productQuantity-orderLineItem.quantity);
				inventory.update();
				Logger.info("after Order"+inventory.productQuantity);
				return ok();
			}
			else{
				return ok("product out of stock");
			}


		}

	}

	public static Result pharmacyProfile() {
		final Pharmacy pharmacy=LoginController.getLoggedInUser().getPharmacist().pharmacy;
		return ok(views.html.pharmacist.pharmacy_profile.render(pharmacy.inventoryList,pharmacy));

	}



	/*
	 * @author : lakshmi
	 * 
	 * @url:
	 * 
	 * descrition: getting the filled form to edit Pharmacy details
	 */
	public static Result editPharmacyDetails() {
		final Pharmacy pharmacy = LoginController.getLoggedInUser().getPharmacist().pharmacy;
		final Form<PharmacyBean> filledForm = pharmacyBean.fill(pharmacy.toBean());
		return ok(views.html.pharmacist.pharmacyDetails.render(filledForm));
	}

	/*
	 * @author : lakshmi
	 * 
	 * @url:
	 * 
	 * descrition: updating the Pharmacy with edit information
	 */

	public static Result editPharmacyDetailsprocess() {
		final Form<PharmacyBean> filledForm = pharmacyBean.bindFromRequest();
		if (filledForm.hasErrors()) {
			return badRequest(views.html.pharmacist.pharmacyDetails.render(filledForm));
		}
		else {

			Logger.info("Bean L1: "+filledForm.get().addrressLine1);

			final Pharmacy pharmacy = filledForm.get().toPharmacy();
			final Address address=new Address();

			if(pharmacy.address.id == null){
				pharmacy.address.save();
			}
			else{
				pharmacy.address.update();
			}

			//Logger.info(""+pharmacy.address.toString());

			Logger.info("Address"+pharmacy.address.addrressLine1);
			Logger.info("id: "+pharmacy.id);
			pharmacy.update();
		}
		return ok("updated successfully");
	}

	/*
	 * @author : lakshmi
	 * 
	 * @url:
	 * 
	 * descrition:rendering to upload the Pharmacy Images
	 */
	public static Result uploadPharmacyImage(){
		return ok(views.html.pharmacist.uploadPharmacyImages.render(pharmacyForm));
	}

	/*
	 * @author : lakshmi
	 * 
	 * @url:
	 * 
	 * descrition:uploading the Pharmacy Images process
	 */

	public static Result uploadPharmacyImageProcess() {
		final Pharmacy pharmacy = LoginController.getLoggedInUser().getPharmacist().pharmacy;
		final MultipartFormData body = request().body().asMultipartFormData();
		if(request().body().asMultipartFormData().getFile("profileImage") != null){
			final File image = request().body().asMultipartFormData().getFile("profileImage").getFile();
			try{
				pharmacy.profileImage = Files.toByteArray(image);
				Logger.info("Pharmacy image uploaded");
			}
			catch (final Exception e){
				e.printStackTrace();
			}
		}

		if(request().body().asMultipartFormData().getFile("backgroundImage").getFile() != null){
			Logger.info("bg IMAGE TEST1");
			final File image = request().body().asMultipartFormData().getFile("backgroundImage").getFile();
			try{
				Logger.info("bg IMAGE 2");
				//final byte[] image1 = Files.toByteArray(image);
				final byte[] ba = Files.toByteArray(image);
				Logger.info("23232323");

				pharmacy.backgroundImageList.add(ba);

				Logger.info("Pharmacy Back ground image uploaded");
				Logger.info("list size"+pharmacy.backgroundImageList.size());
			}
			catch (final Exception e){
				Logger.info("bg IMAGE 3");
				e.printStackTrace();
			}
		}
		else{
			Logger.info("BG IMAGE NULL");
		}

		pharmacy.update();

		return ok(views.html.pharmacist.displayImage.render(pharmacy));

	}

	public static Result getProfileImage(final Long id) {

		return ok(Pharmacy.find.byId(id).profileImage).as("image/jpeg");

	}
	public static Result getBackgroundImage(final Long id,final Integer index) {

		return ok(	Pharmacy.find.byId(id).backgroundImageList.get(index)).as("image/jpeg");


	}





	private static Status ok(final List<Byte[]> backgroundImageList) {
		// TODO Auto-generated method stub
		return null;
	}

}







