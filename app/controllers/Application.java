package controllers;


import models.AppUser;
import models.DiagnosticRepresentative;
import models.MedicalRepresentative;
import play.Logger;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;

public class Application extends Controller {

	public static Form<MedicalRepresentative> salesRepForm=Form.form(MedicalRepresentative.class);
	public static Form<DiagnosticRepresentative> diagnosticRepForm=Form.form(DiagnosticRepresentative.class);


	public static Result index() {
		return ok(views.html.index.render("Your new application is ready."));
	}

	//sales representator proccessing

	public static Result salesRepresentator(){
		return ok(views.html.salesRep.render(salesRepForm));

	}

	public static Result salesRepresentatorProccess(){
		final Form<MedicalRepresentative> filledForm=salesRepForm.bindFromRequest();

		if(filledForm.hasErrors()) {
			Logger.info("*** user bad request");
			return badRequest(views.html.salesRep.render(filledForm));
		}
		else {
			//final SalesRep salesRepForm = filledForm.get();
			final AppUser appUser = filledForm.get();
			Logger.info("*** user object ");

			if(appUser.id == null) {
				//final AppUser appUser = salesRepForm.toEntity();
				appUser.save();
				final String message = flash("success");

			}
			else {
				appUser.update();
			}
		}


		return ok("salesrepresentr information added");
	}

	//diagnostic representator proccessing


	public static Result diagnosticRep(){

		return ok(views.html.diagnosticRep.render(diagnosticRepForm));

	}

	public static Result  diagnosticRepProccess(){
		final Form<DiagnosticRepresentative> filledForm=diagnosticRepForm.bindFromRequest();

		if(filledForm.hasErrors()) {
			Logger.info("*** user bad request");
			return badRequest(views.html.diagnosticRep.render(filledForm));
		}
		else {
			final DiagnosticRepresentative diagnosticRepForm = filledForm.get();
			Logger.info("*** user object ");

			if(diagnosticRepForm.id == null) {
				//final AppUser appUser = salesRepForm.toEntity();
				diagnosticRepForm.save();
				final String message = flash("success");

			}
			else {
				diagnosticRepForm.update();
			}
		}

		return ok("Diagnostic Representr information added");
	}


	//home Page

	public static Result homePage(){
		return ok(views.html.home.render());
	}
}
