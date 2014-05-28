package controllers;


import models.AppUser;
import models.DiagnosticRep;
import models.SalesRep;
import play.Logger;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.index;

public class Application extends Controller {
	
	public static Form<SalesRep> salesRepForm=Form.form(SalesRep.class);
	public static Form<DiagnosticRep> diagnosticRepForm=Form.form(DiagnosticRep.class);


    public static Result index() {
        return ok(views.html.index.render("Your new application is ready."));
    }
    
    //sales representator proccessing
    
    public static Result salesRepresentator(){
    	return ok(views.html.salesRep.render(salesRepForm));
    	
    }
    
    public static Result salesRepresentatorProccess(){
    	final Form<SalesRep> filledForm=salesRepForm.bindFromRequest();
    	
    			if(filledForm.hasErrors()) {
    				Logger.info("*** user bad request");
    				return badRequest(views.html.salesRep.render(filledForm));
    			}
    			else {
    				final SalesRep salesRepForm = filledForm.get();
    				Logger.info("*** user object ");

    				if(salesRepForm.id == null) {
    					//final AppUser appUser = salesRepForm.toEntity();
    					salesRepForm.save();
    					final String message = flash("success");

    				}
    				else {
    					salesRepForm.update();
    				}
    			}
    	
    	
    	return ok("salesrepresentr information added");
    }

    //diagnostic representator proccessing
    
    
    public static Result diagnosticRep(){
    	
    	return ok(views.html.diagnosticRep.render(diagnosticRepForm));
    	
    }
   
    public static Result  diagnosticRepProccess(){
        final Form<DiagnosticRep> filledForm=diagnosticRepForm.bindFromRequest();
    	
		if(filledForm.hasErrors()) {
			Logger.info("*** user bad request");
			return badRequest(views.html.diagnosticRep.render(filledForm));
		}
		else {
			final DiagnosticRep diagnosticRepForm = filledForm.get();
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
}
