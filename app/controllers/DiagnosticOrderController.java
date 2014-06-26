package controllers;

import java.util.Date;
import java.util.List;

import models.DiagnosticCenter;
import models.DiagnosticOrder;
import models.DiagnosticOrderStatus;
import models.DiagnosticReport;
import models.DiagnosticTest;
import models.DoctorsPrescription;
import play.Logger;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;

public class DiagnosticOrderController extends Controller {

	static Form<DiagnosticOrder> diagnosticOrder = Form
			.form(DiagnosticOrder.class);
	static Form<DiagnosticReport> diagnosticReport = Form
			.form(DiagnosticReport.class);
	static Form<DiagnosticCenter> diagnosticCentreForm = Form
			.form(DiagnosticCenter.class);

	
	/*
	 * status for the order
	 * received
	 */
	public static Result receive() {
		String diagnosticTestIdStrings[] = request().body().asFormUrlEncoded().get("diagnosticTestIds");
		DiagnosticOrder diagnosticOrder=new DiagnosticOrder();
		
		diagnosticOrder.patient=LoginController.getLoggedInUser().getPatient();
		for (String idStr : diagnosticTestIdStrings) {
			DiagnosticReport diagnosticReport=new DiagnosticReport();
			Long id=Long.valueOf(idStr);
			Logger.info(idStr);
			diagnosticReport.diagnosticTest=DiagnosticTest.find.byId(id);
			diagnosticReport.save();
			
		}
		
		
		
		
		
		
		return ok();
	}
	/*
	 * status for the order
	 * confirmed
	 */
	public static Result confirmed(final Long id) {

		DiagnosticOrder diagnosticOrder = DiagnosticOrder.find.byId(id);
		DiagnosticReport diagnosticReport = DiagnosticReport.find.byId(id);

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

		DiagnosticReport diagnosticReport = DiagnosticReport.find.byId(id);

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

		DiagnosticReport diagnosticReport = DiagnosticReport.find.byId(id);

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
