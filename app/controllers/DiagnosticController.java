package controllers;

import java.util.List;

import models.DiagnosticCenter;
import play.Logger;
import play.data.DynamicForm;
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
		return ok(views.html.diagnostic.diagnosticList.render(allList));

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
	
	/*
	 * editing the 
	 * diagnostic center details
	 */
	public static Result editDiagnosticDetails(Long id) {
		final DiagnosticCenter dc = DiagnosticCenter.find.byId(id);
		final Form<DiagnosticCenter> filledForm = diagnosticForm.fill(dc);
		final DiagnosticCenter diagForm = filledForm.get();
		diagForm.update();
		return ok(views.html.diagnostic.diagnosticReg.render(filledForm));
	}
	public static Result diagnosticEditprocess() {
		final Form<DiagnosticCenter> filledForm = diagnosticForm.bindFromRequest();
		if(filledForm.hasErrors()) {
			return badRequest(views.html.diagnostic.diagnosticReg.render(filledForm));
		}
		else {
			final DiagnosticCenter dc = filledForm.get();

			if(dc.id == null) {
				dc.save();

			} else {
				dc.update();
			}
		}
		return ok();
	}
	
	/*
	 * searching the diagnostic ceter by
	 *  name ,mobile no &email id
	 */
	public static Result diagnosticSearch(){

		final DynamicForm requestData = Form.form().bindFromRequest();

		final String searchStr = requestData.get("searchStr");

		// if string is empty return zero
		if (searchStr != null && !searchStr.isEmpty()) {

			// it is a string, search by name
			if (searchStr.matches("[a-zA-Z]+")) {

				final List<DiagnosticCenter> dcSearch = DiagnosticCenter.find.where()
						.like("name", searchStr+"%").findList();

				return ok(views.html.diagnostic.diagnosticSearch.render(dcSearch));
			}
			// if it is an email
			else if (searchStr.contains("@")) {

				final List<DiagnosticCenter> dcSearch = DiagnosticCenter.find.where()
						.eq("emailId", searchStr).findList();

				return ok(views.html.diagnostic.diagnosticSearch.render(dcSearch));
			}// if it is a number
			else {
				final List<DiagnosticCenter> dcSearch = DiagnosticCenter.find.where()
						.eq("mobileNo", searchStr).findList();

				return ok(views.html.diagnostic.diagnosticSearch.render(dcSearch));
			}

		}

		else {

			return ok();
		}
		
	}

}
