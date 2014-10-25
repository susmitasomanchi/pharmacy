package controllers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import models.Alert;
import models.AppUser;
import models.Role;
import models.bloodBank.BloodDonation;
import models.diagnostic.DiagnosticCentre;
import models.diagnostic.DiagnosticCentrePrescriptionInfo;
import models.diagnostic.DiagnosticCentrePrescritionStatus;
import models.doctor.Appointment;
import models.doctor.AppointmentStatus;
import models.doctor.Clinic;
import models.doctor.Doctor;
import models.doctor.DoctorClinicInfo;
import models.doctor.Prescription;
import models.doctor.QuestionAndAnswer;
import models.patient.Patient;
import models.patient.PatientClinicInfo;
import models.patient.PatientDoctorInfo;
import models.pharmacist.Pharmacy;
import models.pharmacist.PharmacyPrescriptionInfo;
import models.pharmacist.PharmacyPrescriptionStatus;
import play.Logger;
import play.data.Form;
import play.libs.F.Function0;
import play.libs.F.Promise;
import play.mvc.Controller;
import play.mvc.Result;
import utils.EmailService;
import utils.SMSService;
import actions.BasicAuth;
import actions.ConfirmAppUser;
import beans.QuestionAndAnswerBean;

@BasicAuth
public class PatientController extends Controller {

	public static Form<Patient> form = Form.form(Patient.class);

	public static Form<AppUser> registrationForm = Form.form(AppUser.class);

	public static Form<QuestionAndAnswerBean> questionAndAnswerForm = Form
			.form(QuestionAndAnswerBean.class);


	/**
	 * @author Mitesh
	 * Action to display currently logged in Patient' Doctor List
	 *  GET  /secure-user/my-doctors
	 */
	//@ConfirmAppUser
	public static Result myFavouriteDoctors() {
		final Patient patient=LoginController.getLoggedInUser().getPatient();
		return ok(views.html.patient.fav_doctors.render(patient.patientDoctorInfoList));
	}



	/**
	 * @author Mitesh
	 * Action to search Doctor and display it
	 *  GET /patient/delete-fav-doc/:id
	 */
	public static Result searchDoctors(final String search) {

		// final List<Patient> patients=Patient.find.where().eq("appUser.email",
		// "mitesh@greensoftware.in").findList();


		final List<Doctor> doctors = Doctor.find
				.where().like("searchIndex","%"+search+"%").findList();

		return ok(views.html.doctor.searchedDoctors.render(true, search,null,null, doctors));

	}


	public static Result displayQuestion() {
		return ok(views.html.patient.askQuestion.render(questionAndAnswerForm));
	}

	// Question Asked By A Patients
	public static final Result askQuestion() {
		try {
			final QuestionAndAnswerBean questionAndAnswerBean = questionAndAnswerForm
					.bindFromRequest().get();
			final QuestionAndAnswer questionAndAnswer = questionAndAnswerBean
					.toEntity();
			questionAndAnswer.questionBy = AppUser.find.byId(LoginController
					.getLoggedInUser().id);

			questionAndAnswer.questionDate = new Date();

			questionAndAnswer.save();

			return ok("0");
		} catch (final Exception e) {
			e.printStackTrace();
			return ok("-1");
		}
	}

	/*
	 * displaying all diagnostic centers
	 */
	public static Result diagnosticList() {
		final List<DiagnosticCentre> allList = DiagnosticCentre.find.all();

		return ok(views.html.diagnostic.patientDiagnosticCenterList.render(allList));

	}

	/*
	 * saving diagnostic center in patient favorite list
	 */
	@ConfirmAppUser
	public static Result saveDiagnosticCenter(final Long id) {
		final DiagnosticCentre dc = DiagnosticCentre.find.byId(id);
		final Patient patient = LoginController.getLoggedInUser().getPatient();

		patient.diagnosticCenterList.add(dc);
		patient.update();

		return ok("diagnostic center persisted in patient table");

	}

