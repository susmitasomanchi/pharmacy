package controllers;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.activation.MimetypesFileTypeMap;

import models.Address;
import models.Alert;
import models.Country;
import models.FileEntity;
import models.Product;
import models.Role;
import models.State;
import models.pharmacist.Batch;
import models.pharmacist.Inventory;
import models.pharmacist.OrderLineItem;
import models.pharmacist.Pharmacist;
import models.pharmacist.Pharmacy;
import models.pharmacist.ShowCasedProduct;
import play.Logger;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import play.mvc.Result;
import actions.BasicAuth;
import beans.AddProductToInventoryBean;
import beans.PharmacyBean;

import com.avaje.ebean.Expr;
import com.google.common.io.Files;

@BasicAuth
public class PharmacistController extends Controller {

	public static Form<PharmacyBean> pharmacyBeanForm = Form.form(PharmacyBean.class);
	public static Form<Pharmacist> form = Form.form(Pharmacist.class);
	public static Form<Pharmacy> pharmacyForm = Form.form(Pharmacy.class);
	public static Form<OrderLineItem> orderLineItemForm = Form.form(OrderLineItem.class);
	public static Form<AddProductToInventoryBean> addProductForm = Form.form(AddProductToInventoryBean.class);
	public static Form<Batch> batchForm = Form.form(Batch.class);



