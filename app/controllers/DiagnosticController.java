package controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import models.AppUser;
import models.diagnostic.DiagnosticCentre;
import models.diagnostic.DiagnosticReport;
import models.diagnostic.DiagnosticRepresentative;
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
	public static DiagnosticRepresentative dr = LoginController.getLoggedInUser()
			.getDiagnosticRepresentative();
	public static DiagnosticCentre dc = dr.diagnosticCentre;

	public static Form<DiagnosticBean> diagnosticBeanForm = Form
			.form(DiagnosticBean.class);

	public static Form<DiagnosticCentre> diagnosticForm = Form
			.form(DiagnosticCentre.class);

	public static Form<DiagnosticTest> diagnosticTestForm = Form
			.form(DiagnosticTest.class);

	public static Form<DiagnosticReport> diagReport = Form
			.form(DiagnosticReport.class);

	/*
	 * list out all diagnostic centers
	 * from diagnostic center table
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
		final Long id=LoginController.getLoggedInUser().getDiagnosticRepresentative().id;
		final DiagnosticCentre allList = DiagnosticCentre.find.byId(id);

		return ok(views.html.diagnostic.diagnosticCenterList.render(allList));

	}

	/*
	 * deleting diagnostic center
	 * from table based on id
	 */
	public static Result deleteCenter(final Long id) {
		final DiagnosticCentre dc = DiagnosticCentre.find.byId(id);
		dc.delete();
		return ok("deleted successfully");
	}

	/*
	 * editing the diagnostic center details
	 */
	public static Result editDiagnosticDetails(final Long id) {
		final DiagnosticCentre dc = DiagnosticCentre.find.byId(id);
		final Form<DiagnosticBean> filledForm = diagnosticBeanForm.fill(dc.toBean());
		return ok(views.html.diagnostic.diagnosticReg.render(filledForm));
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

	}

	/*
	 * searching the diagnostic center by name ,mobile no &email id
	 */
	public static Result diagnosticSearchProcess() {

		final DynamicForm requestData = Form.form().bindFromRequest();

		final String searchStr = requestData.get("searchStr");

		// if string is empty return zero
		if (searchStr != null && !searchStr.isEmpty()) {

			// it is a string, search by name
			if (searchStr.matches("[a-zA-Z]+")) {

				final List<DiagnosticCentre> dcSearch = DiagnosticCentre.find
						.where().like("name", searchStr + "%").findList();

				return ok(views.html.diagnostic.patientDiagnosticCenterList
						.render(dcSearch));
			}
			// if it is an email
			else if (searchStr.contains("@")) {

				final List<DiagnosticCentre> dcSearch = DiagnosticCentre.find
						.where().eq("emailId", searchStr).findList();

				return ok(views.html.diagnostic.patientDiagnosticCenterList
						.render(dcSearch));
			}// if it is a number
			else {
				final List<DiagnosticCentre> dcSearch = DiagnosticCentre.find
						.where().eq("mobileNo", searchStr).findList();

				return ok(views.html.diagnostic.patientDiagnosticCenterList
						.render(dcSearch));
			}

		}

		else {

			return ok();
		}

	}


	/*
	 * adding test to
	 * the diagnostic center
	 */

	public static Result addTest(){
		return ok(views.html.diagnostic.addDiagnosticTest.render(diagnosticTestForm));

	}

	public static Result addTestProcess() {
		final Form<DiagnosticTest> filledForm = diagnosticTestForm
				.bindFromRequest();

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
	public static Result addTestDone(){
		final AppUser appUser = UserController.joinUsForm.bindFromRequest().get().toAppUser();
		return ok(views.html.dashboard.render(appUser));
	}
	/*
	 * displaying all the
	 * tests available for
	 * incoming id
	 */

	public static Result diagnosticTestList(final Long id) {
		final DiagnosticCentre diagnosticCenter =  DiagnosticCentre.find.byId(id);
		//List<DiagnosticTest> diagnosticTestList=diagnosticCenter.diagnosticTestList;
		return ok(views.html.diagnostic.diagnosticCentreProfile.render(diagnosticCenter));
	}
	public static Result diagnosticCentreTestList() {

		return ok(views.html.diagnostic.diagnosticCentreTestProfile.render(dc));
	}
	/*
	 * uploading the
	 * Diagnostic report
	 */
	public static Result uploadFile() {
		return ok(views.html.diagnostic.uploadPatientReort.render(diagReport));
	}


	public static Result uploadFileProcess() {
		final DiagnosticReport upload=new DiagnosticReport();
		final play.mvc.Http.MultipartFormData body = request().body().asMultipartFormData();
		final FilePart file = body.getFile("upload");
		if (file != null) {
			final String fileName = file.getFilename();
			final String contentType = file.getContentType();
			final File file1=file.getFile();
			final byte[] bytes = new byte[(int) file1.length()];
			try {
				final FileInputStream fileInputStream = new FileInputStream(file1);
				fileInputStream.read(bytes);
				for (int i = 0; i < bytes.length; i++) {
					System.out.print((char)bytes[i]);
				}
			} catch (final FileNotFoundException e) {
				System.out.println("File Not Found.");
				e.printStackTrace();
			}
			catch (final IOException e1) {
				System.out.println("Error Reading The File.");
				e1.printStackTrace();
			}
			upload.fileName=file.getFilename();
			upload.fileContent =bytes;
			upload.save();

			return ok(views.html.diagnostic.download.render(upload));
		} else {
			flash("error", "Missing file");
			return ok("got error while uploading");
		}
	}

	/*
	 * downloading the
	 * Diagnostic report
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



}