	/**
	 * displaying diagnostic centers which are there in patient favorite list
	 */
	@ConfirmAppUser
	public static Result myDiagnosticCenters() {
		final Long id = LoginController.getLoggedInUser().getPatient().id;
		final Patient diagnoCenterList = Patient.find.where().eq("id", id)
				.findUnique();
		final List<DiagnosticCentre> list = diagnoCenterList.diagnosticCenterList;

		return ok(views.html.patient.myDiagnoList.render(list));

	}

	/**
	 * removing diagnostic center from patient favorite diagnostic center list
	 */
	@ConfirmAppUser
	public static Result removePatientDiagnoCenter(final Long id) {

		final Patient patient = LoginController.getLoggedInUser().getPatient();
		final DiagnosticCentre centre = DiagnosticCentre.find.byId(id);
		patient.diagnosticCenterList.remove(centre);
		patient.update();

		return redirect(routes.PatientController.myDiagnosticCenters());

	}
	/**
	 * @author Mitesh
	 * Action to display currently logged in Patient'Doctor List
	 *  GET /patient/favdoctors
	 */
	@ConfirmAppUser
	public static Result patientMyFavDoctors() {
		final Patient patient=LoginController.getLoggedInUser().getPatient();
		return ok(views.html.patient.fav_doctors.render(patient.patientDoctorInfoList));
	}

	/**
	 * @author Mitesh
	 * Action to Delete one of the doctor from currently logged in Patient
	 *  GET /patient/delete-fav-doc/:id
	 */
	@ConfirmAppUser
	public static Result deleteMyFavDoctors(final Long patDocid) {

		final PatientDoctorInfo patientDoctorInfo=PatientDoctorInfo.find.byId(patDocid);
		patientDoctorInfo.delete();
		flash().put("alert", new Alert("alert-success","Successfully Deleted:"+patientDoctorInfo.doctor.appUser.name).toString());
		return redirect(routes.PatientController.patientMyFavDoctors());
	}

	public static Result staticPatientMyFavDoctors() {
		return ok(views.html.patient.static_fav_doctors.render());
	}

	public static Result staticPatientNewAppointment() {
		return ok(views.html.patient.static_patient_new_appointment.render());
	}


	public static Result staticPatientViewAppointments(){
		return ok(views.html.patient.static_patient_view_appointments.render());
	}

	/**
	 * @author lakshmi
	 * Action to add favorite pharmacy of the Doctor to the list of Patient of loggedin PATIENT
	 * GET/patient/add-favorite-pharmacy/:pharmacyId/:str
	 */
	@ConfirmAppUser
	public static Result addFavoritePharmacy(final Long pharmacyId,final String searchStr) {
		final Patient patient = LoginController.getLoggedInUser().getPatient();
		final Pharmacy pharmacy = Pharmacy.find.byId(pharmacyId);
		if(patient.pharmacyList.contains(pharmacy)!= true){
			patient.pharmacyList.add(pharmacy);
			patient.update();
		}
		else{
			flash().put("alert", new Alert("alert-info", pharmacy.name+" Already existed in the Favorite List.").toString());
		}
		final List<Pharmacy> pharmacyList = new ArrayList<Pharmacy>();
		for (final Pharmacy pharmacy2 : Pharmacy.find.where().like("searchIndex","%"+searchStr+"%").findList()) {
			if(patient.pharmacyList.contains(pharmacy2) != true){
				pharmacyList.add(pharmacy2);
			}
		}
		return ok(views.html.pharmacist.searched_pharmacies.render(true,searchStr,null,pharmacyList));
		//		return redirect(routes.UserActions.dashboard());
	}

	/**
	 * @author lakshmi
	 * Action to list out favorite pharmacies of Patient of loggedin PATIENT
	 * GET/patient/my-favorite-pharmacies
	 */
	@ConfirmAppUser
	public static Result patientFavoritePharmacies() {
		final Patient patient = LoginController.getLoggedInUser().getPatient();
		return ok(views.html.pharmacist.favorite_pharmacy_list.render(patient.pharmacyList));
	}


	/**
	 * @author lakshmi
	 * Action to list out favorite Diagnostic Centre of loggedIn Patient
	 * GET/patient/favorite-diagnostic-centres
	 */
	@ConfirmAppUser
	public static Result patientFavoriteDiagnosticCentres() {
		final Patient patient = LoginController.getLoggedInUser().getPatient();
		return ok(views.html.diagnostic.favorite_diagnosticCentre_list.render(patient.diagnosticCenterList));
	}



