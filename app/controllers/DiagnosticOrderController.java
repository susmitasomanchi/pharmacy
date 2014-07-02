package controllers;

import java.util.Date;

<<<<<<< HEAD
import models.diagnostic.DiagnosticCentre;
import models.diagnostic.DiagnosticOrder;
import models.diagnostic.DiagnosticOrderStatus;
import models.diagnostic.DiagnosticReport;
import models.diagnostic.DiagnosticReportStatus;
import models.diagnostic.DiagnosticRepresentative;
import models.diagnostic.DiagnosticTest;
=======
import models.DiagnosticCentre;
import models.DiagnosticOrder;
import models.DiagnosticOrderStatus;
import models.DiagnosticReport;
import models.DiagnosticRepresentative;
import models.DiagnosticTest;
>>>>>>> branch 'master' of http://pharmacy.bz/green-software/mednetwork.git
import play.Logger;
import play.mvc.Controller;
import play.mvc.Result;

public class DiagnosticOrderController extends Controller {

	public static DiagnosticRepresentative diagnosticRepresentative = LoginController
			.getLoggedInUser().getDiagnosticRepresentative();
	public static DiagnosticCentre dc = diagnosticRepresentative.diagnosticCentre;

<<<<<<< HEAD
=======

>>>>>>> branch 'master' of http://pharmacy.bz/green-software/mednetwork.git
	/*
	 * status for the order received
	 */
	public static Result receive() {
<<<<<<< HEAD
		String diagnosticTestIdStrings[] = request().body().asFormUrlEncoded().get("diagnosticTestIds");
		DiagnosticOrder diagnosticOrder = new DiagnosticOrder();
		diagnosticOrder.patient = LoginController.getLoggedInUser()
				.getPatient();
		for (String idStr : diagnosticTestIdStrings) {
			DiagnosticReport diagnosticReport = new DiagnosticReport();
			Long id = Long.valueOf(idStr);
=======
		final String diagnosticTestIdStrings[] = request().body().asFormUrlEncoded().get("diagnosticTestIds");
		final DiagnosticOrder diagnosticOrder=new DiagnosticOrder();

		diagnosticOrder.patient=LoginController.getLoggedInUser().getPatient();
		for (final String idStr : diagnosticTestIdStrings) {
			final DiagnosticReport diagnosticReport=new DiagnosticReport();
			final Long ids=Long.valueOf(idStr);
>>>>>>> branch 'master' of http://pharmacy.bz/green-software/mednetwork.git
			Logger.info(idStr);
<<<<<<< HEAD
			diagnosticReport.diagnosticTest = DiagnosticTest.find.byId(id);
=======
			diagnosticReport.diagnosticTest=DiagnosticTest.find.byId(ids);
			//	diagnosticReport.save();
>>>>>>> branch 'master' of http://pharmacy.bz/green-software/mednetwork.git
			diagnosticOrder.diagnosticReportList.add(diagnosticReport);
<<<<<<< HEAD
			diagnosticOrder.update();
=======

>>>>>>> branch 'master' of http://pharmacy.bz/green-software/mednetwork.git
		}
<<<<<<< HEAD
=======
		final DiagnosticRepresentative diagnosticRepresentative=LoginController.getLoggedInUser().getDiagnosticRepresentative();
		final DiagnosticCentre dc=DiagnosticCentre.find.where().eq("diagnosticRepAdmin", diagnosticRepresentative).findUnique();
		//	DiagnosticCenter dc=DiagnosticCenter.find.byId(id);
>>>>>>> branch 'master' of http://pharmacy.bz/green-software/mednetwork.git

		dc.diagnosticOrderList.add(diagnosticOrder);
<<<<<<< HEAD
		dc.update();
		return ok("updated successfully");
=======





		return ok();
>>>>>>> branch 'master' of http://pharmacy.bz/green-software/mednetwork.git
	}
<<<<<<< HEAD

	public static Result viewOrders(Long id) {

		Logger.info("loggerrrrrrrrrr....."
				+ dc.diagnosticOrderList.get(0).diagnosticOrderStatus);

		return ok("views.html.diagnostic.diagnosticCenterList.render(dc.diagnosticOrderList)");
=======
	public static Result viewOrders(final Long id){

		/*DiagnosticRepresentative diagnosticRepresentative=LoginController.getLoggedInUser().getDiagnosticRepresentative();
		DiagnosticCenter dc=DiagnosticCenter.find.where().eq("diagnosticRepAdmin", diagnosticRepresentative).findUnique();*/
		final DiagnosticCentre dc=DiagnosticCentre.find.byId(id);
		Logger.info("loggerrrrrrrrrr....."+dc.diagnosticOrderList.get(0).diagnosticOrderStatus);

		return TODO;

>>>>>>> branch 'master' of http://pharmacy.bz/green-software/mednetwork.git

	}

<<<<<<< HEAD
=======

>>>>>>> branch 'master' of http://pharmacy.bz/green-software/mednetwork.git
	/*
	 * status for the order confirmed
	 */
	public static Result confirmed(final Long id) {

<<<<<<< HEAD
		DiagnosticOrder diagnosticOrder = DiagnosticOrder.find.byId(id);
		DiagnosticReport diagnosticReport = DiagnosticReport.find.byId(id);
=======
		final DiagnosticOrder diagnosticOrder = DiagnosticOrder.find.byId(id);
		final DiagnosticReport diagnosticReport = DiagnosticReport.find.byId(id);

		/*
		 * Long
		 * doctorsPrescriptionId=diagnosticPrescription.doctorsPrescriptionId;
		 * DoctorsPrescription
		 * doctorsPrescription=DoctorsPrescription.find.byId(
		 * doctorsPrescriptionId);
		 */
>>>>>>> branch 'master' of http://pharmacy.bz/green-software/mednetwork.git
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
<<<<<<< HEAD
		DiagnosticReport diagnosticReport = DiagnosticReport.find.byId(id);
		diagnosticReport.reportStatus = DiagnosticReportStatus.SAMPLE_COLLECTED;
=======

		final DiagnosticReport diagnosticReport = DiagnosticReport.find.byId(id);

		/*
		 * Long
		 * doctorsPrescriptionId=diagnosticPrescription.doctorsPrescriptionId;
		 * DoctorsPrescription
		 * doctorsPrescription=DoctorsPrescription.find.byId(
		 * doctorsPrescriptionId);
		 */
		diagnosticReport.reportStatus = DiagnosticOrderStatus.SAMPLE_COLLECTED;
>>>>>>> branch 'master' of http://pharmacy.bz/green-software/mednetwork.git
		diagnosticReport.sampleCollectionDate = new Date();
		diagnosticReport.update();
		return ok("sample collected");
	}

	/*
	 * status for the report generated
	 */
	public static Result reoprtReady(final Long id) {

<<<<<<< HEAD
		DiagnosticReport diagnosticReport = DiagnosticReport.find.byId(id);
		diagnosticReport.reportStatus = DiagnosticReportStatus.REPORT_READY;
=======
		final DiagnosticReport diagnosticReport = DiagnosticReport.find.byId(id);

		/*
		 * Long
		 * doctorsPrescriptionId=diagnosticPrescription.doctorsPrescriptionId;
		 * DoctorsPrescription
		 * doctorsPrescription=DoctorsPrescription.find.byId(
		 * doctorsPrescriptionId);
		 */
		diagnosticReport.reportStatus = DiagnosticOrderStatus.REPORT_READY;
>>>>>>> branch 'master' of http://pharmacy.bz/green-software/mednetwork.git
		diagnosticReport.reportGenerationDate = new Date();
		diagnosticReport.update();
		return ok("Report generated");

	}
<<<<<<< HEAD
=======

>>>>>>> branch 'master' of http://pharmacy.bz/green-software/mednetwork.git

}
