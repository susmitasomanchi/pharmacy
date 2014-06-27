package controllers;

import java.util.List;

import models.DiagnosticCentre;
import models.DiagnosticRepresentative;
import models.Doctor;
import play.Logger;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;

public class DRController extends Controller {
	public static DiagnosticRepresentative dr = LoginController.getLoggedInUser()
			.getDiagnosticRepresentative();
	public static DiagnosticCentre dc = dr.diagnosticCentre;
	public static Form<DiagnosticRepresentative> diagnosticRepresentative = Form
			.form(DiagnosticRepresentative.class);

	public static Result addDiagRep() {
		return ok(views.html.diagnostic.diagnosticRep
				.render(diagnosticRepresentative));
	}

	public static Result addDiagRepProcess() {
		/*DiagnosticRepresentative dr = LoginController.getLoggedInUser()
				.getDiagnosticRepresentative();
		*/final Form<DiagnosticRepresentative> filledForm = diagnosticRepresentative
				.bindFromRequest();
		Logger.info("filledForm" + filledForm);

		if (filledForm.hasErrors()) {
			return badRequest(views.html.diagnostic.diagnosticRep
					.render(filledForm));
		} else {

			final DiagnosticRepresentative diagRep = filledForm.get();
			Logger.info("diagrep.." + diagRep);
			Logger.info("id of diagrep..." + diagRep.id);
			diagRep.save();
			dc.diagnosticRepresentativelist.add(diagRep);

			dc.update();
			return ok(String.format("Saved product %s", diagRep));
		}

	}

	/*
	 * displaying all diagnostic Representators
	 */
	public static Result diagnosticRepresentativeList() {

		//DiagnosticCentre diagnosticCentre = dr.diagnosticCentre;
		Logger.info("size of list........"
				+ dc.diagnosticRepresentativelist.size());
		return ok(views.html.diagnostic.diagnosticList
				.render(dc.diagnosticRepresentativelist));

	}

	/*
	 * displaying all doctors
	 */
	public static Result doctorList() {
		List<Doctor> doctorList = Doctor.find.all();
		return ok(views.html.diagnostic.doctorsList.render(doctorList));
	}

	/*
	 * add
	 */

	public static Result addDoctor(Long id) {
		/*DiagnosticRepresentative dr = LoginController.getLoggedInUser()
				.getDiagnosticRepresentative();*/
		
		Logger.info("Diagnostic centre name.." + dc.name);
		Logger.info("Diagnostic centre id.." + dc.id);
		Doctor doctor=Doctor.find.byId(id);
		if(dc.doctorList.contains(doctor)!=true){
		dc.doctorList.add(doctor);
		}
		dc.update();
		Logger.info("after adding Doctors list size is.." + dc.doctorList.size());
		// views.html.diagnostic.addDoctor.render(dc.doctorList)
		Logger.info("after adding Doctors list size is.." + dc.doctorList.get(0).appUser.name);
		return ok();

	}

	// remove doctor from DR list
	public static Result removeDoctor(final Long id) {
		
		System.out.println("id........." + id);
		Doctor doctor = Doctor.find.byId(id);
		dc.doctorList.remove(doctor);
		dc.update();
		return ok(views.html.diagnostic.addDoctor.render(dc.doctorList));

	}

	// for searching doctor
	public static Result search() {
		return ok(views.html.diagnostic.searchDoctor.render());
	}

	/*
	 * doctors search process
	 */
	public static Result searchProcess() {

		final DynamicForm requestData = Form.form().bindFromRequest();

		final String searchStr = requestData.get("searchStr");

		// if string is empty return zero
		if (searchStr != null && !searchStr.isEmpty()) {
			// it is a string, search by name
			if (searchStr.matches("[a-zA-Z]+")) {

				final List<Doctor> doctorList = Doctor.find.where()
						.like("appUser.name", searchStr + "%").findList();

				return ok(views.html.diagnostic.doctorsList.render(doctorList));
			}

		}
		return ok("no doctor present with this name");

	}

	public static Result displayDiagnosticCentreDoctors() {
		/*DiagnosticRepresentative dr = LoginController.getLoggedInUser()
				.getDiagnosticRepresentative();*/
		//DiagnosticCentre diagnosticCentre = dr.diagnosticCentre;
		Logger.info("diagnostic center name:" + dc.name);
		Logger.info("Diagnostic centre id.." + dc.id);
		Logger.info("size of list" + dc.doctorList.size());
		return ok(views.html.diagnostic.diagosticCentreDoctorsList
				.render(dr.diagnosticCentre.doctorList));
	}

	public static Result addPatient() {
		return TODO;

	}
}
