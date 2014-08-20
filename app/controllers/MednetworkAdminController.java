package controllers;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import models.Alert;
import models.AppUser;
import models.Feedback;
import models.diagnostic.DiagnosticCentre;
import models.doctor.Doctor;
import models.doctor.MasterSpecialization;
import models.patient.Patient;
import models.pharmacist.Pharmacy;
import play.Logger;
import play.mvc.Controller;
import play.mvc.Result;
import actions.BasicAuth;

@BasicAuth
public class MednetworkAdminController extends Controller {
	/**
	 * @author Lakshmi
	 * GET		/admin/specialization-list
	 * @return
	 */
	public static Result getSpecializationList(){
		final List<MasterSpecialization> masterSpecializations = MasterSpecialization.find.orderBy("name").findList();
		return ok(views.html.mednetAdmin.specializationList.render(masterSpecializations));
	}

	/**
	 * @author Lakshmi
	 * POST		/admin/add-specialization
	 * @return
	 */
	public static Result addSpecialization(){
		if(request().body().asFormUrlEncoded().get("specialization")!= null && !request().body().asFormUrlEncoded().get("specialization")[0].trim().isEmpty()){
			final String specializationName = request().body().asFormUrlEncoded().get("specialization")[0].trim();
			if(MasterSpecialization.find.where().ieq("name", specializationName).findRowCount() == 0){
				final MasterSpecialization masterSpecialization = new MasterSpecialization();
				masterSpecialization.name = specializationName;
				masterSpecialization.save();
				flash().put("alert", new Alert("alert-info",specializationName+" added.").toString());
			}
			else{
				flash().put("alert", new Alert("alert-danger",specializationName+" already exists.").toString());
			}
		}
		return redirect(routes.MednetworkAdminController.getSpecializationList());
	}


	/**
	 * @author Lakshmi
	 * POST		/admin/update-specialization
	 * @return
	 */
	public static Result updateSpecialization(){

		if(request().body().asFormUrlEncoded().get("spezId")!= null
				&& request().body().asFormUrlEncoded().get("spezName")!= null
				&& !request().body().asFormUrlEncoded().get("spezName")[0].trim().isEmpty()
				){
			final String specializationName = request().body().asFormUrlEncoded().get("spezName")[0].trim();
			if(MasterSpecialization.find.where().ieq("name", specializationName).findRowCount() == 0){
				final MasterSpecialization masterSpecialization = MasterSpecialization.find.byId(Long.parseLong(request().body().asFormUrlEncoded().get("spezId")[0]));
				masterSpecialization.name = specializationName;
				masterSpecialization.update();
				flash().put("alert", new Alert("alert-info",specializationName+" edited.").toString());
			}
			else{
				flash().put("alert", new Alert("alert-danger",specializationName+" already exists.").toString());
			}
		}
		return redirect(routes.MednetworkAdminController.getSpecializationList());
	}

	/**
	 * @author Lakshmi
	 * GET		/admin/specialization-list
	 * @return
	 */
	public static Result getFeedbackList(){
		final List<Feedback> feedbacks = Feedback.find.orderBy().desc("date").findList();
		return ok(views.html.mednetAdmin.feedbackList.render(feedbacks));
	}


	/**
	 * @author Lakshmi
	 * GET		/admin/delete-feedback/:id
	 */
	public static Result deleteFeedback(final Long id){
		Feedback.find.byId(id).delete();
		flash().put("alert", new Alert("alert-info"," Feedback deleted.").toString());
		return redirect(routes.MednetworkAdminController.getFeedbackList());
	}


	/**
	 * GET	/admin/get-user-date-between-dates/:from/:to
	 * Get count of Appusers and other roles created between :from and :to
	 */
	public static Result getUserDateBetweenDates(final String fromDate, final String toDate){
		final Map<String, Integer> map = new HashMap<String, Integer>();
		try{
			final SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
			final Date from = sdf.parse(fromDate.trim());
			final Calendar cal = Calendar.getInstance();
			cal.setTime(sdf.parse(toDate.trim()));
			cal.set(Calendar.HOUR_OF_DAY, 23);
			cal.set(Calendar.MINUTE, 59);
			cal.set(Calendar.SECOND, 59);
			cal.set(Calendar.MILLISECOND, 999);
			final Date to = cal.getTime();
			map.put("error", 0);
			map.put("appUsers", AppUser.find.where().ge("createdOn", from).le("createdOn", to).findRowCount());
			map.put("patients", Patient.find.where().ge("createdOn", from).le("createdOn", to).findRowCount());
			map.put("doctors", Doctor.find.where().ge("createdOn", from).le("createdOn", to).findRowCount());
			map.put("pharmacies", Pharmacy.find.where().ge("createdOn", from).le("createdOn", to).findRowCount());
			map.put("dc", DiagnosticCentre.find.where().ge("createdOn", from).le("createdOn", to).findRowCount());
			return ok(new JSONObject(map).toString());
		}
		catch (final Exception e){
			e.printStackTrace();
			map.put("error", -1);
			return ok(new JSONObject(map).toString());
		}
	}

}
