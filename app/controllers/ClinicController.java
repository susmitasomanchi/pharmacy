package controllers;

import models.Alert;
import models.AppUser;
import models.doctor.Clinic;
import play.libs.F.Function0;
import play.libs.F.Promise;
import play.mvc.Controller;
import play.mvc.Result;
import utils.EmailService;

public class ClinicController extends Controller{

	public static Result searchDoctorsByEmail(){
		return ok(views.html.clinic.searchDoctorsByEmailID.render(null,""));

	}
	/**
	 * @author lakshmi
	 * 
	 */

	public static Result invitationEmailToTheDoctor(final String doctorEmail){
		final Clinic clinic = LoginController.getLoggedInUser().getClinicAdminstrator().clinic;
		final AppUser appUser = AppUser.find.where().eq("email", doctorEmail).findUnique();
		final boolean result;
		Promise.promise(new Function0<Integer>() {

			// @Override
			//@Override
			@Override
			public Integer apply() {
				final boolean result1 = EmailService
						.sendClinicInvitationConfirmationEmail(appUser,clinic);
				return 0;
			}
		});
		// End of async
		flash().put(
				"alert",
				new Alert("alert-success",
						"A confirmation email has been sent to you at "
								+ appUser.email
								+ ". Kindly verify the same.").toString());
		return redirect(routes.UserController.confirmAppUserPage());

	}

}




