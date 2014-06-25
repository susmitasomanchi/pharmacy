package controllers;

import java.util.Date;

import models.DiagnosticOrder;
import models.DiagnosticOrderStatus;
import models.DiagnosticReport;
import models.DiagnosticTest;
import models.DoctorsPrescription;
import play.Logger;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;

public class DiagnosticPrescriptionController extends Controller{
	
	static Form<DiagnosticOrder> diagnosticOrder=Form.form(DiagnosticOrder.class);
	static Form<DiagnosticReport> diagnosticReport=Form.form(DiagnosticReport.class);
	static Form<DoctorsPrescription> doctorsPrescriptionForm=Form.form(DoctorsPrescription.class);
	public static Result prescribe(){
		return ok(views.html.diagnosticReport.doctorsPrescription.render(doctorsPrescriptionForm));
	}
	
/*
 * status of diagostic test order
 */
	public static Result receive() {

		Form<DoctorsPrescription> docPresc=doctorsPrescriptionForm.bindFromRequest();
		if (docPresc.hasErrors()) {
			Logger.info("*** user bad request");
			return badRequest(views.html.diagnosticReport.doctorsPrescription.render(docPresc));
		} else {
			DoctorsPrescription doctorsPrescription=docPresc.get();
			DiagnosticOrder diagnosticOrder=new DiagnosticOrder();
		diagnosticOrder.doctorsPrescriptionId=doctorsPrescription.doctorsPrescriptionId;
		diagnosticOrder.diagnosticOrderStatus = DiagnosticOrderStatus.RECEIVED;
		diagnosticOrder.receivedDate = new Date();
		
			diagnosticOrder.save();
			Logger.info("status of the report====="+diagnosticOrder.diagnosticOrderStatus);
		return ok("test prescription accepted");
		}
			
	}
public static Result confirmed(final Long id) {

		
		DiagnosticOrder diagnosticOrder=DiagnosticOrder.find.byId(id);
		DiagnosticReport diagnosticReport=DiagnosticReport.find.byId(id);
		
		/*Long doctorsPrescriptionId=diagnosticPrescription.doctorsPrescriptionId;
		DoctorsPrescription doctorsPrescription=DoctorsPrescription.find.byId(doctorsPrescriptionId);*/
		if(diagnosticReport.reportStatus.equals("REPORT_READY")){
		diagnosticOrder.diagnosticOrderStatus = DiagnosticOrderStatus.CONFIRMED;
		diagnosticOrder.confirmedDate = new Date();
		
		diagnosticOrder.update();
		return ok("order confirmed");
		}
		
		else 
			
		return ok("order is in progress");
			
	}

	
public static Result sampleCollect(final Long id) {

		
	DiagnosticReport diagnosticReport=DiagnosticReport.find.byId(id);
		
		/*Long doctorsPrescriptionId=diagnosticPrescription.doctorsPrescriptionId;
		DoctorsPrescription doctorsPrescription=DoctorsPrescription.find.byId(doctorsPrescriptionId);*/
	diagnosticReport.reportStatus = DiagnosticOrderStatus.SAMPLE_COLLECTED;
	diagnosticReport.sampleCollectedDate = new Date();
		
	diagnosticReport.update();
		
		return ok("sample collected");
			
	}
public static Result generateReport(final Long id) {

	
	DiagnosticReport diagnosticReport=DiagnosticReport.find.byId(id);
	
	/*Long doctorsPrescriptionId=diagnosticPrescription.doctorsPrescriptionId;
	DoctorsPrescription doctorsPrescription=DoctorsPrescription.find.byId(doctorsPrescriptionId);*/
	diagnosticReport.reportStatus = DiagnosticOrderStatus.REPORT_READY;
	diagnosticReport.reportGenertaedDate = new Date();
	diagnosticReport.update();
	return ok("Report generated");
		
}
		

}
