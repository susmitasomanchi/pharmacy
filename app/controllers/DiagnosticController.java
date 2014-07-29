package controllers;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.activation.MimetypesFileTypeMap;

import models.Address;
import models.Alert;
import models.FileEntity;
import models.Role;
import models.State;
import models.diagnostic.DiagnosticCentre;
import models.diagnostic.DiagnosticOrder;
import models.diagnostic.DiagnosticOrderStatus;
import models.diagnostic.DiagnosticReport;
import models.diagnostic.DiagnosticReportStatus;
import models.diagnostic.DiagnosticTest;
import models.diagnostic.DiagnosticTestLineItem;
import models.diagnostic.ShowCasedService;
import models.doctor.Prescription;
import models.patient.Patient;

import org.apache.commons.io.FileUtils;

import play.Logger;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import play.mvc.Result;
import actions.BasicAuth;
import beans.DiagnosticBean;

import com.google.common.io.Files;

@BasicAuth
public class DiagnosticController extends Controller {
	public static Form<DiagnosticBean> diagnosticBeanForm = Form.form(DiagnosticBean.class);
	public static Form<DiagnosticCentre> diagnosticForm = Form.form(DiagnosticCentre.class);
	public static Form<DiagnosticTest> diagnosticTestForm = Form.form(DiagnosticTest.class);
	public static Form<DiagnosticReport> diagReport = Form.form(DiagnosticReport.class);

	/**
	 * @author lakshmi
	 * Action for uploading DiagnosticCentre backgroundImage and profile
	 * Images of of DiagnosticCentre of the loggedIn ADMIN_DIAGREP
	 * POST	/diagnostic/upload-diagnostic-images
	 */
	public static Result uploadDiagnosticImageProcess() {
		try{
			final DiagnosticCentre diagnosticCentre = DiagnosticCentre.find.byId(Long.parseLong(request().body().asMultipartFormData().asFormUrlEncoded().get("diagnosticId")[0]));
			// Server side validation
			if((diagnosticCentre.id.longValue() != LoginController.getLoggedInUser().getDiagnosticRepresentative().diagnosticCentre.id.longValue()) || (!LoginController.getLoggedInUser().role.equals(Role.ADMIN_DIAGREP))){
				Logger.warn("COULD NOT VALIDATE LOGGED IN USER TO PERFORM THIS TASK");
				Logger.warn("update attempted for DiagnosticCentre id: "+diagnosticCentre.id);
				Logger.warn("logged in AppUser: "+LoginController.getLoggedInUser().id);
				Logger.warn("logged in DiagnosticRep: "+LoginController.getLoggedInUser().getDiagnosticRepresentative().id);
				return redirect(routes.LoginController.processLogout());
			}

			if (request().body().asMultipartFormData().getFile("backgroundImage") != null) {
				final File image = request().body().asMultipartFormData().getFile("backgroundImage").getFile();
				diagnosticCentre.backgroudImage = Files.toByteArray(image);
				diagnosticCentre.update();
			}

			if (request().body().asMultipartFormData().getFile("profileImage") != null) {
				final FileEntity fileEntity = new FileEntity();
				final File image = request().body().asMultipartFormData().getFile("profileImage").getFile();
				fileEntity.fileName = image.getName();
				fileEntity.mimeType = new MimetypesFileTypeMap().getContentType(image);
				fileEntity.byteContent = Files.toByteArray(image);
				fileEntity.save();
				final Long imageId=fileEntity.id;
				diagnosticCentre.profileImageList.add(FileEntity.find.byId(imageId));
				diagnosticCentre.update();
			} else {
				Logger.info("BG IMAGE NULL");
			}
		}catch(final Exception e){
			e.printStackTrace();
			Logger.error("");
			flash().put("alert", new Alert("alert-danger", "Sorry. Something went wrong. Please try again.").toString());
		}

		//return ok(views.html.pharmacist.pharmacy_profile.render(pharmacy.inventoryList, pharmacy));

		return redirect(routes.UserActions.dashboard());

	}
	/**
	 * @author lakshmi
	 *  Action to get byteData as image of DiagnosticCentre	 *
	 * GET/diagnostic/get-image/:diagnosticId/:fileId
	 */
	public static Result getDiagnosticImages(final Long diagnosticId,final Long imageId) {
		byte[] byteContent = null;
		if(imageId == 0){
			byteContent=DiagnosticCentre.find.byId(diagnosticId).backgroudImage;
		}
		else{
			for (final FileEntity file : DiagnosticCentre.find.byId(diagnosticId).profileImageList) {
				if(file.id == imageId){
					byteContent = file.byteContent;
				}
			}
		}

		return ok(byteContent).as("image/jpeg");

	}

