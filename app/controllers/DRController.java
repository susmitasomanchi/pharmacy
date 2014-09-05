package controllers;

import java.util.List;

import models.AppUser;
import models.diagnostic.DiagnosticCentre;
import models.diagnostic.DiagnosticRepresentative;
import models.doctor.Doctor;
import play.Logger;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;

public class DRController extends Controller {

	public static DiagnosticRepresentative dr = LoginController.getLoggedInUser()
			.getDiagnosticRepresentative();
	public static Form<AppUser> registrationForm = Form.form(AppUser.class);
	public static DiagnosticCentre dc = dr.diagnosticCentre;
	public static Form<DiagnosticRepresentative> diagnosticRepresentative = Form
			.form(DiagnosticRepresentative.class);

	/**
	 * @author : lakshmi
	 * /diagnosticrep
	 * rendering to DiagnosticRepresentative form
	 */
	public static Result addDiagRep() {
		return ok(views.html.diagnostic.diagnosticRep
				.render(diagnosticRepresentative));
	}

	/**
	 * @author : lakshmi
	 * POST/diagnosticrep/save
	 * persisiting the diagnosticRepresentative in diagnosticCentre
	 */
	public static Result addDiagRepProcess() {
		final Form<AppUser> filledForm = registrationForm.bindFromRequest();

		if (filledForm.hasErrors()) {
			//return badRequest(views.html.registerAppUser.render(filledForm));
			return ok();
		} else {
			final AppUser appUser = filledForm.get();
			final DiagnosticRepresentative diagnosticRepresentative = new DiagnosticRepresentative();
			appUser.save();
			diagnosticRepresentative.appUser = appUser;
			dc.diagnosticRepresentativelist.add(diagnosticRepresentative);
			dc.update();

			return ok("Registerd ");
		}

	}

	/**
	 * @author : lakshmi
	 * GET/diagnosticrep/list
	 * displaying all diagnostic Representators belongs to the logged in diagnostic centre
	 */
	public static Result diagnosticRepresentativeList() {
		Logger.info("size of list........"
				+ dc.diagnosticRepresentativelist.size());
		return ok(views.html.diagnostic.diagnosticList
				.render(dc.diagnosticRepresentativelist));

	}
	/**
	 * @author : lakshmi
	 * GET /diagnosticrep/add-doctor/:id
	 * adding a doctor to the DiagnosticCentre
	 */
	public static Result addDoctor(final Long id) {
		Logger.info("test1");

		final DiagnosticCentre diagCentre = LoginController.getLoggedInUser()
				.getDiagnosticRepresentative().diagnosticCentre;
		Logger.info("test2");

		final Doctor doctor=Doctor.find.byId(id);
		Logger.info("test3");
		if(diagCentre.doctorList.contains(doctor)!=true){
			diagCentre.doctorList.add(doctor);
		}
		Logger.info("test4");
		diagCentre.update();
		Logger.info("after adding Doctors list size is.." + diagCentre.doctorList.size());
		// views.html.diagnostic.addDoctor.render(dc.doctorList)
		Logger.info("after adding Doctors list size is.." + diagCentre.doctorList.get(0).appUser.name);
		return ok();
	}

	/**
	 * @author : lakshmi
	 * GET/diagnosticrep/remove-doctor/:id
	 * removing a doctor from the DiagnosticCentre
	 */
	public static Result removeDoctor(final Long id) {
		System.out.println("id........." + id);
		final Doctor doctor = Doctor.find.byId(id);
		dc.doctorList.remove(doctor);
		dc.update();
		return ok(views.html.diagnostic.addDoctor.render(dc.doctorList));

	}


	/**
	 * @author : lakshmi
	 * GET /diagnosticrep/doctor-search
	 * rendering page to search a doctor
	 */
	public static Result search() {
		return ok(views.html.diagnostic.searchDoctor.render());
	}
	/**
	 * @author : lakshmi
	 * POST /diagnosticrep/doctorsearch
	 * searchProcess for a Doctor
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

	/**
	 * @author : lakshmi
	 * GET /diagnostic-centre/all-doctors
	 * displying all doctors belongs to the loggedin ADMIN_DIAGREP
	 */
	public static Result displayDiagnosticCentreDoctors() {
		final DiagnosticCentre diagCentre = LoginController.getLoggedInUser().getDiagnosticRepresentative().diagnosticCentre;
		Logger.info("diagnostic center name:" + diagCentre.name);
		Logger.info("Diagnostic centre id.." + diagCentre.id);
		Logger.info("size of list" + diagCentre.doctorList.size());
		return ok(views.html.diagnostic.diagosticCentreDoctorsList.render(diagCentre.doctorList));
	}

}
