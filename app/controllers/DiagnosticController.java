package controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import models.Patient;
import models.diagnostic.DiagnosticCentre;
import models.diagnostic.DiagnosticReport;
import models.diagnostic.DiagnosticTest;

import org.apache.commons.io.FileUtils;

import play.Logger;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Http.MultipartFormData.FilePart;
import play.mvc.Result;
import beans.DiagnosticBean;

public class DiagnosticController extends Controller {
<<<<<<< HEAD
=======
	public static DiagnosticRepresentative dr = LoginController.getLoggedInUser()
			.getDiagnosticRepresentative();
	public static DiagnosticCentre dc = dr.diagnosticCentre;

	public static Form<DiagnosticBean> diagnosticBeanForm = Form
			.form(DiagnosticBean.class);

	public static Form<DiagnosticCentre> diagnosticForm = Form
			.form(DiagnosticCentre.class);
>>>>>>> branch 'master' of http://pharmacy.bz/green-software/mednetwork.git

	public static Form<DiagnosticBean> diagnosticBeanForm = Form.form(DiagnosticBean.class);
	public static Form<DiagnosticCentre> diagnosticForm = Form.form(DiagnosticCentre.class);
	public static Form<DiagnosticTest> diagnosticTestForm = Form.form(DiagnosticTest.class);
	public static Form<DiagnosticReport> diagReport = Form.form(DiagnosticReport.class);

	/*
	 * @author : lakshmi
	 * 
	 * @url: /diagnosticlist
	 * 
	 * descrition: list out all diagnostic centers from diagnostic center table
	 */
	public static Result diagnosticCenter() {
		return ok(views.html.diagnosticCenters.render(diagnosticForm));
	}


	public static Result diagnosticCenterProcess() {
		final Form<DiagnosticCentre> filledForm = diagnosticForm
				.bindFromRequest();

		if (filledForm.hasErrors()) {
			Logger.info("*** user bad request");
			return badRequest(views.html.diagnosticCenters.render(filledForm));
		} else {
			// final SalesRep salesRepForm = filledForm.get();
			final DiagnosticCentre diagForm = filledForm.get();
			Logger.info("*** user object ");

			// final AppUser appUser = salesRepForm.toEntity();
			diagForm.save();
			final String message = flash("success");

			return ok(String.format("Saved product %s", diagForm));
		}

	}
	public static Result diagnosticList() {
<<<<<<< HEAD
=======
		final Long id=LoginController.getLoggedInUser().getDiagnosticRepresentative().id;
		final DiagnosticCentre allList = DiagnosticCentre.find.byId(id);
>>>>>>> branch 'master' of http://pharmacy.bz/green-software/mednetwork.git

		final Long id = LoginController.getLoggedInUser().getDiagnosticRepresentative().id;
		final DiagnosticCentre allList = DiagnosticCentre.find.byId(id);
		return ok(views.html.diagnostic.diagnosticCenterList.render(allList));

	}

	/*
	 * @author : lakshmi
	 * 
	 * @url: /diagnostic-delete/:id
	 * 
	 * descrition: deleting diagnostic center from table based on id
	 */
	public static Result deleteCenter(final Long id) {
<<<<<<< HEAD

=======
>>>>>>> branch 'master' of http://pharmacy.bz/green-software/mednetwork.git
		final DiagnosticCentre dc = DiagnosticCentre.find.byId(id);
		dc.delete();
		return ok("deleted successfully");
	}

	/*
	 * @author : lakshmi
	 * 
	 * @url: /diagnostic-edit/:id
	 * 
	 * descrition: getting the filled form to edit diagnostic centre details
	 */
	public static Result editDiagnosticDetails(final Long id) {
<<<<<<< HEAD

=======
>>>>>>> branch 'master' of http://pharmacy.bz/green-software/mednetwork.git
		final DiagnosticCentre dc = DiagnosticCentre.find.byId(id);
		final Form<DiagnosticBean> filledForm = diagnosticBeanForm.fill(dc.toBean());
		return ok(views.html.diagnostic.diagnosticReg.render(filledForm));
<<<<<<< HEAD
=======
	}