	/**
	 * @author : lakshmi
	 * POST	/diagnostic/basic-update
	 * Action to update the basic details(like name & brief description etc) of DiagnosticCentre
	 * of the loggedIn ADMIN_DIAGREP
	 */

	public static Result diagnosticBasicUpdate() {
		try{
			final Map<String, String[]> requestMap = request().body().asFormUrlEncoded();
			final DiagnosticCentre diagnosticCentre = DiagnosticCentre.find.byId(Long.parseLong(requestMap.get("diagnosticId")[0]));
			// Server side validation
			if((diagnosticCentre.id.longValue() != LoginController.getLoggedInUser().getDiagnosticRepresentative().diagnosticCentre.id.longValue()) || (!LoginController.getLoggedInUser().role.equals(Role.ADMIN_DIAGREP))){
				Logger.warn("COULD NOT VALIDATE LOGGED IN USER TO PERFORM THIS TASK");
				Logger.warn("update attempted for DiagnosticCentre id: "+diagnosticCentre.id);
				Logger.warn("logged in AppUser: "+LoginController.getLoggedInUser().id);
				Logger.warn("logged in DiagnosticRep: "+LoginController.getLoggedInUser().getDiagnosticRepresentative().id);
				return redirect(routes.LoginController.processLogout());
			}
			if(requestMap.get("name") != null && (requestMap.get("name")[0].trim().compareToIgnoreCase("")!=0)){
				diagnosticCentre.name = requestMap.get("name")[0].trim();
			}
			if(requestMap.get("description") != null && (requestMap.get("description")[0].trim().compareToIgnoreCase("")!=0)){
				diagnosticCentre.description = requestMap.get("description")[0].trim();
			}
			if(requestMap.get("slugUrl") != null && (requestMap.get("slugUrl")[0].trim().compareToIgnoreCase("")!=0)){
				final String newSlug = requestMap.get("slugUrl")[0].trim();
				if(!newSlug.matches("^[a-z0-9\\-]+$")){
					flash().put("alert", new Alert("alert-danger", "Invalid charactrer provided in Url.").toString());
					return redirect(routes.UserActions.dashboard());
				}
				if(requestMap.get("slugUrl")[0].trim().compareToIgnoreCase(diagnosticCentre.slugUrl) != 0){
					final int availableSlug = DiagnosticCentre.find.where().eq("slugUrl", requestMap.get("slugUrl")[0].trim()).findRowCount();
					if(availableSlug == 0){
						diagnosticCentre.slugUrl = requestMap.get("slugUrl")[0].trim();
					}else{
						flash().put("alert", new Alert("alert-danger", "Sorry, Requested URL is not available.").toString());
						return redirect(routes.UserActions.dashboard());
					}
				}
			}
			diagnosticCentre.update();
		}
		catch (final Exception e){
			flash().put("alert", new Alert("alert-danger", "Sorry. Something went wrong. Please try again.").toString());
		}
		return redirect(routes.UserActions.dashboard());
	}


	/**
	 * @author : lakshmi
	 * POST	/diagnostic/address-update
	 * Action to update the address details of DiagnosticCentre
	 * of the loggedIn ADMIN_DIAGREP
	 */

