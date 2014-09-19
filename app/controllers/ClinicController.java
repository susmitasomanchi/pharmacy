package controllers;

import java.util.List;

import actions.BasicAuth;
import models.Alert;
import models.AppUser;
import models.Role;
import models.doctor.Clinic;
import models.doctor.Doctor;
import models.doctor.DoctorClinicInfo;
import play.Logger;
import play.libs.F.Function0;
import play.libs.F.Promise;
import play.mvc.Controller;
import play.mvc.Result;
import utils.EmailService;

@BasicAuth
public class ClinicController extends Controller{

	public static Result searchDoctorsByEmail(){
		return ok(views.html.clinic.inviteDoctorsByEmailID.render());
	}

	/**
	 * @author lakshmi
	 * Action to invitation to the doctor
	 * GET    /secure-clinic/invite-doctor/:email
	 */
	public static Result invitationEmailToTheDoctor(final String doctorEmail){
		final Clinic clinic = LoginController.getLoggedInUser().getClinicAdminstrator().clinic;
		final AppUser appUser = AppUser.find.where().eq("email", doctorEmail).findUnique();
		if((appUser != null) && (appUser.role.equals(Role.DOCTOR))){
			final int count = DoctorClinicInfo.find.where().eq("clinic", clinic).eq("doctor", appUser.getDoctor()).findRowCount();
			if(count>0){
				flash().put("alert",new Alert("alert-info"," Dr."+appUser.name+" is already associated with clinic "+clinic.name).toString());
			}else{
				final boolean result;
				Promise.promise(new Function0<Integer>() {
					// @Override
					//@Override
					@Override
					public Integer apply() {
						final boolean result1 = EmailService.sendClinicInvitationConfirmationEmail(appUser,clinic);
						return 0;
					}
				});
				// End of async
				flash().put("alert",new Alert("alert-success","An Invitation has been sent to the Dr."+appUser.name).toString());
			}
		}else{
			flash().put("alert",new Alert("alert-info","With "+doctorEmail+" no Doctor found").toString());
		}
		return redirect(routes.ClinicController.searchDoctorsByEmail());

	}
	/**
	 * @author lakshmi
	 * Action to add doctor to the clinic
	 * GET		/secure-clinic/add-doctor/:docId
	 */
	public static Result addDoctorToTheClinic(final Long docId){
		final Clinic clinic = LoginController.getLoggedInUser().getClinicAdminstrator().clinic;
		final Doctor doctor = Doctor.find.byId(docId);
		final DoctorClinicInfo doctorClinicInfo = new DoctorClinicInfo();
		doctorClinicInfo.clinic = clinic;
		doctorClinicInfo.doctor = doctor;
		doctorClinicInfo.save();

		final AppUser appUser = doctor.appUser;
		final boolean result;
		Promise.promise(new Function0<Integer>() {

			// @Override
			//@Override
			@Override
			public Integer apply() {
				final boolean result1 = EmailService
						.sendClinicInvitationSuccessEmail(appUser,clinic);
				return 0;
			}
		});
		// End of async
		return redirect(routes.ClinicController.searchDoctorsByEmail());
	}
	/**
	 * @author lakshmi
	 * Action to add doctor to the clinic
	 * GET		/secure-clinic/add-doctor/:docId
	 */
	public static Result getDoctors(){
		final Clinic clinic = LoginController.getLoggedInUser().getClinicAdminstrator().clinic;
		final List<DoctorClinicInfo> doctorClinicInfos = DoctorClinicInfo.find.where().eq("clinic", clinic).findList();
		Logger.info("size of the list "+doctorClinicInfos.size());
		return ok(views.html.clinic.clinicDoctors.render(doctorClinicInfos));
	}

}