	public static Result diagnosticEditprocess() {
		final Form<DiagnosticBean> filledForm = diagnosticBeanForm
				.bindFromRequest();
		if (filledForm.hasErrors()) {
			return badRequest(views.html.diagnostic.diagnosticReg
					.render(filledForm));
		} else {

			final DiagnosticCentre dc = filledForm.get().toDiagnosticCentre();
			//dc.diagnosticRepAdmin.appUser=filledForm.get().toAppUserEntity();
			dc.update();
		}
		return ok("updated successfully");
	}

	public static Result diagnosticSearch(){
		return ok(views.html.diagnostic.diagnosticSearch.render());
>>>>>>> branch 'master' of http://pharmacy.bz/green-software/mednetwork.git

	}

	/*
	 * @author : lakshmi
	 * 
	 * @url: /diagnostic-edit
	 * 
	 * descrition: updating the diagnostic centre with edit information
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

	/*
	 * @author : lakshmi
	 * 
	 * @url:/diagnostic-search
	 * 
	 * descrition: rendering to search form
	 */

	public static Result diagnosticSearch() {
		return ok(views.html.diagnostic.diagnosticSearch.render());
	}

	/*
	 * @author : lakshmi
	 * 
	 * @url: /diagnostic-search/list
	 * 
	 * descrition: searching the diagnostic center by name ,mobile no &email id
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

	/*
	 * @author : lakshmi
	 * 
	 * @url:/test
	 * 
	 * descrition: rendering to addDiagnosticTest scala to add test to the diagnostic center
	 */

	public static Result addTest() {
		return ok(views.html.diagnostic.addDiagnosticTest.render(diagnosticTestForm));
	}

	/*
	 * @author : lakshmi
	 * 
	 * @url:/test/save
	 * 
	 * descrition: updating diagnostic centre with added test
	 */

	public static Result addTestProcess() {
		final Form<DiagnosticTest> filledForm = diagnosticTestForm.bindFromRequest();
		if (filledForm.hasErrors()) {
			Logger.info("*** user bad request");
			return badRequest(views.html.diagnostic.addDiagnosticTest.render(filledForm));
		} else {
			final DiagnosticTest diagTestForm = filledForm.get();
			Logger.info("*** user object ");
<<<<<<< HEAD
			final Long id = LoginController.getLoggedInUser().getDiagnosticRepresentative().id;
			final DiagnosticCentre dc = DiagnosticCentre.find.byId(id);
=======
			final Long id=LoginController.getLoggedInUser().getDiagnosticRepresentative().id;
			final DiagnosticCentre dc=DiagnosticCentre.find.byId(id);
>>>>>>> branch 'master' of http://pharmacy.bz/green-software/mednetwork.git
			dc.diagnosticTestList.add(diagTestForm);
			dc.update();
			return ok(views.html.diagnostic.addDiagnosticTest.render(diagnosticTestForm));
		}

	}
<<<<<<< HEAD