	public static Result diagnosticAddressUpdate() {

		try{
			final Map<String, String[]> requestMap = request().body().asFormUrlEncoded();
			final DiagnosticCentre diagnosticCentre = DiagnosticCentre.find.byId(Long.parseLong(requestMap.get("diagnosticId")[0]));
			Logger.info("map size"+requestMap.toString());
			if(diagnosticCentre.address == null){
				final Address address = new Address();
				address.save();
				diagnosticCentre.address = address;
			}
			if(requestMap.get("contactPerson") != null && (requestMap.get("contactPerson")[0].trim().compareToIgnoreCase("")!=0)){
				diagnosticCentre.contactPerson = requestMap.get("contactPerson")[0];
			}
			if(requestMap.get("addressLine1") != null && (requestMap.get("addressLine1")[0].trim().compareToIgnoreCase("")!=0)){
				diagnosticCentre.address.addressLine1 = requestMap.get("addressLine1")[0];
			}
			if(requestMap.get("city") != null && (requestMap.get("city")[0].trim().compareToIgnoreCase("")!=0)){
				diagnosticCentre.address.city = requestMap.get("city")[0];
			}
			if(requestMap.get("area") != null && (requestMap.get("area")[0].trim().compareToIgnoreCase("")!=0)){
				diagnosticCentre.address.area = requestMap.get("area")[0];
			}
			if(requestMap.get("pincode") != null && (requestMap.get("pincode")[0].trim().compareToIgnoreCase("")!=0)){
				diagnosticCentre.address.pinCode = requestMap.get("pincode")[0];
			}
			if(requestMap.get("state") != null && (requestMap.get("state")[0].trim().compareToIgnoreCase("")!=0)){
				diagnosticCentre.address.state = Enum.valueOf(State.class,requestMap.get("state")[0]);
			}
			if(requestMap.get("latitude") != null && (requestMap.get("latitude")[0].trim().compareToIgnoreCase("")!=0)){
				diagnosticCentre.address.latitude = requestMap.get("latitude")[0];
			}
			if(requestMap.get("longitude") != null && (requestMap.get("longitude")[0].trim().compareToIgnoreCase("")!=0)){
				diagnosticCentre.address.longitude = requestMap.get("longitude")[0];
			}
			if(requestMap.get("contactNo") != null && (requestMap.get("contactNo")[0].trim().compareToIgnoreCase("")!=0)){
				diagnosticCentre.mobileNo = requestMap.get("contactNo")[0];
			}
			diagnosticCentre.address.update();
			diagnosticCentre.update();

		}
		catch (final Exception e){
			e.printStackTrace();
			flash().put("alert", new Alert("alert-danger", "Sorry. Something went wrong. Please try again.").toString());
		}
		return redirect(routes.UserActions.dashboard());
	}

	/**
	 * @author lakshmi
	 * Action to remove profileImage of DiagnosticCentre of the loggedIn ADMIN_DIAGREP	 *
	 * GET/diagnostic/remove-image/:diagnosticId/:fileId
	 */
	public static Result removeDiagnosticImage(final Long diagnosticId,final Long imageId){
		final DiagnosticCentre diagnosticCentre = DiagnosticCentre.find.byId(diagnosticId);
		Logger.info("before list size="+diagnosticCentre.profileImageList.size());
		final FileEntity image = FileEntity.find.byId(imageId);

		diagnosticCentre.profileImageList.remove(image);

		diagnosticCentre.update();
		//image.delete();
		Logger.info("after list size="+diagnosticCentre.profileImageList.size());
		//		return ok(views.html.pharmacist.pharmacy_profile.render(pharmacy.inventoryList, pharmacy));
		return redirect(routes.UserActions.dashboard());
	}

