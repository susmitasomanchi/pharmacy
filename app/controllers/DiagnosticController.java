package controllers;

import java.util.List;

import models.DiagnosticCenter;
import play.Logger;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;

public class DiagnosticController extends Controller{
	public static Form<DiagnosticCenter> diagnosticForm=Form.form(DiagnosticCenter.class);
	public static Result  diagnosticCenter(){
		return ok(views.html.diagnosticCenters.render(diagnosticForm));
		}
	public static Result  diagnosticCenterProcess(){
		final Form<DiagnosticCenter> filledForm=diagnosticForm.bindFromRequest();

		if(filledForm.hasErrors()) {
			Logger.info("*** user bad request");
			return badRequest(views.html.diagnosticCenters.render(filledForm));
		}
		else {
			//final SalesRep salesRepForm = filledForm.get();
			final DiagnosticCenter diagForm = filledForm.get();
			Logger.info("*** user object ");

				//final AppUser appUser = salesRepForm.toEntity();
			diagForm.save();
				final String message = flash("success");

			
				return ok(String.format("Saved product %s", diagForm));
	}
		
	}
	public static Result Diagnosticlist() {
		List<DiagnosticCenter> allList=DiagnosticCenter.all();
		/*views.html.list.render(allList)*/
		return ok(views.html.diagnosticList.render(allList));

	}
	/*public static Result search(){
		Form form= Form.form().bindFromRequest();
		String name=form.get("search");
		List<DiagnosticCenterForm> c1=DiagnosticCenterForm.getDetails(name);
		return ok(views.html.getlist.render(c));
	}
*/
	public static Result deleteCenter(Long id) {
		 DiagnosticCenter.delete(id);
		  return ok("deleted successfully");
	  }

}