	/**
	 * @author Mitesh
	 * Action to show a forms which have currently logged in patient requested appointments
	 *  GET		/patient/my-appointments
	 */
	@ConfirmAppUser
	public static Result viewMyAppointments(){
		final AppUser patient=LoginController.getLoggedInUser();
		final List<Appointment> patientApppointments=Appointment.find.where().eq("requestedBy", patient).orderBy().desc("appointmentTime").findList();
		return ok(views.html.patient.patientViewAppointments.render(patientApppointments));
	}

	/**
	 * @author Mitesh
	 * Action to process requested appointments
	 * POST		/patient/process-appointment
	 */
	@ConfirmAppUser
	public static Result processAppointment(final Long apptId) {
		final String remark=request().body().asFormUrlEncoded().get("remark")[0];
		Logger.warn(remark);
		final Appointment appointment=Appointment.find.byId(apptId);
		appointment.appointmentStatus=AppointmentStatus.APPROVED;
		appointment.problemStatement=remark;
		appointment.requestedBy=LoginController.getLoggedInUser();
		appointment.bookedOn = new Date();
		appointment.update();



		// Async Execution
		Promise.promise(new Function0<Integer>() {
			//@Override
			@Override
			public Integer apply() {
				int result = 0;
				if(!EmailService.sendAppointmentConformMail(appointment.requestedBy, appointment.doctorClinicInfo.doctor.appUser, appointment)){
					result=1;
				}

				return result;
			}
		});
		// End of async

		final StringBuilder smsMessage = new StringBuilder();

		smsMessage.append("You have booked an appointment with Dr. "+appointment.doctorClinicInfo.doctor.appUser.name+" on ");
		smsMessage.append( new SimpleDateFormat("dd-MMM-yyyy").format(appointment.appointmentTime));
		smsMessage.append(" at "+ new SimpleDateFormat("HH:mm").format(appointment.appointmentTime));
		smsMessage.append(" at "+appointment.doctorClinicInfo.clinic.name+", "+appointment.doctorClinicInfo.clinic.address.area);
		SMSService.sendSMS(appointment.requestedBy.mobileNumber.toString(), smsMessage.toString());

		/*
		final StringBuilder smsMessage2 = new StringBuilder();
		smsMessage2.append("An Appointment has been booked on");
		smsMessage2.append( new SimpleDateFormat("dd-MMM-yyyy").format(appointment.appointmentTime));
		smsMessage2.append(","+ new SimpleDateFormat("HH:mm").format(appointment.appointmentTime));
		smsMessage2.append("at "+appointment.doctorClinicInfo.clinic.name+","+appointment.doctorClinicInfo.clinic.address.area);
		smsMessage2.append("by patient "+appointment.requestedBy.name+".");
		SMSService.sendSMS(appointment.doctorClinicInfo.doctor.appUser.mobileNumber.toString(), smsMessage2.toString());
		 */

		return redirect(routes.PatientController. viewMyAppointments());
	}

	/**@author lakshmi
	 * Action to show all prescription Of loggedInPatient
	 *GET /secure-user/prescriptions
	 */
	@ConfirmAppUser
	public static Result viewAllPatientPrescriptions() {
		final Patient patient = LoginController.getLoggedInUser().getPatient();
		final List<Prescription> prescriptionList = Prescription.find.where()
				.eq("patient", patient).orderBy().desc("prescriptionDate").findList();
		return ok(views.html.patient.patientPrescriptionList.render(prescriptionList));
	}
	/**
	 * @author lakshmi
	 * Action to show the prescription content to the loggedInPatient
	 * GET/secure-user/show-prescription/:prescriptionId
	 */
	@ConfirmAppUser
	public static Result viewPrescription(final Long prescriptionId) {
		final Patient patient = LoginController.getLoggedInUser().getPatient();
		final Prescription prescription = Prescription.find.byId(prescriptionId);
		// server-side check
		if (prescription.patient.id.longValue() != patient.id.longValue()) {
			return redirect(routes.LoginController.processLogout());
		}
		return ok(views.html.patient.patientSharedPrescription.render(prescription));
	}