	/**
	 * @author lakshmi
	 * Action to get byteData as image of ShowcasedService Of DiagnosticCentre
	 * GET/diagnostic/get-image/:diagnosticId/:fileId
	 */
	public static Result getShowCasedImages(final Long diagnosticServiceId,final Long diagnosticServiceImageID) {
		byte[] byteContent = null;
		if(diagnosticServiceId != 0 && diagnosticServiceImageID != 0){
			for (final FileEntity file : ShowCasedService.find.byId(diagnosticServiceId).showcasedImagesList) {
				if(file.id == diagnosticServiceImageID){
					byteContent = file.byteContent;
				}
			}
		}
		return ok(byteContent).as("image/jpeg");

	}


	/**
	 * @author : lakshmi	  
	 * GET/diagnostic/place-order	 
	 * Action to persist the orders placed by Prescription
	 */
	public static Result receive() {

		DiagnosticCentre diagnosticCentre = DiagnosticCentre.find.byId(1L);
		int i=0;
		for (Prescription prescription : diagnosticCentre.prescriptionList) {
			Logger.info(prescription.id+"=====Prescription id");
			for (DiagnosticTestLineItem diagnosticTestLineItem : prescription.diagnosticTestLineItemList) {
				Logger.info("test "+i++);
				Logger.info("test name="+diagnosticTestLineItem.diagnosticTest.name);
				
			}
			
		}
		return ok();
	}





















	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	



	/**
	 * @author : lakshmi
	 * GET /diagnosticlist
	 * Action to list out all diagnostic centers from diagnostic center table
	 */
	public static Result diagnosticList() {
		final Long id = LoginController.getLoggedInUser().getDiagnosticRepresentative().id;
		final DiagnosticCentre allList = DiagnosticCentre.find.byId(id);
		return ok(views.html.diagnostic.diagnosticCenterList.render(allList));

	}

	/**
	 * @author : lakshmi
	 * 
	 * GET /diagnostic-delete/:id
	 * 
	 * Action for deleting diagnostic center from table based on id
	 */
	public static Result deleteCenter(final Long id) {
		final DiagnosticCentre dc = DiagnosticCentre.find.byId(id);
		dc.delete();
		return ok("deleted successfully");
	}

	/**
	 * @author : lakshmi
	 * 
	 * GET /diagnostic-edit/:id
	 * 
	 * Action for getting the filled form to edit diagnostic centre details
	 */
	public static Result editDiagnosticDetails(final Long id) {
		final DiagnosticCentre dc = DiagnosticCentre.find.byId(id);
		final Form<DiagnosticBean> filledForm = diagnosticBeanForm.fill(dc.toBean());
		return ok(views.html.diagnostic.diagnosticReg.render(filledForm));
	}

	/**
	 * @author : lakshmi
	 * 
	 * POST /diagnostic-edit
	 * 
	 *Action for updating the diagnostic centre with edit information
	 */

	public static Result diagnosticEditprocess() {
		final Form<DiagnosticBean> filledForm = diagnosticBeanForm.bindFromRequest();
		if (filledForm.hasErrors()) {
			return badRequest(views.html.diagnostic.diagnosticReg.render(filledForm));
		} else {
			final DiagnosticCentre dc = filledForm.get().toDiagnosticCentre();
			dc.update();
		}
		return ok("updated successfully");
	}

	/**
	 * @author : lakshmi
	 * 
	 * GET/diagnostic-search
	 * 
	 * descrition: rendering to search form
	 */

	public static Result diagnosticSearch() {
		return ok(views.html.diagnostic.diagnosticSearch.render());
	}

