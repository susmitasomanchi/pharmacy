package controllers;

import java.util.List;

import actions.BasicAuth;
import models.Alert;
import models.Feedback;
import models.doctor.MasterSpecialization;
import play.mvc.Controller;
import play.mvc.Result;

@BasicAuth
public class MednetworkAdminController extends Controller {
	/**
	 * @author Lakshmi
	 * GET		/admin/specialization-list
	 * @return
	 */
	public static Result getSpecializationList(){
		final List<MasterSpecialization> masterSpecializations = MasterSpecialization.find.all();
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

}
