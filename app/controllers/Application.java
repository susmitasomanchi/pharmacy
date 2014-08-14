package controllers;


import models.diagnostic.DiagnosticRepresentative;
import models.mr.MedicalRepresentative;
import play.Logger;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import beans.JoinUsBean;
import beans.LoginBean;

public class Application extends Controller {

	public static Form<JoinUsBean> joinUsForm=Form.form(JoinUsBean.class);
	public static Form<DiagnosticRepresentative> diagnosticRepForm=Form.form(DiagnosticRepresentative.class);
	public static final Form<LoginBean> loginForm = Form.form(LoginBean.class);
	public static Form<MedicalRepresentative> mrForm=Form.form(MedicalRepresentative.class);

	public static Result index() {
		//return ok(views.html.comingsoon.render(loginForm));
		return ok(views.html.home.render(loginForm));
	}

	public static Result indexX(final String str) {
		return redirect(routes.Application.index());
	}


	/*public static Result medicalRepresentative(){
		return ok(views.html.mr.medicalRepresentative.render(mrForm));

	}*/

	/*public static Result medicalRepresentativeProccess(){
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
	}*/
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
		return ok(views.html.home.render(loginForm));
	}


	/**
	 * Renders sitemap xml
	 * url: /sitemap.xml
	 * */
	public static Result sitemap(){
		return ok(views.xml.sitemap.render("http://"));
	}


}
