package controllers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import models.Alert;
import models.AppUser;
import models.SugarTracker;
import models.WeightTracker;
import actions.ConfirmAppUser;
import play.Logger;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import utils.Constants;

public class TrackerController extends Controller{

	/**
	 * @author Lakshmi
	 * Action to render the page to edit emailId and mobileNumber of loggedInUser
	 * GET	   /edit-contact-details
	 */
	public static Result appUserWeightDetails(){
		final List<WeightTracker> weightTrackers = WeightTracker.find.where().eq("appUser", LoginController.getLoggedInUser()).orderBy().desc("date").findList();
		return ok(views.html.appUserWeightDetails.render(LoginController.getLoggedInUser(),weightTrackers));
	}

	/**
	 * @author lakshmi
	 * Action to persist weight details of AppUser
	 */
	@ConfirmAppUser
	public static Result processWeightDetails(){
		final WeightTracker weightTracker = new WeightTracker();
		final Map<String, String[]> requestMap = request().body().asFormUrlEncoded();
		final Long appUserId = Long.parseLong(requestMap.get("appUserId")[0]);
		final AppUser loggedInUser = LoginController.getLoggedInUser();
		// server-side check
		if(appUserId.longValue() != loggedInUser.id.longValue()){
			//session().clear();
			session().remove(Constants.LOGGED_IN_USER_ID);
			session().remove(Constants.LOGGED_IN_USER_ROLE);

			return redirect(routes.LoginController.processLogout());
		}else{
			weightTracker.appUser = AppUser.find.byId(appUserId);
		}
		if(requestMap.get("weight")[0]!=null && requestMap.get("weight")[0].trim()!=""){
			weightTracker.weight = Float.parseFloat(requestMap.get("weight")[0]);
		}
		if(requestMap.get("date")[0]!=null && !(requestMap.get("date")[0].trim().isEmpty())){
			final String date = requestMap.get("date")[0].replaceAll(" ","").trim();
			final SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
			try {
				weightTracker.date =  sdf.parse(date);
			} catch (final ParseException e) {
				Logger.error("ERROR WHILE PARSING DOB");
				e.printStackTrace();
			}
		}
		weightTracker.save();
		return redirect(routes.TrackerController.appUserWeightDetails());
	}
	/**
	 * 
	 */
	public static Result removeAppUserDetails(final Long id){
		final WeightTracker weightTracker = WeightTracker.find.byId(id);
		weightTracker.delete();
		flash().put("alert", new Alert("alert-success", "Weight Details Are Deleted Successfully.").toString());
		return redirect(routes.TrackerController.appUserWeightDetails());
	}

	/**
	 * @author : anand
	 * 
	 * @description : this method is rendering to sugar tracker page for AppUser
	 * 
	 * url : /secure-weight-details
	 * 
	 * */
	public static Result sugarTracker(){
		final List<SugarTracker> sugarTrackers = SugarTracker.find.where().eq("appUser", LoginController.getLoggedInUser()).orderBy().desc("date").findList();
		Logger.info("hiii");
		return ok(views.html.sugarTracker.render(LoginController.getLoggedInUser(),sugarTrackers));
	}

	/*public static Result processSugarTracker(){
		final SugarTracker sugarTracker = new SugarTracker();
		final Map<String, String[]> requestMap = request().body().asFormUrlEncoded();
		final Long appUserId = Long.parseLong(requestMap.get("appUserId")[0]);
		final AppUser loggedInUser = LoginController.getLoggedInUser();
		// server-side check
		if(appUserId.longValue() != loggedInUser.id.longValue()){
			//session().clear();
			session().remove(Constants.LOGGED_IN_USER_ID);
			session().remove(Constants.LOGGED_IN_USER_ROLE);

			return redirect(routes.LoginController.processLogout());
		}else{
			sugarTracker.appUser = AppUser.find.byId(appUserId);
		}
		if(requestMap.get("sugar")[0]!=null && requestMap.get("sugar")[0].trim()!=""){
			sugarTracker.sugarLevel = Float.parseFloat(requestMap.get("sugar")[0]);
		}
		if(requestMap.get("date")[0]!=null && !(requestMap.get("date")[0].trim().isEmpty())){
			final String date = requestMap.get("date")[0].replaceAll(" ","").trim();
			final SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
			try {
				sugarTracker.date =  sdf.parse(date);
			} catch (final ParseException e) {
				Logger.error("ERROR WHILE PARSING DOB");
				e.printStackTrace();
			}
		}
		sugarTracker.save();
		return redirect(routes.TrackerController.sugarTracker());
	}
*/

}
