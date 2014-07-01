package controllers;

import java.util.Date;

import models.DiagnosticCentre;
import models.DiagnosticOrder;
import models.DiagnosticOrderStatus;
import models.DiagnosticReport;
import models.DiagnosticRepresentative;
import models.DiagnosticTest;
import play.Logger;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;

public class DiagnosticOrderController extends Controller {

	static Form<DiagnosticOrder> diagnosticOrder = Form
			.form(DiagnosticOrder.class);
	static Form<DiagnosticReport> diagnosticReport = Form
			.form(DiagnosticReport.class);
	static Form<DiagnosticCentre> diagnosticCentreForm = Form
			.form(DiagnosticCentre.class);


	/*
	 * status for the order
	 * received
	 */
	public static Result receive() {
		final String diagnosticTestIdStrings[] = request().body().asFormUrlEncoded().get("diagnosticTestIds");
		final DiagnosticOrder diagnosticOrder=new DiagnosticOrder();

		diagnosticOrder.patient=LoginController.getLoggedInUser().getPatient();
		for (final String idStr : diagnosticTestIdStrings) {
			final DiagnosticReport diagnosticReport=new DiagnosticReport();
			final Long ids=Long.valueOf(idStr);
			Logger.info(idStr);
			diagnosticReport.diagnosticTest=DiagnosticTest.find.byId(ids);
			//	diagnosticReport.save();
			diagnosticOrder.diagnosticReportList.add(diagnosticReport);

		}
		final DiagnosticRepresentative diagnosticRepresentative=LoginController.getLoggedInUser().getDiagnosticRepresentative();
		final DiagnosticCentre dc=DiagnosticCentre.find.where().eq("diagnosticRepAdmin", diagnosticRepresentative).findUnique();
		//	DiagnosticCenter dc=DiagnosticCenter.find.byId(id);

		dc.diagnosticOrderList.add(diagnosticOrder);





		return ok();
	}
	public static Result viewOrders(final Long id){

		/*DiagnosticRepresentative diagnosticRepresentative=LoginController.getLoggedInUser().getDiagnosticRepresentative();
		DiagnosticCenter dc=DiagnosticCenter.find.where().eq("diagnosticRepAdmin", diagnosticRepresentative).findUnique();*/
		final DiagnosticCentre dc=DiagnosticCentre.find.byId(id);
		Logger.info("loggerrrrrrrrrr....."+dc.diagnosticOrderList.get(0).diagnosticOrderStatus);

		return TODO;


	}


	/*
	 * status for the order
	 * confirmed
	 */
	public static Result confirmed(final Long id) {

		final DiagnosticOrder diagnosticOrder = DiagnosticOrder.find.byId(id);
		final DiagnosticReport diagnosticReport = DiagnosticReport.find.byId(id);

		/*
		 * Long
		 * doctorsPrescriptionId=diagnosticPrescription.doctorsPrescriptionId;
		 * DoctorsPrescription
		 * doctorsPrescription=DoctorsPrescription.find.byId(
		 * doctorsPrescriptionId);
		 */
		if (diagnosticReport.reportStatus
				.equals(DiagnosticOrderStatus.REPORT_READY)) {
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
	 * status for the report
	 * sample colected
	 */

	public static Result sampleCollect(final Long id) {

		final DiagnosticReport diagnosticReport = DiagnosticReport.find.byId(id);

		/*
		 * Long
		 * doctorsPrescriptionId=diagnosticPrescription.doctorsPrescriptionId;
		 * DoctorsPrescription
		 * doctorsPrescription=DoctorsPrescription.find.byId(
		 * doctorsPrescriptionId);
		 */
		diagnosticReport.reportStatus = DiagnosticOrderStatus.SAMPLE_COLLECTED;
		diagnosticReport.sampleCollectionDate = new Date();

		diagnosticReport.update();

		return ok("sample collected");

	}
	/*
	 * status for the report
	 * generated
	 */
	public static Result reoprtReady(final Long id) {

		final DiagnosticReport diagnosticReport = DiagnosticReport.find.byId(id);

		/*
		 * Long
		 * doctorsPrescriptionId=diagnosticPrescription.doctorsPrescriptionId;
		 * DoctorsPrescription
		 * doctorsPrescription=DoctorsPrescription.find.byId(
		 * doctorsPrescriptionId);
		 */
		diagnosticReport.reportStatus = DiagnosticOrderStatus.REPORT_READY;
		diagnosticReport.reportGenerationDate = new Date();
		diagnosticReport.update();
		return ok("Report generated");

	}


}