	/**
	 * @author : lakshmi
	 * 
	 * POST: /diagnostic-search/list
	 * 
	 * Action for searching the diagnostic center by name ,mobile no &email id
	 */
	public static Result diagnosticSearchProcess() {
		final DynamicForm requestData = Form.form().bindFromRequest();
		final String searchStr = requestData.get("searchStr");
		Logger.info("searchStr==" + searchStr);
		// if string is empty return zero
		if (searchStr != null && !searchStr.isEmpty()) {
			// it is a string, search by name
			if (searchStr.matches("[a-zA-Z]+")) {
				final List<DiagnosticCentre> dcSearch = DiagnosticCentre.find.where().like("name", searchStr + "%").findList();
				Logger.info("dcSearch.size()" + dcSearch.size());
				return ok(views.html.diagnostic.patientDiagnosticCenterList.render(dcSearch));
			}
			// if it is an email
			else if (searchStr.contains("@")) {
				final List<DiagnosticCentre> dcSearch = DiagnosticCentre.find.where().eq("emailId", searchStr).findList();
				return ok(views.html.diagnostic.patientDiagnosticCenterList.render(dcSearch));
			}
			// if it is a number
			else {
				final List<DiagnosticCentre> dcSearch = DiagnosticCentre.find.where().eq("mobileNo", searchStr).findList();
				return ok(views.html.diagnostic.patientDiagnosticCenterList.render(dcSearch));
			}

		}

		else {

			return ok();
		}

	}

	/**
	 * @author : lakshmi
	 * 
	 * GET/test
	 * 
	 * Action for rendering to addDiagnosticTest scala to add test to the diagnostic center
	 */

	public static Result addTest() {
		return ok(views.html.diagnostic.addDiagnosticTest.render(diagnosticTestForm));
	}

	/**
	 * @author : lakshmi
	 * 
	 * POST /test/save
	 * 
	 * Action for updating diagnostic centre with added test
	 */

	public static Result addTestProcess() {
		final Form<DiagnosticTest> filledForm = diagnosticTestForm.bindFromRequest();
		if (filledForm.hasErrors()) {
			Logger.info("*** user bad request");
			return badRequest(views.html.diagnostic.addDiagnosticTest.render(filledForm));
		} else {
			final DiagnosticTest diagTestForm = filledForm.get();
			Logger.info("*** user object ");
			final Long id=LoginController.getLoggedInUser().getDiagnosticRepresentative().id;
			final DiagnosticCentre dc=DiagnosticCentre.find.byId(id);
			dc.diagnosticTestList.add(diagTestForm);
			dc.update();
			return ok(views.html.diagnostic.addDiagnosticTest.render(diagnosticTestForm));
		}

	}
	/*
	 * @author : lakshmi
	 * 
	 * @url: /add-test-done

	public static Result addTestDone() {
		final AppUser appUser = UserController.joinUsForm.bindFromRequest()
				.get().toAppUser();
		return ok(views.html.dashboard.render(appUser));
	}
	 */

	/**
	 * @author : lakshmi
	 * 
	 * GET /test-list/:diagnosticCentreId/:patientId
	 * 
	 * Action for displaying all the tests available for incoming diagnosticCentreId & PatientId
	 */
	public static Result diagnosticTestList(final Long diagnosticCentreId,final Long PatientId) {
		if (PatientId != 0 && diagnosticCentreId == 0) {
			Logger.info("patient id=="+PatientId);
			Logger.info("diagnosticCentreId=="+diagnosticCentreId);
			final DiagnosticCentre diagnosticCenter = LoginController.getLoggedInUser().getDiagnosticRepresentative().diagnosticCentre;
			final Patient patient = Patient.find.byId(PatientId);
			return ok(views.html.diagnostic.diagnosticCentreProfile.render(diagnosticCenter, patient));
		} else {
			Logger.info("patient id=="+PatientId);
			Logger.info("diagnosticCentreId=="+diagnosticCentreId);
			final DiagnosticCentre diagnosticCenter = DiagnosticCentre.find.byId(diagnosticCentreId);
			return ok(views.html.diagnostic.diagnosticCentreProfile.render(diagnosticCenter, null));
		}
	}

	/**
	 * @author : lakshmi
	 * 
	 *GET/diagnostic-test-list
	 * 
	 * Action for displaying all the tests belonging to the diagnostic centre
	 */
	public static Result diagnosticCentreTestList() {
		final DiagnosticCentre dc = LoginController.getLoggedInUser().getDiagnosticRepresentative().diagnosticCentre;
		return ok(views.html.diagnostic.diagnosticCentreTestProfile.render(dc));
	}

