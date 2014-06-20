package controllers;


import beans.JoinUsBean;
import beans.LoginBean;
import models.AppUser;
import models.DiagnosticRepresentative;
import models.MedicalRepresentative;
import models.PharmaceuticalCompany;
import play.Logger;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;

public class Application extends Controller {

	public static Form<JoinUsBean> joinUsForm=Form.form(JoinUsBean.class);
	public static Form<DiagnosticRepresentative> diagnosticRepForm=Form.form(DiagnosticRepresentative.class);
	public static final Form<LoginBean> loginForm = Form.form(LoginBean.class);

	public static Result index() {
		return ok(views.html.index.render(loginForm));
	}

	//sales representator proccessing

	//public static Result medicalRepresentative(){
		//return ok(views.html.mr.medicalRepresentative.render(joinUsForm));

	//}

	/*public static Result medicalRepresentativeProccess(){
		final Form<JoinUsBean> filledForm=joinUsForm.bindFromRequest();
		
		if(filledForm.hasErrors()) {
			Logger.info("*** user bad request");
			return badRequest(views.html.mr.medicalRepresentative.render(filledForm));
		}
		else {
			final AppUser appUser = filledForm.get().toAppUser();
			appUser.save();
			
			final MedicalRepresentative medicalRepresentative = new MedicalRepresentative();
			if(medicalRepresentative.id != null) {	
			medicalRepresentative.appUser = appUser;
			medicalRepresentative.save();
			final PharmaceuticalCompany pharmaCompany = new PharmaceuticalCompany();
			pharmaCompany.name = filledForm.get().pharmaceuticalCompanyName;
			pharmaCompany.adminMR = medicalRepresentative;
			pharmaCompany.save();
			Logger.info("*** user object ");

			
				
				final String message = flash("success");

			}
			else {
				medicalRepresentative.update();
			}
		}


		return ok("Medical representative information added");
	}
*/
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