	/**
	 * @author Mitesh Action to Display appointment requested to logged-in
	 *         DOCTOR GET /secure-doctor/all-appointments
	 */
	@ConfirmAppUser
	public static Result viewPatientAppointments() {

		final Patient patient = LoginController.getLoggedInUser().getPatient();
		/*final List<DoctorClinicInfo> docclinicInfo = DoctorClinicInfo.find.where().eq("patient", patient).findList();
		final List<AppointmentStatus> statusList = new ArrayList<AppointmentStatus>();
		final List<Appointment> appointments = Appointment.find.where()
				.eq("requestedBy", patient.appUser.id)
				.findList();*/
		return ok(views.html.patient.patientAllAppointments.render(patient));

	}





	/**
	 * Action to show logged In patient a page to assign a prescription to a pharmacy / diagnostic centre
	 * GET /secure-user/share-prescription
	 */
	@ConfirmAppUser
	public static Result sharePrescription(final Long prId,final String pharmacyId, final String diagnosticId) {
		final Patient patient = LoginController.getLoggedInUser().getPatient();
		final Prescription prescription = Prescription.find.byId(prId);
		// server-side check
		if (prescription.patient.id.longValue() != patient.id.longValue()) {
			return redirect(routes.LoginController.processLogout());
		}
		final StringBuilder sharedWith = new StringBuilder();

		if(pharmacyId != null && !pharmacyId.trim().isEmpty()){
			final Pharmacy pharmacy = Pharmacy.find.byId(Long.parseLong(pharmacyId));
			final PharmacyPrescriptionInfo ppInfo = PharmacyPrescriptionInfo.find
					.where().eq("pharmacy", pharmacy)
					.eq("prescription", prescription).findUnique();
			if (ppInfo == null) {
				final PharmacyPrescriptionInfo phprInfo = new PharmacyPrescriptionInfo();
				phprInfo.pharmacy = pharmacy;
				phprInfo.prescription = prescription;
				phprInfo.sharedBy = patient.appUser;
				phprInfo.sharedDate = new Date();
				phprInfo.pharmacyPrescriptionStatus = PharmacyPrescriptionStatus.RECEIVED;
				phprInfo.save();
				// send sms and email to pharmacy
				final StringBuilder message = new StringBuilder();
				message.append("<html><body>");
				message.append("<p>Dear "+pharmacy.adminPharmacist.appUser.name+",<br><br>Prescription of Dr."+prescription.doctor.appUser.name+"for patient"+prescription.patient.appUser.name+" has been shared with you.");
				message.append("<br><br>Best regards,<br>MedNetwork.in</p>");
				message.append("</body></html>");
				// Async Execution
				Promise.promise(new Function0<Integer>() {
					//@Override
					@Override
					public Integer apply() {
						int result = 0;
						if(!EmailService.sendSimpleHtmlEMail(pharmacy.adminPharmacist.appUser.email, "Prescription Shared", message.toString())){
							result=1;
						}

						return result;
					}
				});
				// End of async
				sharedWith.append(phprInfo.pharmacy.name);
			}
			else{
				flash().put("alert",new Alert("alert-info", "Your Prescription Already shared with "+ ppInfo.pharmacy.name).toString());
				return redirect(routes.DoctorController.viewTodaysAppointments());
			}
		}

		if(diagnosticId != null && !diagnosticId.trim().isEmpty()){
			final DiagnosticCentre diagnosticCentre = DiagnosticCentre.find.byId(Long.parseLong(diagnosticId));
			final DiagnosticCentrePrescriptionInfo dcpInfo = DiagnosticCentrePrescriptionInfo.find
					.where().eq("diagnosticCentre", diagnosticCentre)
					.eq("prescription", prescription).findUnique();
			if (dcpInfo == null) {
				final DiagnosticCentrePrescriptionInfo diagPrescriptionInfo = new DiagnosticCentrePrescriptionInfo();
				diagPrescriptionInfo.diagnosticCentre = diagnosticCentre;
				diagPrescriptionInfo.prescription = prescription;
				diagPrescriptionInfo.sharedBy = patient.appUser;
				diagPrescriptionInfo.sharedDate = new Date();
				diagPrescriptionInfo.diagnosticCentrePrescritionStatus = DiagnosticCentrePrescritionStatus.RECEIVED;
				diagPrescriptionInfo.save();
				// send sms and email to dc
				final StringBuilder message = new StringBuilder();
				message.append("<html><body>");
				message.append("<p>Dear "+diagnosticCentre.diagnosticRepAdmin.appUser.name+",<br><br>Prescription of Dr."+prescription.doctor.appUser.name+"for patient"+prescription.patient.appUser.name+" has been shared with you.");
				message.append("<br><br>Best regards,<br>MedNetwork.in</p>");
				message.append("</body></html>");
				// Async Execution
				Promise.promise(new Function0<Integer>() {
					//@Override
					@Override
					public Integer apply() {
						int result = 0;
						if(!EmailService.sendSimpleHtmlEMail(diagnosticCentre.diagnosticRepAdmin.appUser.email, "Prescription Shared", message.toString())){
							result=1;
						}

						return result;
					}
				});
				// End of async
				if(sharedWith.length() > 0){
					sharedWith.append(" and ");
				}
				sharedWith.append(diagnosticCentre.name);
			}
			else{
				flash().put("alert",new Alert("alert-info", "Your Prescription Already shared with "+ dcpInfo.diagnosticCentre.name).toString());
				return redirect(routes.DoctorController.viewTodaysAppointments());
			}
		}


		if(!(
				(pharmacyId == null || pharmacyId.trim().isEmpty())
				&&
				(diagnosticId == null || diagnosticId.trim().isEmpty())
				)){

			//send mail to patient and doctor
			//send sms to patient
			final StringBuilder message = new StringBuilder();
			message.append("<html><body>");
			message.append("<p>Dear "+prescription.patient.appUser.email+",<br><br>Your prescription of Dr."+prescription.doctor.appUser.name+" has been shared with"+sharedWith.toString()+".");
			message.append("<br><br>Best regards,<br>MedNetwork.in</p>");
			message.append("</body></html>");
			// Async Execution
			Promise.promise(new Function0<Integer>() {
				//@Override
				@Override
				public Integer apply() {
					int result = 0;
					if(!EmailService.sendSimpleHtmlEMail(prescription.patient.appUser.email, "Prescription Shared", message.toString())){
						result=1;
					}

					return result;
				}
			});
			// End of async

			final StringBuilder smsToPatient = new StringBuilder();
			smsToPatient.append("Your prescription of Dr."+prescription.doctor.appUser.name+" has been shared with"+sharedWith.toString()+".");
			SMSService.sendSMS(prescription.patient.appUser.mobileNumber.toString(), smsToPatient.toString());

			flash().put("alert",new Alert("alert-success", "Prescription shared with "+sharedWith.toString()).toString());
		}

		flash().put("alert",new Alert("alert-success", "Prescription shared with "+sharedWith.toString()).toString());

		return redirect(routes.PatientController.viewAllPatientPrescriptions());
	}
	/**
	 * @author lakshmi
	 * Action to display diagnostic reports
	 *//*
	@ConfirmAppUser
	public static Result viewDiagnosticReports(){
		final Patient patient = LoginController.getLoggedInUser().getPatient();
		DiagnosticCentrePrescriptionInfo info = DiagnosticCentrePrescriptionInfo.find.byId(diagnosticInfoId);
		final List<DiagnosticCentrePrescriptionInfo> diagnosticCentrePrescriptionInfos = new ArrayList<DiagnosticCentrePrescriptionInfo>();
		return ok(views.html.patient.patientDiagnosticReports.render(diagnosticCentrePrescriptionInfos));

	}
	  */
	/**
	 * @author lakshmi
	 * Action to display diagnostic reports
	 */
	@ConfirmAppUser
	public static Result viewDiagnosticReports(){
		final Patient patient = LoginController.getLoggedInUser().getPatient();
		final List<Prescription> prescriptions = Prescription.find.where().eq("patient", patient).findList();
		/*final DiagnosticCentrePrescriptionInfo info = DiagnosticCentrePrescriptionInfo.find.byId(diagnosticInfoId);
		final List<DiagnosticCentrePrescriptionInfo> diagnosticCentrePrescriptionInfos = new ArrayList<DiagnosticCentrePrescriptionInfo>();*/
		return ok(views.html.patient.patientDiagnosticReports.render(prescriptions));

	}

