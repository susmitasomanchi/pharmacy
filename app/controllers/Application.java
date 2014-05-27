package controllers;


import models.AppUser;
import models.SalesRep;
import beans.AppUserBean;
import beans.SalesRepBean;
import play.Logger;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.index;

public class Application extends Controller {
	
	public static Form<SalesRepBean> salesRepForm=Form.form(SalesRepBean.class);


    public static Result index() {
        return ok(views.html.index.render("Your new application is ready."));
    }
    
    public static Result salesRepresentator(){
    	final Form<SalesRepBean> filledForm=salesRepForm.bindFromRequest();
    	
    			if(filledForm.hasErrors()) {
    				Logger.info("*** user bad request");
    				return badRequest(views.html.salesRep.render(filledForm));
    			}
    			else {
    				final SalesRepBean salesRepForm = filledForm.get();
    				Logger.info("*** user object ");

    				if(salesRepForm.id == null) {
    					final AppUser appUser = salesRepForm.toEntity();
    					appUser.save();
    					final String message = flash("success");

    				}
    				else {
    					salesRepForm.toEntity().update();
    				}
    			}
    	
    	
    	return ok("salesrepresentr information added");
    }

}