	/*
	 * @author : lakshmi
	 * 
	 * @url: /upload-patient-file/:id
	 * 
	 * description: rendering to the uploadPatientReort.scala to get upload form
	 */
	static Long ids;

	public static Result uploadFile(final Long id) {
		ids = id;
		return ok(views.html.diagnostic.uploadPatientReort.render(diagReport));
	}

	/*
	 * @author : lakshmi
	 * 
	 * @url:/upload-patient-file/save
	 * 
	 * description: updating DiagnosticReport by uploading the Diagnostic report
	 */

	public static Result uploadFileProcess() {
		Logger.info("ids==" + ids);
		final DiagnosticReport upload = DiagnosticReport.find.byId(ids);
		final play.mvc.Http.MultipartFormData body = request().body().asMultipartFormData();
		final FilePart file = body.getFile("upload");
		if (file != null) {
			/*final String fileName = file.getFilename();
			final String contentType = file.getContentType();*/
			final File file1 = file.getFile();
			final byte[] bytes = new byte[(int) file1.length()];
			try {
				final FileInputStream fileInputStream = new FileInputStream(file1);
				fileInputStream.read(bytes);
				for (int i = 0; i < bytes.length; i++) {
					System.out.print((char) bytes[i]);
				}
			} catch (final FileNotFoundException e) {
				System.out.println("File Not Found.");
				e.printStackTrace();
			}
			catch (final IOException e1) {
				System.out.println("Error Reading The File.");
				e1.printStackTrace();
			}
			upload.fileName = file.getFilename();
			upload.fileContent = bytes;
			upload.update();
			return TODO;
			//return redirect(routes.DiagnosticOrderController.reportReady(ids));
		} else {
			flash("error", "Missing file");
			return ok("got error while uploading");
		}
	}

