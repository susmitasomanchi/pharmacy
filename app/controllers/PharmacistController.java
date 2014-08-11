package controllers;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import models.Address;
import models.Alert;
import models.Country;
import models.FileEntity;
import models.MasterProduct;
import models.Role;
import models.State;
import models.pharmacist.Batch;
import models.pharmacist.OrderLineItem;
import models.pharmacist.Pharmacist;
import models.pharmacist.Pharmacy;
import models.pharmacist.PharmacyPrescriptionInfo;
import models.pharmacist.PharmacyPrescriptionStatus;
import models.pharmacist.PharmacyProduct;
import models.pharmacist.ShowCasedProduct;

import org.joda.time.DateTime;

import play.Logger;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import play.mvc.Result;
import actions.BasicAuth;
import actions.ConfirmAppUser;
import beans.AddProductToInventoryBean;
import beans.PharmacyBean;

import com.avaje.ebean.Expr;
import com.google.common.io.Files;

@BasicAuth
public class PharmacistController extends Controller {

	public static Form<PharmacyProduct> pharmacyProductForm = Form.form(PharmacyProduct.class);
	public static Form<PharmacyBean> pharmacyBeanForm = Form.form(PharmacyBean.class);
	public static Form<Pharmacist> form = Form.form(Pharmacist.class);
	public static Form<Pharmacy> pharmacyForm = Form.form(Pharmacy.class);
	public static Form<OrderLineItem> orderLineItemForm = Form.form(OrderLineItem.class);
	public static Form<AddProductToInventoryBean> addProductToInventoryForm = Form.form(AddProductToInventoryBean.class);
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
				final FilePart image = request().body().asMultipartFormData().getFile("backgroundImage");
				if(image.getContentType().equalsIgnoreCase("image/bmp")||image.getContentType().equalsIgnoreCase("image/png")||image.getContentType().equalsIgnoreCase("image/jpeg")||image.getContentType().equalsIgnoreCase("image/gif")){
				pharmacy.backgroundImage = Files.toByteArray(image.getFile());
				}else{
					flash().put("alert", new Alert("alert-info", "Sorry. Images Should Be In The Following Formats .JPEG,.jpg,.png,.gif,.bmp").toString());
				}
			}
			if (request().body().asMultipartFormData().getFile("profileImage") != null) {
				final FilePart image = request().body().asMultipartFormData().getFile("profileImage");
				if(image.getContentType().equalsIgnoreCase("image/bmp")||image.getContentType().equalsIgnoreCase("image/png")||image.getContentType().equalsIgnoreCase("image/jpeg")||image.getContentType().equalsIgnoreCase("image/gif")){
				fileEntity.fileName = image.getFilename();
				fileEntity.mimeType = image.getContentType();
				fileEntity.byteContent = Files.toByteArray(image.getFile());
				pharmacy.profileImageList.add(fileEntity);
				}else{
					flash().put("alert", new Alert("alert-info", "Sorry. Images Should Be In The Following Formats .JPEG,.jpg,.png,.gif,.bmp").toString());
				}

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
				final String newSlug = requestMap.get("slugUrl")[0].trim();
				if(!newSlug.matches("^[a-z0-9\\-]+$")){
					flash().put("alert", new Alert("alert-danger", "Invalid charactrer provided in Url.").toString());
					return redirect(routes.UserActions.dashboard());
				}
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
	 * Action to display prescriptions of Pharmacy
	 * GET 	/pharmacy/prescriptions
	 */
	@ConfirmAppUser
	public static Result pharmacyPrescriptionList(final String status){
		final List<PharmacyPrescriptionStatus> statusList = new ArrayList<PharmacyPrescriptionStatus>();
		if(status == null || status.trim().isEmpty() || status.trim().compareToIgnoreCase("any")==0){
			for (final PharmacyPrescriptionStatus ppStatus : PharmacyPrescriptionStatus.values()) {
				statusList.add(ppStatus);
			}
		}
		else{
			statusList.add(PharmacyPrescriptionStatus.valueOf(status.trim().toUpperCase()));
		}
		final Pharmacy pharmacy = LoginController.getLoggedInUser().getPharmacist().pharmacy;
		final List<PharmacyPrescriptionInfo> pharmacyPrescriptionInfos =
				PharmacyPrescriptionInfo.find.where()
				.eq("pharmacy", pharmacy)
				.in("pharmacyPrescriptionStatus", statusList)
				.findList();
		return ok(views.html.pharmacist.viewPharmacyPrescriptionList.render(pharmacyPrescriptionInfos, status.trim().toUpperCase()));
	}

	/**
	 * @author lakshmi
	 * Action to display a shared prescription to the LoggedInPharmacist
	 * (Only the medicines part of prescription should be shown)
	 * GET 	/pharmacy/prescription/:pharmacyPrescriptionInfoId
	 */
	@ConfirmAppUser
	public static Result getPrescriptionDetails(final Long pharmacyPrescriptionInfoId){
		final PharmacyPrescriptionInfo pharmacyPrescriptionInfo = PharmacyPrescriptionInfo.find.byId(pharmacyPrescriptionInfoId);
		//server-side check
		if(pharmacyPrescriptionInfo.pharmacy.id.longValue() != LoginController.getLoggedInUser().getPharmacist().pharmacy.id.longValue()){
			return redirect(routes.LoginController.processLogout());
		}
		return ok(views.html.pharmacist.viewPrescriptionDetails.render(pharmacyPrescriptionInfo));
	}


	/*
	@ConfirmAppUser
	public static Result confirmPrescription(final Long pharmacyPrescriptionInfoId){
		final PharmacyPrescriptionInfo pharmacyPrescriptionInfo =PharmacyPrescriptionInfo.find.byId(pharmacyPrescriptionInfoId);
		pharmacyPrescriptionInfo.pharmacyPrescriptionStatus = PharmacyPrescriptionStatus.CONFIRMED;
		pharmacyPrescriptionInfo.update();
		final Pharmacy pharmacy = LoginController.getLoggedInUser().getPharmacist().pharmacy;
		final List<PharmacyPrescriptionInfo> pharmacyPrescriptionInfos = PharmacyPrescriptionInfo.find.where().eq("pharmacy", pharmacy).findList();
		return ok(views.html.pharmacist.viewPharmacyPrescriptionList.render(pharmacyPrescriptionInfos, ""));

	}
	 */


	/**
	 * @author lakshmi
	 * Action to change the status of loggedInPharmcy's prescription to SERVED
	 * GET 	/pharmacy/served-prescription/:pharmacyPrescriptionInfoId
	 */
	@ConfirmAppUser
	public static Result servedPrescription(final Long pharmacyPrescriptionInfoId){
		final PharmacyPrescriptionInfo pharmacyPrescriptionInfo = PharmacyPrescriptionInfo.find.byId(pharmacyPrescriptionInfoId);
		if((pharmacyPrescriptionInfo.pharmacy.id.longValue() != LoginController.getLoggedInUser().getPharmacist().pharmacy.id.longValue()) || (!LoginController.getLoggedInUser().role.equals(Role.ADMIN_PHARMACIST))){
			Logger.warn("COULD NOT VALIDATE LOGGED IN USER TO PERFORM THIS TASK");
			Logger.warn("update attempted for Pharmacy id: "+pharmacyPrescriptionInfo.pharmacy.id);
			Logger.warn("logged in AppUser: "+LoginController.getLoggedInUser().id);
			Logger.warn("logged in Pharmacist: "+LoginController.getLoggedInUser().getPharmacist().id);
			return redirect(routes.LoginController.processLogout());
		}
		pharmacyPrescriptionInfo.pharmacyPrescriptionStatus = PharmacyPrescriptionStatus.SERVED;
		pharmacyPrescriptionInfo.update();
		return redirect(routes.PharmacistController.pharmacyPrescriptionList("any"));
	}


	/**
	 * @author lakshmi
	 * Action to Display Todays Prescriptions requested by logged-in ADMIN_PHARMACIST
	 * GET 	/pharmacy/todays-prescriptions
	 */
	@ConfirmAppUser
	public static Result viewTodaysPrescriptions() {
		final Date now = new Date();
		final Calendar calendarFrom = Calendar.getInstance();
		calendarFrom.setTime(now);
		calendarFrom.set(Calendar.HOUR_OF_DAY, 0);
		calendarFrom.set(Calendar.MINUTE, 0);
		calendarFrom.set(Calendar.SECOND,0);
		calendarFrom.set(Calendar.MILLISECOND,0);

		final Calendar calendarTo = Calendar.getInstance();
		calendarTo.setTime(now);
		calendarTo.set(Calendar.HOUR_OF_DAY, 23);
		calendarTo.set(Calendar.MINUTE, 59);
		calendarTo.set(Calendar.SECOND,59);
		calendarTo.set(Calendar.MILLISECOND,999);

		final Pharmacy pharmacy = LoginController.getLoggedInUser().getPharmacist().pharmacy;
		final List<PharmacyPrescriptionInfo> pharmacyPrescriptionInfos =
				PharmacyPrescriptionInfo.find.where()
				.eq("pharmacy", pharmacy)
				.ge("sharedDate", calendarFrom.getTime())
				.le("sharedDate", calendarTo.getTime())
				.findList();
		return ok(views.html.pharmacist.viewPharmacyPrescriptionList.render(pharmacyPrescriptionInfos, ""));
	}
	/**
	 * @author lakshmi
	 * Action to Display Prescriptions between 2 dates as requested by logged-in ADMIN_PHARMACIST
	 * POST /pharmacy/from-and-to-date-prescriptions
	 */
	@ConfirmAppUser
	public static Result getFromAndToDatePrescriptions() {
		final Map<String, String[]> requestMap = request().body().asFormUrlEncoded();
		Date dateFrom = null,dateTo=null;
		if(requestMap.get("from") != null && (requestMap.get("from")[0].trim().compareToIgnoreCase("")!=0)){
			dateFrom = new DateTime(requestMap.get("from")[0]).toDate();
		}
		if(requestMap.get("to") != null && (requestMap.get("to")[0]).trim().compareToIgnoreCase("")!=0){
			dateTo = new DateTime(requestMap.get("to")[0]).toDate();
		}
		Logger.info("dateFrom===="+dateFrom+" DateTo==="+dateTo);
		if(dateFrom == null || dateTo == null){
			flash().put("alert", new Alert("alert-info", "Please provide both dates.").toString());
		}
		final Pharmacy pharmacy = LoginController.getLoggedInUser().getPharmacist().pharmacy;
		final List<PharmacyPrescriptionInfo> pharmacyPrescriptionInfos =
				PharmacyPrescriptionInfo.find.where()
				.eq("pharmacy", pharmacy)
				.ge("sharedDate", dateFrom)
				.le("sharedDate",dateTo)
				.findList();
		return ok(views.html.pharmacist.viewPharmacyPrescriptionList.render(pharmacyPrescriptionInfos,""));
	}

















	/**
	 * @author : lakshmi
	 * GET/pharmacy/add-pharmacy-product
	 * Action to render the form of new PharmacyProduct of Pharmacy
	 * of the loggedIn ADMIN_PHARMACIST
	 */
	public static Result addPharmacyProduct() {
		Logger.info("enter 1");
		return ok(views.html.pharmacist.createPharmacyProduct.render(pharmacyProductForm));
	}

	/**
	 * @author : lakshmi
	 * POST/pharmacy/add-pharmacy-product
	 * Action to add new PharmacyProduct of Pharmacy
	 * of the loggedIn ADMIN_PHARMACIST
	 */
	public static Result addPharmacyProductProcess() {
		final Pharmacy loggedInPharmacy = LoginController.getLoggedInUser().getPharmacist().pharmacy;

		final Form<PharmacyProduct> productFilledForm = pharmacyProductForm.bindFromRequest();

		if(productFilledForm.hasErrors()) {
			Logger.info("bad request");
			return badRequest(views.html.pharmacist.createPharmacyProduct.render(productFilledForm));
		}
		else {

			if(LoginController.getLoggedInUserRole().compareToIgnoreCase(Role.ADMIN_PHARMACIST.toString()) == 0){
				final PharmacyProduct filledProduct = productFilledForm.get();
				Logger.info("pharmaceutical company is : "+loggedInPharmacy.name);
				if(filledProduct.id == null){
					//Logger.info("Saving product for MR");
					loggedInPharmacy.pharmacyProductList.add(filledProduct);
					loggedInPharmacy.update();
				}else{
					final PharmacyProduct product = PharmacyProduct.find.byId(filledProduct.id);
					product.update();
				}
				Logger.info("product added list size is=="+loggedInPharmacy.pharmacyProductList.size());
			}


		}
		return redirect(routes.UserActions.dashboard());
	}

	/**
	 * @author : lakshmi
	 * GET/pharmacy/pharmacy-product-list
	 * Action to get all PharmacyProducts of Pharmacy
	 * of the loggedIn ADMIN_PHARMACIST
	 */
	public static Result getPharmacyProducts() {
		final Pharmacy pharmacy = LoginController.getLoggedInUser().getPharmacist().pharmacy;
		return ok(views.html.pharmacist.pharmacyProductList.render(pharmacy.pharmacyProductList,pharmacy.id));

	}
	/**
	 * @author : lakshmi
	 * POST/pharmacy/add-product-to-inventory
	 * Action to get all PharmacyProducts of Pharmacy
	 * of the loggedIn ADMIN_PHARMACIST
	 */
	public static Result addProductToInventory(final Long productId,final Long pharmacyId){
		return ok(views.html.pharmacist.addProductToInventory.render(addProductToInventoryForm,productId,pharmacyId));
	}

	/**
	 * @author : lakshmi
	 * POST/pharmacy/add-product-to-inventory
	 * Action to get all PharmacyProducts of Pharmacy
	 * of the loggedIn ADMIN_PHARMACIST
	 */
	public static Result addProductToInventoryProcess(final Long productId,final Long pharmacyId) {
		final Pharmacy pharmacy = Pharmacy.find.byId(pharmacyId);
		final PharmacyProduct pharmacyProduct = PharmacyProduct.find.byId(productId);
		/*pharmacy.inventoryList.add(pharmacyProduct);


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
		 */
		return redirect(routes.UserActions.dashboard());
		// return TODO;
	}

	/**
	 * @author : lakshmi
	 * GET/pharmacy/order
	 * Action to render the place_pharmacy_order
	 * of the loggedIn ADMIN_PHARMACIST
	 */
	/*public static Result placePharmacyOrder() {
		final Pharmacy pharmacy=LoginController.getLoggedInUser().getPharmacist().pharmacy;

		return ok(views.html.pharmacist.place_pharmacy_order.render(pharmacy));
	}*/
	/**
	 * @author : lakshmi
	 * POST/pharmacy/order
	 *  Action to svae the placed pharmacy order
	 * of the loggedIn ADMIN_PHARMACIST
	 */
	/*public static Result placePharmacyOrderProcess() {
		OrderLineItem orderLineItem = new OrderLineItem();
		Logger.info("inside 1");
		final Pharmacy pharmacy=LoginController.getLoggedInUser().getPharmacist().pharmacy;
		final String[] product = request().body().asFormUrlEncoded().get("productIds");
		final String[] quantity = request().body().asFormUrlEncoded().get("quantity");
		final String[] mrp = request().body().asFormUrlEncoded().get("mrp");
		for(int index=0;index<product.length;index++){
			orderLineItem.product = MasterProduct.find.byId(Long.parseLong(product[index]));
			orderLineItem.quantity = Double.parseDouble(quantity[index]);
			orderLineItem.price = Double.parseDouble(mrp[index]);
			if(orderLineItem.id == null){
				orderLineItem.save();
			}
			pharmacy.orderLineItemList.add(orderLineItem);
		}
		pharmacy.update();
		Logger.info("orderLineItemList size()=="+pharmacy.orderLineItemList.size());
		Logger.info("product size()=="+product.length);
		Logger.info("quantity size()=="+quantity.length);
		Logger.info("mrp size()=="+mrp.length);

		return ok(views.html.pharmacist.place_pharmacy_order.render(pharmacy));
	}
	 */



	/**
	 * @author lakshmi
	 * Action to add Prescription data to the Pharmacy
	 * GET /pharmacy/add-pharmacy-order/:pharmacyId/:prescriptionId
	 */
	/*public static Result addPharmacyOrderFromDoctor(Long pharmacyId,Long prescriptionId){
		Pharmacy pharmcy = Pharmacy.find.byId(pharmacyId);
		for (Prescription prescription : pharmcy.prescriptionList) {
			if(prescription.id == prescriptionId){
				PharmacyOrder pharmacyOrder = new PharmacyOrder();
				pharmacyOrder.prescription = prescription;
				pharmacyOrder.OrderedDate = new Date();
				pharmcy.pharmacyOrderList.add(pharmacyOrder);
			}
			pharmcy.update();
			Logger.info("size of list=="+pharmcy.pharmacyOrderList.size());
		}
		return ok();
	}*/














































































	/*
	public static Result searchForm() {
		final List<MasterProduct> products = MasterProduct.find.all();
		return ok(views.html.pharmacist.searchProduct.render(products));
	}

	 */
	public static Result searchProduct(final String search) {

		// final List<Patient> patients=Patient.find.where().eq("appUser.email",
		// "mitesh@greensoftware.in").findList();

		final List<MasterProduct> products = MasterProduct.find
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

		final MasterProduct product = MasterProduct.find.byId(id);
		final Form<MasterProduct> editForm = ProductController.productForm.fill(product);

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
		//		return ok(views.html.pharmacist.addProductToInventory.render(
		//				addProductForm, id));
		return TODO;
	}



	/*
	public static Result orderRecord() {

		final List<MasterProduct> products = MasterProduct.find.all();
		return ok(views.html.pharmacist.orderEntry.render(products));

	}

	 */

	public static Result pharmacyPlaceOrder(final Long id) {
		final Pharmacy pharmacy=Pharmacy.find.byId(id);
		//return ok(views.html.pharmacist.place_order.render(pharmacy.inventoryList,pharmacy));
		return ok();

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

			/*	final Inventory inventory = Inventory.find
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
			 */
			return ok();
		}

	}





























	/**
	 * @author lakshmi
	 * handling pharmacy orders
	 */

	public static Result orderManagement(){
		return ok();
	}


	/**
	 * @author lakshmi
	 * adding a ShowCasedProduct to the pharmacy showCasedList
	 * POST	/pharmacy/add-product-to-showcase
	 * @return
	 */

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
			}
			pharmacy.showCaseProductList.add(showCasedProduct);

			pharmacy.update();

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
