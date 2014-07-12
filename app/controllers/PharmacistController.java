package controllers;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.activation.MimetypesFileTypeMap;

import models.Alert;
import models.FileEntity;
import models.Product;
import models.pharmacist.Batch;
import models.pharmacist.Inventory;
import models.pharmacist.OrderLineItem;
import models.pharmacist.Pharmacist;
import models.pharmacist.Pharmacy;
import play.Logger;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import actions.BasicAuth;
import beans.AddProductToInventoryBean;
import beans.PharmacyBean;

import com.avaje.ebean.Expr;
import com.google.common.io.Files;

@BasicAuth
public class PharmacistController extends Controller {

	public static Form<PharmacyBean> pharmacyBean = Form
			.form(PharmacyBean.class);

	public static Form<Pharmacist> form = Form.form(Pharmacist.class);
	// public static Form<UserPreferenceBean> prefForm =
	// Form.form(UserPreferenceBean.class);
	// public static Form<Product> productForm = Form.form(Product.class);

	public static Form<Pharmacy> pharmacyForm = Form.form(Pharmacy.class);

	public static Form<OrderLineItem> orderLineItemForm = Form
			.form(OrderLineItem.class);

	public static Form<AddProductToInventoryBean> addProductForm = Form
			.form(AddProductToInventoryBean.class);

	public static Form<Batch> batchForm = Form.form(Batch.class);