	/**
	 * @author : lakshmi
	 * Action to upload Background and Profile Images of the Pharmacy of the LoggedIn ADMIN_PHARMACIST
	 * POST	/pharmacy/upload-pharmacy-images
	 */
	public static Result uploadPharmacyImageProcess()  {
		try{
			final Pharmacy pharmacy = Pharmacy.find.byId(Long.parseLong(request().body().asMultipartFormData().asFormUrlEncoded().get("pharmacyId")[0]));
			// Server side validation
			if((pharmacy.id.longValue() != LoginController.getLoggedInUser().getPharmacist().pharmacy.id.longValue()) || (!LoginController.getLoggedInUser().role.equals(Role.ADMIN_PHARMACIST))){
				Logger.warn("COULD NOT VALIDATE LOGGED IN USER TO PERFORM THIS TASK");
				Logger.warn("update attempted for Pharmacy id: "+pharmacy.id);
				Logger.warn("logged in AppUser: "+LoginController.getLoggedInUser().id);
				Logger.warn("logged in Pharmacist: "+LoginController.getLoggedInUser().getPharmacist().id);
				return redirect(routes.LoginController.processLogout());
			}
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
			return redirect(routes.UserActions.dashboard());
		}
		catch(final Exception e){
			Logger.error("ERROR WHILE UPLOADING IMAGES OF PHARMACY");
			e.printStackTrace();
			flash().put("alert", new Alert("alert-danger", "Sorry. Something went wrong. Please try again.").toString());
			return redirect(routes.UserActions.dashboard());
		}
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
			// Server side validation
			if((pharmacy.id.longValue() != LoginController.getLoggedInUser().getPharmacist().pharmacy.id.longValue()) || (!LoginController.getLoggedInUser().role.equals(Role.ADMIN_PHARMACIST))){
				Logger.warn("COULD NOT VALIDATE LOGGED IN USER TO PERFORM THIS TASK");
				Logger.warn("update attempted for Pharmacy id: "+pharmacy.id);
				Logger.warn("logged in AppUser: "+LoginController.getLoggedInUser().id);
				Logger.warn("logged in Pharmacist: "+LoginController.getLoggedInUser().getPharmacist().id);
				return redirect(routes.LoginController.processLogout());
			}
			if(requestMap.get("name") != null && (requestMap.get("name")[0].trim().compareToIgnoreCase("")!=0)){
				pharmacy.name = requestMap.get("name")[0];
			}
			if(requestMap.get("description") != null && (requestMap.get("description")[0].trim().compareToIgnoreCase("")!=0)){
				pharmacy.description = requestMap.get("description")[0];
			}
			if(requestMap.get("slugUrl") != null && !(requestMap.get("slugUrl")[0].trim().isEmpty())){
				if(requestMap.get("slugUrl")[0].trim().compareToIgnoreCase(pharmacy.slugUrl) != 0){
					final int availableSlug = Pharmacy.find.where().eq("slugUrl", requestMap.get("slugUrl")[0].trim()).findRowCount();
					if(availableSlug == 0){
						pharmacy.slugUrl = requestMap.get("slugUrl")[0].trim();
					}else{
						flash().put("alert", new Alert("alert-danger", "Sorry, Requested URL is not available.").toString());
						return redirect(routes.UserActions.dashboard());
					}
				}
			}
			pharmacy.update();
		}
		catch (final Exception e){
			Logger.error("ERROR WHILE UPDATING BASIC INFO OF PHARMACY");
			e.printStackTrace();
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

			// Server side validation
			if((pharmacy.id.longValue() != LoginController.getLoggedInUser().getPharmacist().pharmacy.id.longValue()) || (!LoginController.getLoggedInUser().role.equals(Role.ADMIN_PHARMACIST))){
				Logger.warn("COULD NOT VALIDATE LOGGED IN USER TO PERFORM THIS TASK");
				Logger.warn("update attempted for Pharmacy id: "+pharmacy.id);
				Logger.warn("logged in AppUser: "+LoginController.getLoggedInUser().id);
				Logger.warn("logged in Pharmacist: "+LoginController.getLoggedInUser().getPharmacist().id);
				return redirect(routes.LoginController.processLogout());
			}

			if(requestMap.get("contactPerson") != null && (requestMap.get("contactPerson")[0].trim().compareToIgnoreCase("")!=0)){
				pharmacy.contactPerson = requestMap.get("contactPerson")[0];
			}
			if(pharmacy.address == null){
				final Address address = new Address();
				address.save();
				pharmacy.address = address;
			}
			if(requestMap.get("addressLine1") != null && (requestMap.get("addressLine1")[0].trim().compareToIgnoreCase("")!=0)){
				pharmacy.address.addressLine1 = requestMap.get("addressLine1")[0];
			}
			if(requestMap.get("city") != null && (requestMap.get("city")[0].trim().compareToIgnoreCase("")!=0)){
				pharmacy.address.city = requestMap.get("city")[0];
			}
			if(requestMap.get("area") != null && (requestMap.get("area")[0].trim().compareToIgnoreCase("")!=0)){
				pharmacy.address.area = requestMap.get("area")[0];
			}
			if(requestMap.get("pincode") != null && (requestMap.get("pincode")[0].trim().compareToIgnoreCase("")!=0)){
				pharmacy.address.pinCode = requestMap.get("pincode")[0];
			}
			if(requestMap.get("state") != null && (requestMap.get("state")[0].trim().compareToIgnoreCase("")!=0)){
				pharmacy.address.state = Enum.valueOf(State.class,requestMap.get("state")[0]);
			}
			if(requestMap.get("country") != null && (requestMap.get("country")[0].trim().compareToIgnoreCase("")!=0)){
				pharmacy.address.country = Enum.valueOf(Country.class,requestMap.get("country")[0]);
			}
			if(requestMap.get("latitude") != null && (requestMap.get("latitude")[0].trim().compareToIgnoreCase("")!=0)){
				pharmacy.address.latitude = requestMap.get("latitude")[0];
			}
			if(requestMap.get("longitude") != null && (requestMap.get("longitude")[0].trim().compareToIgnoreCase("")!=0)){
				pharmacy.address.longitude = requestMap.get("longitude")[0];
			}
			if(requestMap.get("contactNo") != null && (requestMap.get("contactNo")[0].trim().compareToIgnoreCase("")!=0)){
				pharmacy.contactNumber = requestMap.get("contactNo")[0];
			}
			pharmacy.address.update();
			pharmacy.update();

		}
		catch (final Exception e){
			Logger.error("ERROR WHILE UPDATING ADDRESS OF PHARMACY");
			e.printStackTrace();
			flash().put("alert", new Alert("alert-danger", "Sorry. Something went wrong. Please try again.").toString());
		}
		return redirect(routes.UserActions.dashboard());
	}


	/**
	 * @author lakshmi
	 * GET /pharmacy/remove-image/:pharmacyId/:fileId
	 * Action to remove a profile image of the Pharmacy of the loggedIn ADMIN_PHARMACIST
	 */
	public static Result removeImage(final Long pharmacyId,final Long imageId){
		try{
			final Pharmacy pharmacy = Pharmacy.find.byId(pharmacyId);
			// Server side validation
			if((pharmacy.id.longValue() != LoginController.getLoggedInUser().getPharmacist().pharmacy.id.longValue()) || (!LoginController.getLoggedInUser().role.equals(Role.ADMIN_PHARMACIST))){
				Logger.warn("COULD NOT VALIDATE LOGGED IN USER TO PERFORM THIS TASK");
				Logger.warn("update attempted for Pharmacy id: "+pharmacy.id);
				Logger.warn("logged in AppUser: "+LoginController.getLoggedInUser().id);
				Logger.warn("logged in Pharmacist: "+LoginController.getLoggedInUser().getPharmacist().id);
				return redirect(routes.LoginController.processLogout());
			}
			Logger.info("before list size="+pharmacy.profileImageList.size());
			final FileEntity image = FileEntity.find.byId(imageId);
			pharmacy.profileImageList.remove(image);
			pharmacy.update();
			image.delete();
			return redirect(routes.UserActions.dashboard());
		}
		catch (final Exception e){
			Logger.error("ERROR WHILE REMOVING PROFILE IMAGE OF PHARMACY");
			e.printStackTrace();
			flash().put("alert", new Alert("alert-danger", "Sorry. Something went wrong. Please try again.").toString());
		}
		return redirect(routes.UserActions.dashboard());
	}




	/**
	 * @author lakshmi
	 * GET /pharmacy/get-image/:pharmacyId/:fileId
	 * Action to get byte data as image of Pharmacy's Background and Profile Images
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
	 * @url:/edit-product/:id
	 * editing a product details
	 */

	public static Result editProduct(final Long id) {

		final Product product = Product.find.byId(id);
		final Form<Product> editForm = ProductController.productForm.fill(product);

		// productForm.fill(product);
		return ok(views.html.common.createProduct.render(editForm));

	}

	/**
	 * @autor:
	 * @url:/remove-product/:id
	 * @description: removes the product from the pharmacy
	 */


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



	public static Result pharmacyProfile() {
		final Pharmacy pharmacy = LoginController.getLoggedInUser()
				.getPharmacist().pharmacy;
		return ok(views.html.pharmacist.pharmacy_profile.render(pharmacy));

	}




	/**
	 * @author : lakshmi
	 * @url: /edit-pharmacy-details
	 * descrition: getting the filled form to edit Pharmacy details
	 */
	public static Result editPharmacyDetails() {
		final Pharmacy pharmacy = LoginController.getLoggedInUser()
				.getPharmacist().pharmacy;
		final Form<PharmacyBean> filledForm = pharmacyBeanForm.fill(pharmacy
				.toBean());
		return ok(views.html.pharmacist.pharmacyDetails.render(filledForm));
	}








	/**
	 * @author : lakshmi
	 * 
	 * @url:
	 * 
	 * descrition: handling pharmacy orders
	 */

	public static Result orderManagement(){
		return ok();
	}


	/**
	 * @author lakshmi
	 * 
	 * adding a ShowCasedProduct to the pharmacy showCasedList
	 * 
	 * POST	/pharmacy/add-product-to-showcase
	 * @return
	 */

	/*	public static Result addShowCasedProduct(){
		try{
			final Map<String, String[]> requestMap = request().body().asMultipartFormData().asFormUrlEncoded();
			final Pharmacy pharmacy = Pharmacy.find.byId(Long.parseLong(requestMap.get("pharmacyId")[0]));
			Logger.info("map size"+requestMap);
			final ShowCasedProduct showCasedProduct = new ShowCasedProduct();
			if(requestMap.get("name") != null && (requestMap.get("name")[0].trim().compareToIgnoreCase("")!=0)){
				showCasedProduct.name = requestMap.get("name")[0];
			}
			if(requestMap.get("description") != null && (requestMap.get("description")[0].trim().compareToIgnoreCase("")!=0)){
				showCasedProduct.description = requestMap.get("description")[0];
			}
			if(requestMap.get("mrp") != null && (requestMap.get("mrp")[0].trim().compareToIgnoreCase("")!=0)){
				showCasedProduct.mrp = Double.parseDouble(requestMap.get("mrp")[0]);
			}
			if (request().body().asMultipartFormData().getFiles().size() != 0) {

				final List<Long> ids = new ArrayList<>();
				final MultipartFormData body = request().body().asMultipartFormData();
				final List<FilePart> listOfImages = body.getFiles();
				for (final FilePart filePart : listOfImages) {
					final FileEntity fileEntity = new FileEntity();
					if (filePart != null) {
						final File imageFile = filePart.getFile();
						fileEntity.fileName = filePart.getFilename();
						fileEntity.mimeType = filePart.getContentType();
						fileEntity.byteContent = Files.toByteArray(imageFile);
						fileEntity.save();
						ids.add(fileEntity.id);
						Logger.info(" fileEntity id==="+fileEntity.id);

					}
				}
				for (final Long id : ids) {
					final FileEntity fileEntity2 = FileEntity.find.byId(id);
					showCasedProduct.showcasedImagesList.add(id);
					//					showCasedProduct.update();

				}
				//				showCasedProduct.update();
				Logger.info("image list size() "+showCasedProduct.showcasedImagesList.size());



			}
			pharmacy.showCaseProductList.add(showCasedProduct);

			pharmacy.update();
			Logger.info("pharmacy ID=="+pharmacy.id);

			Logger.info("ShowcasedProduct list size=="+pharmacy.showCaseProductList.size());
			Logger.info("showcased product image list=="+pharmacy.showCaseProductList.get(0).showcasedImagesList.size());
			Logger.info("Showcase Product Id=="+showCasedProduct.id);
			Logger.info("*******************************************************************");
		}catch(final Exception e){
			e.printStackTrace();
			flash().put("alert", new Alert("alert-danger", "Sorry. Something went wrong. Please try again.").toString());
		}

		return redirect(routes.UserActions.dashboard());
		//return ok();

	}*/


	public static Result addShowCasedProduct(){
		try{
			final Map<String, String[]> requestMap = request().body().asMultipartFormData().asFormUrlEncoded();
			final Pharmacy pharmacy = Pharmacy.find.byId(Long.parseLong(requestMap.get("pharmacyId")[0]));
			Logger.info("map size"+requestMap);
			final ShowCasedProduct showCasedProduct = new ShowCasedProduct();
			if(requestMap.get("name") != null && (requestMap.get("name")[0].trim().compareToIgnoreCase("")!=0)){
				showCasedProduct.name = requestMap.get("name")[0];
			}
			if(requestMap.get("description") != null && (requestMap.get("description")[0].trim().compareToIgnoreCase("")!=0)){
				showCasedProduct.description = requestMap.get("description")[0];
			}
			if(requestMap.get("mrp") != null && (requestMap.get("mrp")[0].trim().compareToIgnoreCase("")!=0)){
				showCasedProduct.mrp = Double.parseDouble(requestMap.get("mrp")[0]);
			}
			if (request().body().asMultipartFormData().getFiles().size() != 0) {

				final List<Long> ids = new ArrayList<>();
				final MultipartFormData body = request().body().asMultipartFormData();
				final List<FilePart> listOfImages = body.getFiles();
				for (final FilePart filePart : listOfImages) {
					final FileEntity fileEntity = new FileEntity();
					if (filePart != null) {
						final File imageFile = filePart.getFile();
						fileEntity.fileName = filePart.getFilename();
						fileEntity.mimeType = filePart.getContentType();
						fileEntity.byteContent = Files.toByteArray(imageFile);
						fileEntity.save();
						ids.add(fileEntity.id);
						Logger.info(" fileEntity id==="+fileEntity.id);

					}
				}
				for (final Long id : ids) {
					final FileEntity fileEntity2 = FileEntity.find.byId(id);
					showCasedProduct.showcasedImagesList.add(fileEntity2);
					//					showCasedProduct.update();

				}
				//				showCasedProduct.update();
				Logger.info("image list size() "+showCasedProduct.showcasedImagesList.size());



			}
			pharmacy.showCaseProductList.add(showCasedProduct);

			pharmacy.update();
			Logger.info("pharmacy ID=="+pharmacy.id);

			Logger.info("ShowcasedProduct list size=="+pharmacy.showCaseProductList.size());
			Logger.info("showcased product image list=="+pharmacy.showCaseProductList.get(0).showcasedImagesList.size());
			Logger.info("Showcase Product Id=="+showCasedProduct.id);
			Logger.info("*******************************************************************");
		}catch(final Exception e){
			e.printStackTrace();
			flash().put("alert", new Alert("alert-danger", "Sorry. Something went wrong. Please try again.").toString());
		}

		return redirect(routes.UserActions.dashboard());
		//return ok();

	}






	/**
	 * @author lakshmi
	 * 
	 * remove ShowcasedProduct from the pharmacy showcasedProductList
	 * 
	 * GET		/pharmacy/remove-product-from-showcase/:id
	 */
	public static Result removeProductFromShowcaseList(final Long showCaseProductId){
		Logger.info("inside");
		final Pharmacy pharmacy = LoginController.getLoggedInUser().getPharmacist().pharmacy;
		final ShowCasedProduct showCasedProduct  = ShowCasedProduct.find.byId(showCaseProductId);
		//		showCasedProduct.delete();
		pharmacy.showCaseProductList.remove(showCasedProduct);

		Logger.info("size()==="+pharmacy.showCaseProductList.size());
		pharmacy.update();
		showCasedProduct.delete();
		return redirect(routes.UserActions.dashboard());

	}
	public static Result displayImage(){
		final Pharmacy pharmacy = LoginController.getLoggedInUser().getPharmacist().pharmacy;
		Logger.info("PharmacyId=="+pharmacy.id);
		final ShowCasedProduct showCasedProduct = pharmacy.showCaseProductList.get(0);
		Logger.info("showCasedProduct id=="+showCasedProduct.id);
		Logger.info("showcasedImagesList size()=="+showCasedProduct.showcasedImagesList.size());
		return ok(views.html.pharmacist.displayImage.render(showCasedProduct));
	}

	public static Result getShowPharmacyImages(final Long showcaseId,final Long imageId) {
		Logger.info("showcaseId=="+showcaseId);
		Logger.info("image Id=="+imageId);
		byte[] byteContent = null;
		for (final FileEntity file : ShowCasedProduct.find.byId(showcaseId).showcasedImagesList) {
			if(file.id == imageId){

				byteContent = FileEntity.find.byId(file.id).byteContent;
			}
		}

		return ok(byteContent).as("image/jpeg");

	}


	public static Result staticFavPharmacies(){

		return ok(views.html.pharmacist.static_fav_pharmacies.render());
	}


}
