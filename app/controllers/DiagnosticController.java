package controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.DiagnosticCenter;
import models.DiagnosticRepresentative;
import play.Logger;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;

public class DiagnosticController extends Controller {
	public static Form<DiagnosticCenter> diagnosticForm = Form
			.form(DiagnosticCenter.class);

	public static Result diagnosticCenter() {
		return ok(views.html.diagnosticCenters.render(diagnosticForm));
	}
	/*
	 * storing diagnostic center details 
	 * in table
	 */

	public static Result diagnosticCenterProcess() {
		final Form<DiagnosticCenter> filledForm = diagnosticForm
				.bindFromRequest();

		if (filledForm.hasErrors()) {
			Logger.info("*** user bad request");
			return badRequest(views.html.diagnosticCenters.render(filledForm));
		} else {
			// final SalesRep salesRepForm = filledForm.get();
			final DiagnosticCenter diagForm = filledForm.get();
			Logger.info("*** user object ");

			// final AppUser appUser = salesRepForm.toEntity();
			diagForm.save();
			final String message = flash("success");

			return ok(String.format("Saved product %s", diagForm));
		}

	}
	/*
	 * list out all diagnostic centers
	 * from diagnostic center table
	 */
	public static Result diagnosticList() {
		List<DiagnosticCenter> allList = DiagnosticCenter.find.all();

		return ok(views.html.diagnostic.diagnosticCenterList.render(allList));

	}
	
	/*
	 * deleting diagnostic center
	 * from table based on id
	 */
	public static Result deleteCenter(Long id) {
		DiagnosticCenter.delete(id);
		return ok("deleted successfully");
	}

	/*
	 * editing the diagnostic center details
	 */
	public static Result editDiagnosticDetails(Long id) {
		final DiagnosticCenter dc = DiagnosticCenter.find.byId(id);
		final Form<DiagnosticCenter> filledForm = diagnosticForm.fill(dc);
		final DiagnosticCenter diagForm = filledForm.get();
		diagForm.update();
		return ok(views.html.diagnostic.diagnosticReg.render(filledForm));
	}

	public static Result diagnosticEditprocess() {
		final Form<DiagnosticCenter> filledForm = diagnosticForm
				.bindFromRequest();
		if (filledForm.hasErrors()) {
			return badRequest(views.html.diagnostic.diagnosticReg
					.render(filledForm));
		} else {
			final DiagnosticCenter dc = filledForm.get();

			if (dc.id == null) {
				dc.save();

			} else {
				dc.update();
			}
		}
		return ok();
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

				final List<DiagnosticCenter> dcSearch = DiagnosticCenter.find
						.where().like("name", searchStr + "%").findList();

				return ok(views.html.diagnostic.patientDiagnosticCenterList
						.render(dcSearch));
			}
			// if it is an email
			else if (searchStr.contains("@")) {

				final List<DiagnosticCenter> dcSearch = DiagnosticCenter.find
						.where().eq("emailId", searchStr).findList();

				return ok(views.html.diagnostic.patientDiagnosticCenterList
						.render(dcSearch));
			}// if it is a number
			else {
				final List<DiagnosticCenter> dcSearch = DiagnosticCenter.find
						.where().eq("mobileNo", searchStr).findList();

				return ok(views.html.diagnostic.patientDiagnosticCenterList
						.render(dcSearch));
			}

		}

		else {

			return ok();
		}

	}

	

}