	/*
	 * @author : lakshmi
	 * 
	 * @url:/download
	 * 
	 *description: downloading the Diagnostic report
	 */
	public static Result downloadFile() {
		final Long id=(long) 1;
		final DiagnosticReport diagReport = DiagnosticReport.find.byId(id);
		response().setContentType("application/x-download");
		response().setHeader("Content-disposition","attachment; filename="+diagReport.fileName);
		final File file = new File(diagReport.fileName);
		try {
			FileUtils.writeByteArrayToFile(file, diagReport.fileContent);
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ok(file);
	}

	/**
	 * @author lakshmi
	 * Action to add and update a ShowCasedProduct to the Diagnostic showCasedList
	 * POST	diagnostic/add-service-to-showcase
	 */

	public static Result addShowCasedService(){
		try{
			final Map<String, String[]> requestMap = request().body().asMultipartFormData().asFormUrlEncoded();
			final DiagnosticCentre diagnosticCentre = DiagnosticCentre.find.byId(Long.parseLong(requestMap.get("diagnosticId")[0]));
			Logger.info("map size"+requestMap);
			ShowCasedService showCasedService;
			if(Long.parseLong(requestMap.get("showCaseServiceId")[0]) != 0){
				showCasedService = ShowCasedService.find.byId(Long.parseLong(requestMap.get("showCaseServiceId")[0]));

			}else{
				showCasedService = new ShowCasedService();

			}
			if(requestMap.get("name") != null && (requestMap.get("name")[0].trim().compareToIgnoreCase("")!=0)){
				showCasedService.name = requestMap.get("name")[0];
			}
			if(requestMap.get("description") != null && (requestMap.get("description")[0].trim().compareToIgnoreCase("")!=0)){
				showCasedService.description = requestMap.get("description")[0];
			}
			if(requestMap.get("cost") != null && (requestMap.get("cost")[0].trim().compareToIgnoreCase("")!=0)){
				showCasedService.cost = Double.parseDouble(requestMap.get("cost")[0]);
			}
			if (request().body().asMultipartFormData().getFiles().size() != 0) {
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
						showCasedService.showcasedImagesList.add(fileEntity);
						Logger.info(" fileEntity id==="+fileEntity.id);
					}
				}
				Logger.info("image list size() "+showCasedService.showcasedImagesList.size());
			}


			if(showCasedService.id == null){
				diagnosticCentre.showCasedServiceList.add(showCasedService);
			}
			else{
				diagnosticCentre.showCasedServiceList.remove(ShowCasedService.find.byId(showCasedService.id));
				diagnosticCentre.showCasedServiceList.add(showCasedService);
			}
			diagnosticCentre.update();
			Logger.info("diag centre ID=="+diagnosticCentre.id);

			Logger.info("ShowcasedProduct list size=="+diagnosticCentre.showCasedServiceList.size());
			Logger.info("showcased product image list=="+diagnosticCentre.showCasedServiceList.get(0).showcasedImagesList.size());
			Logger.info("Showcase Product Id=="+showCasedService.id);
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
	public static Result removeServiceFromShowcaseList(final Long showCaseserviceId){
		Logger.info("inside");
		final DiagnosticCentre diagnosticCentre = LoginController.getLoggedInUser().getDiagnosticRepresentative().diagnosticCentre;
		final ShowCasedService showCasedService  = ShowCasedService.find.byId(showCaseserviceId);
		//		showCasedProduct.delete();
		diagnosticCentre.showCasedServiceList.remove(showCasedService);

		Logger.info("size()==="+diagnosticCentre.showCasedServiceList.size());
		diagnosticCentre.update();
		showCasedService.delete();
		return redirect(routes.UserActions.dashboard());

	}




	public static Result removeShowCasedImage(final Long showCasedServiceId,final Long imageId ){
		final ShowCasedService showCasedService = ShowCasedService.find.byId(showCasedServiceId);
		for (final FileEntity image : showCasedService.showcasedImagesList) {
			if(image.id == imageId){
				showCasedService.showcasedImagesList.remove(image);
				break;
			}

		}
		showCasedService.update();

		return redirect(routes.UserActions.dashboard());
	}
	
	
	
	

	/*
	 * @author : lakshmi
	 * 
	 * @url:/display-orders
	 * 
	 * description: Displaying all orders came for the logged in diagnostic centre
	 */

	public static Result viewOrders() {
		final DiagnosticCentre dc=LoginController.getLoggedInUser().getDiagnosticRepresentative().diagnosticCentre;
		Logger.info("loggerrrrrrrrrr....."
				+ dc.diagnosticOrderList.size());


return ok();
	}

	/*
	 * @author : lakshmi
	 * 
	 * @url:/remove-order/:id
	 * 
	 * description : removing the placed order from diagnostic Centre
	 */
	public static Result removeOrder(final Long id) {
		final DiagnosticCentre dc=LoginController.getLoggedInUser().getDiagnosticRepresentative().diagnosticCentre;
		Logger.info("loggerrrrrrrrrr....."
				+ dc.diagnosticOrderList.size());
		dc.diagnosticOrderList.remove(DiagnosticOrder.find.byId(id));
		dc.update();
		Logger.info("deleted success fully");

		return ok(views.html.diagnostic.diagnosticOrderList.render(dc.diagnosticOrderList));


	}

	/*
	 * @author : lakshmi
	 * 
	 * @url:/received-tests/:id
	 * 
	 * description : Displaying all tests for the current order
	 */
	public static Result viewReceivedTest(final Long id) {
		final DiagnosticOrder diagnosticOrder = DiagnosticOrder.find.byId(id);
		Logger.info("diagnosticOrder.diagnosticReportList.size()=="+diagnosticOrder.diagnosticReportList.size());
		for (final DiagnosticReport report : diagnosticOrder.diagnosticReportList) {
			if(report.reportStatus.equals("REPORT_READY")){
				diagnosticOrder.diagnosticOrderStatus = DiagnosticOrderStatus.CONFIRMED;
				diagnosticOrder.confirmedDate = new Date();

			}
			else{
				diagnosticOrder.diagnosticOrderStatus = DiagnosticOrderStatus.CONFIRMED;
			}
			diagnosticOrder.update();
		}
		return ok(views.html.diagnostic.receivedTests.render(diagnosticOrder.diagnosticReportList));
	}
	/*
	 * @author : lakshmi
	 * 
	 * @url:
	 * 
	 * description: making the status of the order to confirmed
	 */
	public static Result confirmed(final Long id) {
		final DiagnosticOrder diagnosticOrder = DiagnosticOrder.find.byId(id);
		final DiagnosticReport diagnosticReport = DiagnosticReport.find.byId(id);
		if (diagnosticReport.reportStatus.equals(DiagnosticReportStatus.REPORT_READY)) {
			diagnosticOrder.diagnosticOrderStatus = DiagnosticOrderStatus.CONFIRMED;
			diagnosticOrder.confirmedDate = new Date();
			diagnosticOrder.update();
			return ok("order confirmed");
		} else {
			return ok("order status is..."
					+ diagnosticOrder.diagnosticOrderStatus);
		}

	}
	/*
	 * @author : lakshmi
	 * 
	 * @url:/sample-collected/:id
	 * 
	 * description: making  the status of report to sample_collected
	 */
	/*
	 * status for the report sample colected
	 */

	public static Result sampleCollect(final Long id) {
		final DiagnosticReport diagnosticReport = DiagnosticReport.find.byId(id);
		diagnosticReport.reportStatus = DiagnosticReportStatus.SAMPLE_COLLECTED;
		diagnosticReport.sampleCollectionDate = new Date();
		diagnosticReport.update();
		return ok("views.html.diagnostic.receivedTests.render(diagnosticOrder.diagnosticReportList)");
	}

	/*
	 * status for the report generated
	 * 
	 * @url: /report-generated
	 * 
	 * description: making the status of report to REPORT_READY
	 */
	public static Result reoprtReady(final Long id) {

		final DiagnosticReport diagnosticReport = DiagnosticReport.find.byId(id);
		diagnosticReport.reportStatus = DiagnosticReportStatus.REPORT_READY;
		diagnosticReport.reportGenerationDate = new Date();
		//diagnosticOrder.diagnosticReportList;

		//if()
		diagnosticReport.update();
		return ok("Report generated");

	}

	/*
	 * @author : lakshmi
	 * 
	 * @url:/patient-search
	 * 
	 * description: rendering page to search for diagnostic centre by patient
	 */

	public static Result patientSearch() {

		return ok(views.html.diagnostic.patientSearch.render());

	}
	/*
	 * @author : lakshmi
	 * 
	 * @url: /patient-search/list
	 * 
	 * description: searching the diagnostic center by name ,email id by the patient
	 */

	public static Result patientSearchProcess() {

		final DynamicForm requestData = Form.form().bindFromRequest();
		final String searchStr = requestData.get("searchStr");
		Logger.info("searchStr==" + searchStr);
		// if string is empty return zero
		if (searchStr != null && !searchStr.isEmpty()) {
			// it is a string, search by name
			if (searchStr.matches("[a-zA-Z]+")) {
				final List<Patient> patientSearch = Patient.find
						.where().like("appUser.name", searchStr + "%").findList();
				Logger.info("dcSearch.size()" + patientSearch.size());
				return ok(views.html.diagnostic.patientList
						.render(patientSearch));
			}
			// if it is an email
			else if (searchStr.contains("@")) {
				final List<Patient> patientSearch = Patient.find
						.where().eq("email", searchStr).findList();
				return ok(views.html.diagnostic.patientList
						.render(patientSearch));
			}

		}

		else {

			return ok();
		}
		return ok();
	}
}