	/**
	 * @author lakshmi
	 * Action to list out Patient BloodDonation
	 * GET/secure-user/blood-donation-info
	 */
	public static Result getPatientBloodDonationInfo(){
		final Patient patient = LoginController.getLoggedInUser().getPatient();
		List<BloodDonation> bloodDonations = new ArrayList<BloodDonation>();
		if((patient.appUser.isBloodDonor == true) && (patient.appUser.bloodDonationList.size() > 0) ){
			bloodDonations = patient.appUser.bloodDonationList;
		}
		return ok(views.html.patient.patientBloodDonationList.render(bloodDonations));
	}
	/**
	 * @author lakshmi
	 * Action to add Clinic to loggedInUser
	 * GET/secure-user/add-fav-clinic/:clinicIds
	 */
	public static Result addClinicToLoggedInUser(final Long clinicId) {
		if(!LoginController.isLoggedIn()){
			flash().put("alert", new Alert("alert-info","Please Login To Add Clinic.").toString());
			return redirect(routes.Application.index());
		}
		else{
			final Patient patient = LoginController.getLoggedInUser().getPatient();
			final Clinic clinic = Clinic.find.byId(clinicId);
			final int count = PatientClinicInfo.find.where()
					.eq("patient", patient)
					.eq("clinic", clinic)
					.findRowCount();
			if(count > 0){
				flash().put("alert", new Alert("alert-info",clinic.name+" is already in your Clinic list.").toString());
			}
			else{
				final PatientClinicInfo patientClinicInfo = new PatientClinicInfo();
				patientClinicInfo.patient = patient;
				patientClinicInfo.clinic = clinic;
				patientClinicInfo.save();
				flash().put("alert", new Alert("alert-success",clinic.name+" added to your Clinic list.").toString());
			}
		}

		return redirect(routes.PublicController.searchClinics());
	}
	/**
	 * @author lakshmi
	 * Action to add Clinic to loggedInUser
	 * GET /secure-user/get-favorite-clinics
	 */
	public static Result getFavoriteClinics() {
		final Patient patient = LoginController.getLoggedInUser().getPatient();
		final List<PatientClinicInfo> patientClinicInfos = PatientClinicInfo.find.where()
				.eq("patient", patient)
				.findList();
		return ok(views.html.patient.patientFavoriteClinics.render(patientClinicInfos));
	}

	/**
	 * @author lakshmi
	 * Action to remove Clinic to loggedInUser
	 * GET /secure-user/remove-favorite-clinic/:clinicId
	 */
	public static Result removeFavoriteClinic(final Long clinicId) {
		final Patient patient = LoginController.getLoggedInUser().getPatient();
		final Clinic clinic = Clinic.find.byId(clinicId);
		final PatientClinicInfo patientClinicInfo = PatientClinicInfo.find.where()
				.eq("patient", patient)
				.eq("clinic", clinic)
				.findUnique();
		patientClinicInfo.delete();
		flash().put("alert", new Alert("alert-danger",clinic.name+" Removed From your Clinic list.").toString());
		return redirect(routes.PatientController.getFavoriteClinics());
	}

	/**
	 * @author lakshmi
	 * Action to show a forms which have Doctor and it will show the available and booked appointment
	 *  GET	/secure-patient/schedule-appointment/:docclinicid
	 */
	@ConfirmAppUser
	public static Result scheduleAppointment(final Long docclinicid) {
		final DoctorClinicInfo clinicInfo=DoctorClinicInfo.find.byId(docclinicid);
		return ok(views.html.patient.patientClinicNewAppointment.render(clinicInfo));
	}
}
