package controllers;


import beans.LoginBean;
import models.DiagnosticRepresentative;
import models.MedicalRepresentative;
import play.Logger;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;

public class Application extends Controller {

	public static Form<MedicalRepresentative> mrForm=Form.form(MedicalRepresentative.class);
	public static Form<DiagnosticRepresentative> diagnosticRepForm=Form.form(DiagnosticRepresentative.class);
	public static final Form<LoginBean> loginForm = Form.form(LoginBean.class);

	public static Result index() {
		return ok(views.html.index.render(loginForm));
	}

	public static Result indexX(final String str) {
		return ok(views.html.index.render(loginForm));
	}

	//sales representator proccessing

	public static Result medicalRepresentative(){
		return ok(views.html.mr.medicalRepresentative.render(mrForm));

	}

	public static Result medicalRepresentativeProccess(){
		final Form<MedicalRepresentative> filledForm=mrForm.bindFromRequest();

		if(filledForm.hasErrors()) {
			Logger.info("*** user bad request");
			return badRequest(views.html.mr.medicalRepresentative.render(filledForm));
		}
		else {
			//final SalesRep salesRepForm = filledForm.get();
			final MedicalRepresentative appUser = filledForm.get();
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

		return ok(views.html.diagnostic.diagnosticRep.render(diagnosticRepForm));

	}

	public static Result  diagnosticRepProccess(){
		final Form<DiagnosticRepresentative> filledForm=diagnosticRepForm.bindFromRequest();

		if(filledForm.hasErrors()) {
			Logger.info("*** user bad request");
			return badRequest(views.html.diagnostic.diagnosticRep.render(filledForm));
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


	public static Result sitemap(){
		return ok(views.xml.sitemap.render("http://", request().host()));
	}


}