	public static Result searchForm() {
		final List<Product> products = Product.find.all();
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

	/**
	 * @author:
	 * 
	 * @url:/edit-product/:id
	 * 
	 * editing a product details
	 */

	public static Result editProduct(final Long id) {

		final Product product = Product.find.byId(id);
		final Form<Product> editForm = ProductController.productForm
				.fill(product);

		// productForm.fill(product);
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
		final Pharmacy pharmacy = LoginController.getLoggedInUser()
				.getPharmacist().pharmacy;
		Logger.info("before==" + pharmacy.productList.size());
		final Product product = Product.find.byId(id);
		pharmacy.productList.remove(product);
		pharmacy.update();

		// productForm.fill(product);
		Logger.info("after==" + pharmacy.productList.size());
		return redirect(routes.ProductController.displayProduct());

	}

	public static Result addProductForm(final Long id) {

		Logger.info("" + id);
		return ok(views.html.pharmacist.addProductToInventory.render(
				addProductForm, id));
		// return TODO;
	}

	public static Result addToInventory(final Long id) {

		final Form<AddProductToInventoryBean> filledForm = addProductForm
				.bindFromRequest();

		if (filledForm.hasErrors()) {
			Logger.info("bad request");
			// return TODO;
			return badRequest(views.html.pharmacist.addProductToInventory
					.render(addProductForm, id));
		} else {
			final Batch batch = filledForm.get().toBatchEntity();
			final Inventory inventory = filledForm.get().toInventoryEntity();

			if ((batch.id == null) && (inventory.id == null)) {
				batch.save();
				inventory.save();

			} else {
				final Pharmacy pharmacy = LoginController.getLoggedInUser()
						.getPharmacist().pharmacy;
				inventory.batchList.add(batch);
				pharmacy.inventoryList.add(inventory);
				pharmacy.update();
			}
		}
		return redirect(routes.UserActions.dashboard());
		// return TODO;
	}

	public static Result orderRecord() {

		final List<Product> products = Product.find.all();
		return ok(views.html.pharmacist.orderEntry.render(products));

	}

	public static Result pharmacyPlaceOrder(final Long id) {
		final Pharmacy pharmacy=Pharmacy.find.byId(id);

		return ok(views.html.pharmacist.place_order.render(pharmacy.inventoryList,pharmacy));
	}

	public static Result placeProductOrder() {

		return ok(views.html.pharmacist.placeProductOrder
				.render(orderLineItemForm));
	}

	public static Result placeProductOrderProcess() {
		final Form<OrderLineItem> filledForm = orderLineItemForm
				.bindFromRequest();
		if (filledForm.hasErrors()) {
			return ok(views.html.pharmacist.placeProductOrder
					.render(filledForm));
		} else {
			final OrderLineItem orderLineItem = filledForm.get();
			// orderLineItem.save();

			final Inventory inventory = Inventory.find
					.where()
					.eq("product.medicineName",
							orderLineItem.product.medicineName).findUnique();
			Logger.info("before Order" + inventory.productQuantity);
			if (inventory.productQuantity > orderLineItem.quantity) {
				inventory.productQuantity = (int) (inventory.productQuantity - orderLineItem.quantity);
				inventory.update();
				Logger.info("after Order" + inventory.productQuantity);
				return ok();
			} else {
				return ok("product out of stock");
			}

		}

	}


	/*
	public static Result pharmacyProfile() {
		final Pharmacy pharmacy = LoginController.getLoggedInUser()
				.getPharmacist().pharmacy;
		return ok(views.html.pharmacist.pharmacy_profile.render(
				pharmacy.inventoryList, pharmacy));

	}

	 */


	/*
	 * @author : lakshmi
	 * 
	 * @url: /edit-pharmacy-details
	 * 
	 * descrition: getting the filled form to edit Pharmacy details
	 */
	public static Result editPharmacyDetails() {
		final Pharmacy pharmacy = LoginController.getLoggedInUser()
				.getPharmacist().pharmacy;
		final Form<PharmacyBean> filledForm = pharmacyBean.fill(pharmacy
				.toBean());
		return ok(views.html.pharmacist.pharmacyDetails.render(filledForm));
	}

	/**
	 * @author : lakshmi
	 * POST	/pharmacy/basic-update
	 * Action to update the basic details(like name & brief description etc) of Pharmacy
	 * of the loggedIn ADMIN_PHARMACIST
	 */

	public static Result pharmacyBasicUpdate() {
		try{
			final Map<String, String[]> requestMap = request().body().asFormUrlEncoded();
			final Pharmacy pharmacy = Pharmacy.find.byId(Long.parseLong(requestMap.get("pharmacyId")[0]));
			if(requestMap.get("name") != null && (requestMap.get("name")[0].trim().compareToIgnoreCase("")!=0)){
				pharmacy.name = requestMap.get("name")[0];
			}
			if(requestMap.get("description") != null && (requestMap.get("description")[0].trim().compareToIgnoreCase("")!=0)){
				pharmacy.description = requestMap.get("description")[0];
			}
			pharmacy.update();
		}
		catch (final Exception e){
			flash().put("alert", new Alert("alert-danger", "Sorry. Something went wrong. Please try again.").toString());
		}
		return redirect(routes.UserActions.dashboard());
	}



	/**
	 * @author : lakshmi
	 * POST	/pharmacy/address-update
	 * Action to update the address details of Pharmacy
	 * of the loggedIn ADMIN_PHARMACIST
	 */

	public static Result pharmacyAddressUpdate() {
		try{
			final Map<String, String[]> requestMap = request().body().asFormUrlEncoded();
			final Pharmacy pharmacy = Pharmacy.find.byId(Long.parseLong(requestMap.get("pharmacyId")[0]));

			if(requestMap.get("name") != null && (requestMap.get("name")[0].trim().compareToIgnoreCase("")!=0)){
				pharmacy.name = requestMap.get("name")[0];
			}
			if(requestMap.get("description") != null && (requestMap.get("description")[0].trim().compareToIgnoreCase("")!=0)){
				pharmacy.description = requestMap.get("description")[0];
			}

			pharmacy.update();

		}
		catch (final Exception e){
			flash().put("alert", new Alert("alert-danger", "Sorry. Something went wrong. Please try again.").toString());
		}
		return redirect(routes.UserActions.dashboard());
	}



	/*
	 * @author : lakshmi
	 * 
	 * @url: /upload-pharmacy-images
	 * 
	 * descrition:rendering to upload the Pharmacy Images
	 */
	public static Result uploadPharmacyImage() {
		return ok(views.html.pharmacist.uploadPharmacyImages
				.render(pharmacyForm));
	}

	/*
	 * @author : lakshmi
	 * 
	 * @url: /upload-pharmacy-images
	 * 
	 * descrition:uploading the Pharmacy Images process
	 */

	public static Result uploadPharmacyImageProcess() throws IOException {
		final Pharmacy pharmacy = LoginController.getLoggedInUser()
				.getPharmacist().pharmacy;
		final FileEntity fileEntity = new FileEntity();

		if (request().body().asMultipartFormData().getFile("backgroundImage") != null) {
			final File image = request().body().asMultipartFormData().getFile("backgroundImage").getFile();

			pharmacy.backgroundImage = Files.toByteArray(image);

		}

		if (request().body().asMultipartFormData().getFile("profileImage") != null) {
			final File image = request().body().asMultipartFormData().getFile("profileImage").getFile();
			fileEntity.fileName = image.getName();
			fileEntity.mimeType = new MimetypesFileTypeMap().getContentType(image);
			fileEntity.byteContent = Files.toByteArray(image);
			pharmacy.profileImageList.add(fileEntity);

		} else {
			Logger.info("BG IMAGE NULL");
		}
		pharmacy.update();
		//return ok(views.html.pharmacist.pharmacy_profile.render(pharmacy.inventoryList, pharmacy));

		return redirect(routes.UserActions.dashboard());

	}
	/*
	 * @author : lakshmi
	 * 
	 * @url: /pharmacy/get-image/:pharmacyId/:fileId
	 * 
	 * descrition: method for getting byte data as image
	 */

	public static Result getPharmacyImages(final Long pharmacyId,final Long imageId) {
		byte[] byteContent = null;
		if(imageId == 0){
			byteContent=Pharmacy.find.byId(pharmacyId).backgroundImage;
		}
		else{
			for (final FileEntity file : Pharmacy.find.byId(pharmacyId).profileImageList) {
				if(file.id == imageId){
					byteContent = file.byteContent;
				}
			}
		}
		return ok(byteContent).as("image/jpeg");

	}
	/*
	 * @author : lakshmi
	 * 
	 * @url:
	 * 
	 * descrition: handling pharmacy orders
	 */

	public static Result orderManagement(){
		return ok();
	}
	/*
	 * @author : lakshmi
	 * 
	 * @url:
	 * 
	 * descrition: removing pharmacy images from list
	 */
	public static Result removeImage(final Long pharmacyId,final Long imageId){
		final Pharmacy pharmacy = Pharmacy.find.byId(pharmacyId);
		Logger.info("before list size="+pharmacy.profileImageList.size());
		final FileEntity image = FileEntity.find.byId(imageId);

		pharmacy.profileImageList.remove(image);

		pharmacy.update();
		image.delete();
		Logger.info("after list size="+pharmacy.profileImageList.size());
		//		return ok(views.html.pharmacist.pharmacy_profile.render(pharmacy.inventoryList, pharmacy));
		return redirect(routes.UserActions.dashboard());
	}

	/*
	 * @author : lakshmi
	 * 
	 * @url:
	 * 
	 * descrition: pharmacy Description Updation
	 */
	public static Result pharmacyDescription(final Long id){
		final Map<String, String[]> s = request().body().asFormUrlEncoded();
		Logger.info("String........"+s);
		final Form<PharmacyBean> filledForm = pharmacyBean.bindFromRequest();
		Logger.info("id==="+Pharmacy.find.byId(id));
		final Pharmacy pharmacy = Pharmacy.find.byId(id);

		pharmacy.description = pharmacyForm.get().description;

		pharmacy.update();
		//		return ok(views.html.pharmacist.pharmacy_profile.render(pharmacy.inventoryList, pharmacy));
		return redirect(routes.UserActions.dashboard());

	}

}