	/*
	 * @author : lakshmi
	 * 
	 * @url: /add-test-done

	public static Result addTestDone() {
		final AppUser appUser = UserController.joinUsForm.bindFromRequest()
				.get().toAppUser();
=======
	public static Result addTestDone(){
		final AppUser appUser = UserController.joinUsForm.bindFromRequest().get().toAppUser();
>>>>>>> branch 'master' of http://pharmacy.bz/green-software/mednetwork.git
		return ok(views.html.dashboard.render(appUser));
	}
	 */

<<<<<<< HEAD
	/*
	 * @author : lakshmi
	 * 
	 * @url:/test-list/:diagnosticCentreId/:patientId
	 * 
	 * descrition: displaying all the tests available for incoming diagnosticCentreId & PatientId
	 */
=======
	public static Result diagnosticTestList(final Long id) {
		final DiagnosticCentre diagnosticCenter =  DiagnosticCentre.find.byId(id);
		//List<DiagnosticTest> diagnosticTestList=diagnosticCenter.diagnosticTestList;
		return ok(views.html.diagnostic.diagnosticCentreProfile.render(diagnosticCenter));
	}
	public static Result diagnosticCentreTestList() {
>>>>>>> branch 'master' of http://pharmacy.bz/green-software/mednetwork.git

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

	/*
	 * @author : lakshmi
	 * 
	 * @url:/diagnostic-test-list
	 * 
	 * description: displaying all the tests belonging to the diagnostic centre
	 */
	public static Result diagnosticCentreTestList() {
		final DiagnosticCentre dc = LoginController.getLoggedInUser().getDiagnosticRepresentative().diagnosticCentre;
		return ok(views.html.diagnostic.diagnosticCentreTestProfile.render(dc));
	}

	/*
<<<<<<< HEAD
	 * @author : lakshmi
	 * 
	 * @url: /upload-patient-file/:id
	 * 
	 * description: rendering to the uploadPatientReort.scala to get upload form
=======
	 * uploading the
	 * Diagnostic report
>>>>>>> branch 'master' of http://pharmacy.bz/green-software/mednetwork.git
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
<<<<<<< HEAD
		Logger.info("ids==" + ids);
		final DiagnosticReport upload = DiagnosticReport.find.byId(ids);

=======
		final DiagnosticReport upload=new DiagnosticReport();
>>>>>>> branch 'master' of http://pharmacy.bz/green-software/mednetwork.git
		final play.mvc.Http.MultipartFormData body = request().body().asMultipartFormData();
		final FilePart file = body.getFile("upload");
		if (file != null) {
<<<<<<< HEAD
			/*final String fileName = file.getFilename();
			final String contentType = file.getContentType();*/
			final File file1 = file.getFile();
=======
			final String fileName = file.getFilename();
			final String contentType = file.getContentType();
			final File file1=file.getFile();
>>>>>>> branch 'master' of http://pharmacy.bz/green-software/mednetwork.git
			final byte[] bytes = new byte[(int) file1.length()];
			try {
<<<<<<< HEAD
				final FileInputStream fileInputStream = new FileInputStream(
						file1);
=======
				final FileInputStream fileInputStream = new FileInputStream(file1);
>>>>>>> branch 'master' of http://pharmacy.bz/green-software/mednetwork.git
				fileInputStream.read(bytes);
				for (int i = 0; i < bytes.length; i++) {
					System.out.print((char) bytes[i]);
				}
			} catch (final FileNotFoundException e) {
<<<<<<< HEAD
=======
				System.out.println("File Not Found.");
>>>>>>> branch 'master' of http://pharmacy.bz/green-software/mednetwork.git
				e.printStackTrace();
<<<<<<< HEAD
			} catch (final IOException e1) {
=======
			}
			catch (final IOException e1) {
				System.out.println("Error Reading The File.");
>>>>>>> branch 'master' of http://pharmacy.bz/green-software/mednetwork.git
				e1.printStackTrace();
			}
			upload.fileName = file.getFilename();
			upload.fileContent = bytes;
			upload.update();
			return redirect(routes.DiagnosticOrderController.reoprtReady(ids));
		} else {
			flash("error", "Missing file");
			return ok("got error while uploading");
		}
	}

	/*
<<<<<<< HEAD
	 * @author : lakshmi
	 * 
	 * @url:/download
	 * 
	 *description: downloading the Diagnostic report
=======
	 * downloading the
	 * Diagnostic report
>>>>>>> branch 'master' of http://pharmacy.bz/green-software/mednetwork.git
	 */
	public static Result downloadFile() {
<<<<<<< HEAD
		final Long id = (long) 1;
		final DiagnosticReport diagReport = DiagnosticReport.find.byId(id);
		response().setContentType("application/x-download");
		response().setHeader("Content-disposition","attachment; filename=" + diagReport.fileName);
=======
		final Long id=(long) 1;
		final DiagnosticReport diagReport = DiagnosticReport.find.byId(id);
		response().setContentType("application/x-download");
		response().setHeader("Content-disposition","attachment; filename="+diagReport.fileName);
>>>>>>> branch 'master' of http://pharmacy.bz/green-software/mednetwork.git
		final File file = new File(diagReport.fileName);
		try {
			FileUtils.writeByteArrayToFile(file, diagReport.fileContent);
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ok(file);
	}

}
