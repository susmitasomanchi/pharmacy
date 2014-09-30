package controllers;

import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.apache.commons.codec.binary.Base64;

import actions.BasicAuth;
import actions.ConfirmAppUser;
import models.Alert;
import models.AppUser;
import models.Role;
import models.clinic.ClinicInvite;
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
	 * POST    /secure-clinic/invite-doctor
	 */
	public static Result processSeachDoctorsByEmailId(){
		final String email = request().body().asFormUrlEncoded().get("email")[0].trim();
		final String confirmEmail = request().body().asFormUrlEncoded().get("confirmEmail")[0].trim();

		if(email.compareToIgnoreCase(confirmEmail) != 0){
			flash().put("alert", new Alert("alert-danger","Email Ids do not match").toString());
			return ClinicController.searchDoctorsByEmail();
		}

		final Clinic clinic = LoginController.getLoggedInUser().getClinicAdminstrator().clinic;
		final AppUser appUser = AppUser.find.where().ieq("email", email).findUnique();
		if((appUser != null) && (appUser.role.equals(Role.DOCTOR))){
			final int count = DoctorClinicInfo.find.where().eq("clinic", clinic).eq("doctor", appUser.getDoctor()).findRowCount();
			if(count>0){
				flash().put("alert",new Alert("alert-info"," Dr."+appUser.name+" is already associated with clinic "+clinic.name).toString());
			}
			else{
				final Doctor doctor = appUser.getDoctor();
				final List<ClinicInvite> pastInvites = ClinicInvite.getInvites(doctor, clinic);
				if(pastInvites.size() > 0){
					final ClinicInvite invite = pastInvites.get(0);
					invite.dateInvited = new Date();
					invite.invitedBy = LoginController.getLoggedInUser();
					invite.update();
					Promise.promise(new Function0<Integer>() {
						@Override
						public Integer apply() {
							final boolean result = EmailService.sendClinicInvitationConfirmationEmail(appUser, clinic, pastInvites.get(0).verificationCode);
							return 0;
						}
					});
					//flash().put("alert", new Alert("alert-info","An Invitation to "+email+" has already been sent on "+
					//		new SimpleDateFormat("dd-MMM-yyyy (hh:mm)").format(pastInvites.get(0).dateInvited)+".").toString());
					flash().put("alert",new Alert("alert-success","An Invitation has been sent to the Dr."+appUser.name).toString());
					return ClinicController.searchDoctorsByEmail();
				}
				final Random random = new SecureRandom();
				final byte[] array = new byte[32];
				random.nextBytes(array);
				final String verificationCode = Base64.encodeBase64String(array);

				final ClinicInvite invite = new ClinicInvite();
				invite.doctor = doctor;
				invite.clinic = clinic;
				invite.dateInvited = new Date();
				invite.invitedBy = LoginController.getLoggedInUser();
				invite.accepted = false;
				invite.verificationCode = verificationCode;
				invite.save();

				Promise.promise(new Function0<Integer>() {
					@Override
					public Integer apply() {
						final boolean result = EmailService.sendClinicInvitationConfirmationEmail(appUser, clinic, invite.verificationCode);
						return 0;
					}
				});
				// End of async
				flash().put("alert",new Alert("alert-success","An Invitation has been sent to the Dr."+appUser.name).toString());
			}
		}else{
			flash().put("alert",new Alert("alert-info","No doctor could be found with email id: "+email+"").toString());
		}
		return redirect(routes.ClinicController.searchDoctorsByEmail());

	}

	/**
	 * @author lakshmi
	 * Action to add doctor to the clinic
	 * GET		/secure-clinic/add-doctor/:docId
	 */
	@ConfirmAppUser
	public static Result addDoctorToTheClinic(final Long doctorId,final Long clinicId, final String verCode){
		final Clinic clinic = Clinic.find.byId(clinicId);
		final Doctor doctor = Doctor.find.byId(doctorId);
		final AppUser loggedInUser = LoginController.getLoggedInUser();

		//server-side check
		if(loggedInUser.getDoctor() == null || loggedInUser.getDoctor().id.longValue() != doctorId.longValue()){
			return redirect(routes.LoginController.processLogout());
		}

		final List<ClinicInvite> invites = ClinicInvite.getInvites(doctor, clinic);
		if(invites.size() == 0){
			flash().put("alert",new Alert("alert-danger","Unauthorized Operation.").toString());
			return Application.index();
		}
		if(invites.get(0).verificationCode.compareTo(verCode) != 0){
			flash().put("alert",new Alert("alert-danger","Unauthorized Operation.").toString());
			return Application.index();
		}

		if(invites.get(0).accepted){
			flash().put("alert",new Alert("alert-danger","Already Accpted Invitation on "+new SimpleDateFormat("").format(invites.get(0).dateAccepted)+".").toString());
			return Application.index();
		}

		final ClinicInvite invite = invites.get(0);
		invite.acceptedBy = loggedInUser;
		invite.accepted = true;
		invite.dateAccepted = new Date();
		invite.update();

		final DoctorClinicInfo doctorClinicInfo = new DoctorClinicInfo();
		doctorClinicInfo.clinic = clinic;
		doctorClinicInfo.doctor = doctor;
		doctorClinicInfo.save();
		Promise.promise(new Function0<Integer>() {
			@Override
			public Integer apply() {
				final boolean result1 = EmailService.sendClinicInvitationSuccessEmail(doctor,clinic);
				return 0;
			}
		});
		// End of async
		return redirect(routes.UserActions.dashboard());
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




