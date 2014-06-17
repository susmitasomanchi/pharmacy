package controllers;

import java.util.List;


import models.DiagnosticRepresentative;
import models.Doctor;
import play.Logger;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;

public class DRController extends Controller {
	static DiagnosticRepresentative loggedInDR = LoginController
			.getLoggedInUser().getDiagnosticRepresentative();
	public static Form<DiagnosticRepresentative> diagnosticRepresentative = Form
			.form(DiagnosticRepresentative.class);

	public static Result addDiagRep() {
		return ok(views.html.diagnostic.diagnosticRep
				.render(diagnosticRepresentative));
	}

	public static Result addDiagRepProcess() {
		final Form<DiagnosticRepresentative> filledForm = diagnosticRepresentative
				.bindFromRequest();

		if (filledForm.hasErrors()) {
			Logger.info("*** user bad request");
			return badRequest(views.html.diagnostic.diagnosticRep
					.render(filledForm));
		} else {

			final DiagnosticRepresentative diagForm = filledForm.get();
			Logger.info("*** user object ");
			diagForm.save();
			final String message = flash("success");
			return ok("Saved");
		}
	}

	public static Result diagnosticReplist() {
		List<DiagnosticRepresentative> allDiagRepList = DiagnosticRepresentative.find
				.all();
		/* views.html.list.render(allList) */
		Logger.info(allDiagRepList.get(1).appUser.name + "~~allDiagRepList");
		return ok(views.html.diagnostic.diagnosticList.render(allDiagRepList));

	}

	public static Result doctorList() {
		List<Doctor> doctorList = Doctor.find.all();
		// Logger.error(doctorList.get(0).appUser.name);
		return ok(views.html.diagnostic.doctorsList.render(doctorList));
	}

	public static Result addDoctor(Long id) {
		Logger.info("id.............." + id);

		if (loggedInDR.doctorList.contains(Doctor.find.byId(id)) != true) {
			loggedInDR.doctorList.add(Doctor.find.byId(id));
		}

		return ok(views.html.diagnostic.addDoctor.render(loggedInDR.doctorList));

	}

	// delete doctor from DR list
	public static Result removeDoctor(final Long id) {
		System.out.println("id........." + id);
		int indexOfDoctorList = -1;
		Doctor doctor = Doctor.find.byId(id);
		for (Doctor doc : loggedInDR.doctorList) {
			indexOfDoctorList++;
			if (doctor.appUser.name.equals(doc.appUser.name)) {
				Logger.info("doctor name : " + doc.appUser.name);

				// indexOfDoctorList=loggedInMR.doctorList.indexOf(doctor.appUser.name);

				Logger.info("index is : " + indexOfDoctorList);
				break;
			}
		}

		// return TODO;
		loggedInDR.doctorList.remove(indexOfDoctorList);

		return ok(views.html.diagnostic.addDoctor.render(loggedInDR.doctorList));

	}

	// for searching doctor
	public static Result search() {
		return ok(views.html.diagnostic.searchDoctor.render());
	}

	public static Result searchProcess() {

		final DynamicForm requestData = Form.form().bindFromRequest();

		final String searchStr = requestData.get("searchStr");

		Logger.info("hello..........................." + searchStr);
		// if string is empty return zero
		if (searchStr != null && !searchStr.isEmpty()) {
			Logger.info("hello...........................");
			// it is a string, search by name
			if (searchStr.matches("[a-zA-Z]+")) {
				Logger.info("inside...........................");

				final List<Doctor> doctorList = Doctor.find.where()
						.like("appUser.name", searchStr + "%").findList();

				return ok(views.html.diagnostic.doctorsList.render(doctorList));
			}

		}
		return ok("no doctor present with this name");

	}

	public static Result addPatient() {
		return TODO;
		
	}
}
