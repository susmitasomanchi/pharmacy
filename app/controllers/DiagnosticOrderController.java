package controllers;

import java.util.Date;

import models.diagnostic.DiagnosticCentre;
import models.diagnostic.DiagnosticOrder;
import models.diagnostic.DiagnosticOrderStatus;
import models.diagnostic.DiagnosticReport;
import models.diagnostic.DiagnosticReportStatus;
import models.diagnostic.DiagnosticRepresentative;
import models.diagnostic.DiagnosticTest;
import play.Logger;
import play.mvc.Controller;
import play.mvc.Result;

public class DiagnosticOrderController extends Controller {

	public static DiagnosticRepresentative diagnosticRepresentative = LoginController
			.getLoggedInUser().getDiagnosticRepresentative();
	public static DiagnosticCentre dc = diagnosticRepresentative.diagnosticCentre;

	/*
	 * status for the order received
	 */
	public static Result receive() {
		String diagnosticTestIdStrings[] = request().body().asFormUrlEncoded().get("diagnosticTestIds");
		DiagnosticOrder diagnosticOrder = new DiagnosticOrder();
		diagnosticOrder.patient = LoginController.getLoggedInUser()
				.getPatient();
		for (String idStr : diagnosticTestIdStrings) {
			DiagnosticReport diagnosticReport = new DiagnosticReport();
			Long id = Long.valueOf(idStr);
			Logger.info(idStr);
			diagnosticReport.diagnosticTest = DiagnosticTest.find.byId(id);
			diagnosticOrder.diagnosticReportList.add(diagnosticReport);
			diagnosticOrder.update();
		}

		dc.diagnosticOrderList.add(diagnosticOrder);
		dc.update();
		return ok("updated successfully");
	}

	public static Result viewOrders(Long id) {

		Logger.info("loggerrrrrrrrrr....."
				+ dc.diagnosticOrderList.get(0).diagnosticOrderStatus);

		return ok("views.html.diagnostic.diagnosticCenterList.render(dc.diagnosticOrderList)");

	}

	/*
	 * status for the order confirmed
	 */
	public static Result confirmed(final Long id) {

		DiagnosticOrder diagnosticOrder = DiagnosticOrder.find.byId(id);
		DiagnosticReport diagnosticReport = DiagnosticReport.find.byId(id);
		if (diagnosticReport.reportStatus
				.equals(DiagnosticReportStatus.REPORT_READY)) {
			diagnosticOrder.diagnosticOrderStatus = DiagnosticOrderStatus.CONFIRMED;
			diagnosticOrder.confirmedDate = new Date();

			diagnosticOrder.update();
			return ok("order confirmed");
		}

		else

			return ok("order status is..."
					+ diagnosticOrder.diagnosticOrderStatus);

	}

	/*
	 * status for the report sample colected
	 */

	public static Result sampleCollect(final Long id) {
		DiagnosticReport diagnosticReport = DiagnosticReport.find.byId(id);
		diagnosticReport.reportStatus = DiagnosticReportStatus.SAMPLE_COLLECTED;
		diagnosticReport.sampleCollectionDate = new Date();
		diagnosticReport.update();
		return ok("sample collected");
	}

	/*
	 * status for the report generated
	 */
	public static Result reoprtReady(final Long id) {

		DiagnosticReport diagnosticReport = DiagnosticReport.find.byId(id);
		diagnosticReport.reportStatus = DiagnosticReportStatus.REPORT_READY;
		diagnosticReport.reportGenerationDate = new Date();
		diagnosticReport.update();
		return ok("Report generated");

	}

}
