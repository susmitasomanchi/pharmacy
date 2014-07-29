package controllers;

import java.util.Date;
import java.util.List;

import models.diagnostic.DiagnosticCentre;
import models.diagnostic.DiagnosticOrder;
import models.diagnostic.DiagnosticOrderStatus;
import models.diagnostic.DiagnosticReport;
import models.diagnostic.DiagnosticReportStatus;
import models.diagnostic.DiagnosticTest;
import models.patient.Patient;
import play.Logger;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;

public class DiagnosticOrderController extends Controller {

	/*
	 * @author : lakshmi
	 * 
	 * @url:/place-order
	 * 
	 * description: persisting the orders placed by both diagnosticRepresentative and patient
	 */
	public static Result receive() {
		final DynamicForm requestData = Form.form().bindFromRequest();

		DiagnosticCentre dc;
		final String diagnosticTestIdStrings[] = request().body().asFormUrlEncoded().get("diagnosticTestIds");
		final DiagnosticOrder diagnosticOrder=new DiagnosticOrder();
		if(LoginController.getLoggedInUserRole().equals("ADMIN_DIAGREP")){
			final Long id = Long.valueOf(requestData.get("pateintId"));
			dc = LoginController.getLoggedInUser().getDiagnosticRepresentative().diagnosticCentre;
			//diagnosticOrder.patient = Patient.find.byId(id);
		}
		else{
			final Long id = Long.valueOf(requestData.get("diagnosticCentreId"));
			dc = DiagnosticCentre.find.byId(id);
			//diagnosticOrder.patient = LoginController.getLoggedInUser().getPatient();
		}
		for (final String idStr : diagnosticTestIdStrings) {
			final DiagnosticReport diagnosticReport=new DiagnosticReport();
			final Long ids=Long.valueOf(idStr);
			Logger.info(idStr);
			diagnosticReport.diagnosticTest = DiagnosticTest.find.byId(ids);
			diagnosticOrder.diagnosticReportList.add(diagnosticReport);


		}
		diagnosticOrder.diagnosticOrderStatus = DiagnosticOrderStatus.RECEIVED;
		diagnosticOrder.receivedDate = new Date();
		dc.diagnosticOrderList.add(diagnosticOrder);
		dc.update();
		Logger.info("dc.diagnosticOrderList.size()=="+dc.diagnosticOrderList.size());
		return ok("updated successfully");
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

		return ok(views.html.diagnostic.diagnosticOrderList.render(dc.diagnosticOrderList));


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
