package controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	public static Result diagnosticlist() {
		List<DiagnosticCenter> allList=DiagnosticCenter.all();
		
		return ok(views.html.diagnostic.diagnosticCenterList.render(allList));

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
	 * searching the diagnostic center by
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
	public static Result diagnosticServiceslist(Long id) {
		DiagnosticCenter allList=DiagnosticCenter.find.where().eq("id", id).findUnique();
		DiagnosticCenter allList1=DiagnosticCenter.find.select("services").where().eq("id", id).findUnique();
		Logger.info("allList1"+allList1.services);
		Logger.info("allList1"+allList1.costOfServices);
		
		Map<String,String> map=new HashMap<>();		
		String[] services=allList.services.split(",");
		String[] cost=allList.costOfServices.split(",");	
		for(int i=0;i<services.length;i++){
			Logger.info(services[i]+"--------------------"+cost[i]);
			map.put(services[i],cost[i]);
		}
		
		
		return ok(views.html.diagnostic.diagnosticServicesList.render(map));

	}


}
