package controllers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import models.Alert;
import models.AppUser;
import models.BloodPressureTracker;
import models.SugarTracker;
import models.WeightTracker;
import play.Logger;
import play.mvc.Controller;
import play.mvc.Result;
import utils.Constants;
import actions.ConfirmAppUser;

public class TrackerController extends Controller{

	/**
	 * @author Lakshmi
	 * Action to render the page for weight details of loggedInUser
	 * GET		/secure-weight-details
	 */
	public static Result appUserWeightTracker(){
		final List<WeightTracker> weightTrackers = WeightTracker.find.where().eq("appUser", LoginController.getLoggedInUser()).orderBy().desc("date").findList();
		return ok(views.html.appUserWeightDetails.render(LoginController.getLoggedInUser(),weightTrackers));
	}

	/**
	 * @author lakshmi
	 * Action to persist weight details of AppUser
	 * POST		/secure-weight-details
	 */
	@ConfirmAppUser
	public static Result processWeightTracker(){
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
		return redirect(routes.TrackerController.appUserWeightTracker());
	}
	/**
	 * @author lakshmi
	 * Action to remove weight details of loggedinUser
	 * GET		/secure-remove-weight-details/:id
	 */
	public static Result removeAppUserWeightDetails(final Long id){
		final WeightTracker weightTracker = WeightTracker.find.byId(id);
		weightTracker.delete();
		flash().put("alert", new Alert("alert-danger", "Weight Details Are Deleted Successfully.").toString());
		return redirect(routes.TrackerController.appUserWeightTracker());
	}


	/**
	 * @author Lakshmi
	 * Action to render the page for BloodPressure Deatails of loggedInUser
	 * GET/secure-bp-details
	 */
	public static Result appUserBpTracker(){
		final List<BloodPressureTracker> bloodPressureTrackers = BloodPressureTracker.find.where().eq("appUser", LoginController.getLoggedInUser()).orderBy().desc("date").findList();
		return ok(views.html.appUserBpDetails.render(LoginController.getLoggedInUser(),bloodPressureTrackers));
	}
	/**
	 * @author lakshmi
	 * Action to persist weight details of AppUser
	 * POST/secure-bp-details
	 */
	@ConfirmAppUser
	public static Result processBpTracker(){
		final BloodPressureTracker bloodPressureTracker = new BloodPressureTracker();
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
			bloodPressureTracker.appUser = AppUser.find.byId(appUserId);
		}
		if(requestMap.get("lowBp")[0]!=null && requestMap.get("lowBp")[0].trim()!=""){
			bloodPressureTracker.lowBp = Float.parseFloat(requestMap.get("lowBp")[0]);
		}
		if(requestMap.get("highBp")[0]!=null && requestMap.get("highBp")[0].trim()!=""){
			bloodPressureTracker.highBp = Float.parseFloat(requestMap.get("highBp")[0]);
		}
		if(requestMap.get("date")[0]!=null && !(requestMap.get("date")[0].trim().isEmpty())){
			final String date = requestMap.get("date")[0].replaceAll(" ","").trim();
			final SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
			try {
				bloodPressureTracker.date =  sdf.parse(date);
			} catch (final ParseException e) {
				Logger.error("ERROR WHILE PARSING DOB");
				e.printStackTrace();
			}
		}
		bloodPressureTracker.save();
		return redirect(routes.TrackerController.appUserBpTracker());
	}

	/**
	 * @author lakshmi
	 * Action to remove BloodPressure details of loggedinUser
	 * GET		/secure-remove-bp-details/:id
	 */
	public static Result removeAppUserBpDetails(final Long id){
		final BloodPressureTracker bloodPressureTracker = BloodPressureTracker.find.byId(id);
		bloodPressureTracker.delete();
		flash().put("alert", new Alert("alert-danger", "Blood Pressure Details Are Deleted Successfully.").toString());
		return redirect(routes.TrackerController.appUserWeightTracker());
	}
	/**
	 * @author : anand
	 * 
	 * @description : this method is rendering to sugar tracker page for AppUser
	 * 
	 * url : GET  /secure-sugar-tracker
	 * 
	 * */
	public static Result sugarTracker(){
		final List<SugarTracker> sugarTrackers = SugarTracker.find.where().eq("appUser", LoginController.getLoggedInUser()).orderBy().desc("date").findList();
		return ok(views.html.sugarTracker.render(LoginController.getLoggedInUser(),sugarTrackers));
	}
	/**
	 * @author : anand
	 * 
	 * @description : this method is use to store sugarContent to database
	 * 
	 * url : POST /secure-sugar-tracker-process
	 * 
	 * */

	public static Result processSugarTracker(){
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
			final SimpleDateFormat sdf = new SimpleDateFormat("dd-mm-yyyy");
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
	/**
	 * @author lakshmi
	 * Action to remove BloodPressure details of loggedinUser
	 * GET		/secure-remove-bp-details/:id
	 */
	public static Result removeAppUserSugarDetails(final Long id){
		final SugarTracker sugarTracker = SugarTracker.find.byId(id);
		sugarTracker.delete();
		flash().put("alert", new Alert("alert-danger", "Sugar Details Are Deleted Successfully.").toString());
		return redirect(routes.TrackerController.sugarTracker());
	}
}
