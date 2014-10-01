package controllers;

import java.util.List;

import org.json.JSONObject;

import models.Alert;
import models.AppUser;
import play.Logger;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import utils.Constants;

public class WebServices extends Controller{

	public static Result login(final String email , final String password) {

		//session().clear(); Cannot clear session() as its used to store Primary City Id
		/*session().remove(Constants.LOGGED_IN_USER_ID);
		session().remove(Constants.LOGGED_IN_USER_ROLE);
		 */
		final List<AppUser> appUsers = AppUser.find.where().eq("email",email.trim().toLowerCase()).findList();
		Logger.info("found appUsers: " + appUsers.size());
		Logger.info("email :  :"+email);
		Logger.info("password :"+password);
		if(appUsers.size() == 1) {
			if(appUsers.get(0).matchPassword(password.trim())){
				/*session(Constants.LOGGED_IN_USER_ID, appUsers.get(0).id + "");
				session(Constants.LOGGED_IN_USER_ROLE, appUsers.get(0).role+ "");*/
				final String token = email+":"+password;
				final JSONObject obj = new JSONObject();
				obj.put("email", email);
				obj.put("password", password);
				obj.put("status","TRUE");
				Logger.info(""+obj);
				return ok("{status:TRUE}");
				//return ok("{\"status\":\"TRUE\"}");
			}else{
				Logger.error("Invalid password.");
				return ok("{\"status\":\"FALSE\"}");
			}

		}else{
			// return invalid login/password
			Logger.error("Invalid username.");
			flash().put("alert", new Alert("alert-danger", "Sorry! Invalid Username/Password.").toString());
			return ok("{\"status\":\"FALSE\"}");
		}
	}

	public static Result logout() {
		//session().clear();
		session().remove(Constants.LOGGED_IN_USER_ID);
		Logger.info("logout successful");
		return ok("{\"status\":\"TRUE\"}");
	}

	public static Result myDoctor(){
		Logger.info(session(Constants.LOGGED_IN_USER_ID));
		//final AppUser loggedInAppUser=LoginController.getLoggedInUser();
		/*final MedicalRepresentative loggedInMr = LoginController
				.getLoggedInUser().getMedicalRepresentative();
		Logger.info("server side method executed");
		final List<String> docList = new ArrayList<String>();
		for (final Doctor doctor : loggedInMr.doctorList) {
			docList.add(doctor.appUser.name);
		}
		session().remove(Constants.LOGGED_IN_USER_ID);
		 */Logger.info("Server side code executed");
		 return ok("{\"status\":\"TRUE\"}");

		 //return ok(Json.toJson(docList));

		 /*if(request.format.equals("json"))
			return ok(Json.toJson(loggedInMr.doctorList));
		else
			return ok(loggedInMr.doctorList);
		  *///return ok(Json.toJson(loggedInMr.doctorList));

	}
	/*public static Result myProduct(){
		final MedicalRepresentative loggedInMr = LoginController
				.getLoggedInUser().getMedicalRepresentative();
		Logger.info("server side method executed");
		return ok(Json.toJson(loggedInMr.pharmaceuticalCompany.pharmaceuticalProductList));
	}
	public static Result dcrLineItem(final String date,final String doctor,final List<String> sampleList, final List<String> promotion,final String pob){
		final MedicalRepresentative loggedInMr = LoginController
				.getLoggedInUser().getMedicalRepresentative();
		final DCRLineItem dcrLineItem = new DCRLineItem();
		//dcrLineItem.doctor = ;
		final Sample sample = new Sample();
		for (final String smpl : sampleList) {
		}
		for(final String promo: promotion){

		}
		dcrLineItem.pob = Integer.parseInt(pob);
		final DailyCallReport dcr = new DailyCallReport();
		final SimpleDateFormat sdf = new SimpleDateFormat();

		try {
			dcr.forDate = sdf.parse(date);
		} catch (final ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		};
		dcr.dcrLineItemList.add(dcrLineItem);
		loggedInMr.dcrList.add(dcr);
		return ok("{\"status\":\"TRUE\"}");
	}
	 */


}
