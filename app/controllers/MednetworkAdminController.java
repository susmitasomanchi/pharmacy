package controllers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.Alert;
import models.AppUser;
import models.Feedback;
import models.PrimaryCity;
import models.Role;
import models.State;
import models.diagnostic.DiagnosticCentre;
import models.diagnostic.DiagnosticCentrePrescriptionInfo;
import models.diagnostic.DiagnosticRepresentative;
import models.doctor.Appointment;
import models.doctor.Clinic;
import models.doctor.Doctor;
import models.doctor.DoctorClinicInfo;
import models.doctor.MasterSpecialization;
import models.doctor.Prescription;
import models.patient.Patient;
import models.patient.PatientDoctorInfo;
import models.pharmacist.Pharmacist;
import models.pharmacist.Pharmacy;
import models.pharmacist.PharmacyPrescriptionInfo;

import org.json.JSONObject;

import play.Logger;
import play.mvc.Controller;
import play.mvc.Result;
import actions.BasicAuth;
import actions.MedNetworkAdmin;

import com.avaje.ebean.Ebean;

@BasicAuth
@MedNetworkAdmin
public class MednetworkAdminController extends Controller {

	/**
	 * @author Lakshmi
	 * GET		/secure-admin/specialization-list
	 * @return
	 */
	public static Result getSpecializationList(){
		return ok(views.html.mednetAdmin.specializationList.render(MasterSpecialization.getAll()));
	}

	/**
	 * @author Lakshmi
	 * POST		/secure-admin/add-specialization
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
	 * POST		/secure-admin/update-specialization
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
	 * GET		/secure-admin/specialization-list
	 * @return
	 */
	public static Result getFeedbackList(){
		final List<Feedback> feedbacks = Feedback.find.orderBy().desc("date").findList();
		return ok(views.html.mednetAdmin.feedbackList.render(feedbacks));
	}


	/**
	 * @author Lakshmi
	 * GET		/secure-admin/delete-feedback/:id
	 */
	public static Result deleteFeedback(final Long id){
		Feedback.find.byId(id).delete();
		flash().put("alert", new Alert("alert-info"," Feedback deleted.").toString());
		return redirect(routes.MednetworkAdminController.getFeedbackList());
	}


	/**
	 * GET	/secure-admin/get-user-date-between-dates/:from/:to
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
	/**
	 * @author lakshmi
	 * Action to render purgeDoctorDetails scala template
	 * GET    /secure-admin/purge-doctor-find
	 */
	public static Result purgeDoctor(){
		return ok(views.html.mednetAdmin.purgeDoctorDetails.render(null,false));
	}
	/**
	 * @author lakshmi
	 * Action to get the Doctor Information
	 * POST    /secure-admin/purge-doctor-details
	 */
	public static Result getAppUserDetails(final String role){
		Logger.info("test1");
		if(request().body().asFormUrlEncoded().get("appUserId")[0] != null && !(request().body().asFormUrlEncoded().get("appUserId")[0].trim().isEmpty())){
			if(AppUser.find.byId(Long.parseLong(request().body().asFormUrlEncoded().get("appUserId")[0])) != null){
				final AppUser appUser = AppUser.find.byId(Long.parseLong(request().body().asFormUrlEncoded().get("appUserId")[0]));
				if(role.equalsIgnoreCase("doctor")) {
					Logger.info("test2");
					return ok(views.html.mednetAdmin.purgeDoctorDetails.render(appUser,true));
				}
				if(role.equalsIgnoreCase("Pharmacy")) {
					return ok(views.html.mednetAdmin.purgePharmacyDetails.render(appUser,true));
				}
				if(role.equalsIgnoreCase("Diagnostic")) {
					return ok(views.html.mednetAdmin.purgeDiagnosticCentreDetails.render(appUser,true));
				}
			}
			else{
				flash().put("alert", new Alert("alert-info","With The Id "+request().body().asFormUrlEncoded().get("appUserId")[0]+" No AppUser Is Available.").toString());
			}
		}
		return redirect(routes.MednetworkAdminController.purgeDoctor());

	}

	/**
	 * @author lakshmi
	 * Action to purge the Doctor Information
	 * GET     /secure-admin/purge-doctor/:id
	 */
	public static Result processPurgeDoctor(final Long id){
		if(!(LoginController.getLoggedInUser().role.equals(Role.MEDNETWORK_ADMIN))){
			flash().put("alert", new Alert("alert-info","Not Authorised.").toString());
			return redirect(routes.Application.index());
		}

		final AppUser appUser = AppUser.find.byId(id);
		final Doctor doctor = Doctor.find.where().eq("appUser", appUser).findUnique();
		Logger.info(""+doctor.id);
		final List<DoctorClinicInfo> doctorClinicInfos = DoctorClinicInfo.find.where().eq("doctor", doctor).findList();
		final List<Appointment> appointments= Appointment.find.where().eq("doctorClinicInfo.doctor", doctor).findList();;
		final List<Prescription> prescriptions = Prescription.find.where().eq("doctor", doctor).findList();
		final List<Clinic> clinics = new ArrayList<Clinic>();
		final List<PharmacyPrescriptionInfo> pharmacyPrescriptionInfos = PharmacyPrescriptionInfo.find.where().eq("prescription.doctor", doctor).findList();
		final List<DiagnosticCentrePrescriptionInfo> diagnosticCentrePrescriptionInfos = DiagnosticCentrePrescriptionInfo.find.where().eq("prescription.doctor", doctor).findList();
		final List<PatientDoctorInfo> patientDoctorInfos = PatientDoctorInfo.find.where().eq("doctor", doctor).findList();
		for (final DoctorClinicInfo doctorClinicInfo : doctorClinicInfos) {
			clinics.add(Clinic.find.where().eq("id", doctorClinicInfo.clinic.id).findUnique());
		}
		Ebean.delete(pharmacyPrescriptionInfos);
		Ebean.delete(diagnosticCentrePrescriptionInfos);
		Ebean.delete(prescriptions);
		Ebean.delete(appointments);
		Ebean.delete(doctorClinicInfos);
		Ebean.delete(clinics);
		Ebean.delete(patientDoctorInfos);
		doctor.delete();
		appUser.delete();
		flash().put("alert", new Alert("alert-info",""
				+ "Doctor Puged Successfully.").toString());
		return redirect(routes.UserActions.dashboard());
	}
	/**
	 * @author lakshmi
	 * Action to render purgePharmacyDetails scala template
	 * GET    /secure-admin/purge-pharmacy-find
	 */
	public static Result purgePharmacy(){
		return ok(views.html.mednetAdmin.purgePharmacyDetails.render(null,false));
	}

	/**
	 * @author lakshmi
	 * Action to purge the Pharmacy details
	 * 
	 */
	public static Result processPurgePharmacy(final Long id){
		if(!(LoginController.getLoggedInUser().role.equals(Role.MEDNETWORK_ADMIN))){
			flash().put("alert", new Alert("alert-info","Not Authorised.").toString());
			return redirect(routes.Application.index());
		}

		final AppUser appUser = AppUser.find.byId(id);
		final Pharmacist pharmacist = Pharmacist.find.where().eq("appUser", appUser).findUnique();
		final Pharmacy pharmacy = Pharmacy.find.where().eq("adminPharmacist.appUser", appUser).findUnique();
		final List<PharmacyPrescriptionInfo> pharmacyPrescriptionInfos = PharmacyPrescriptionInfo.find.where().eq("pharmacy", pharmacy).findList();
		Ebean.delete(pharmacyPrescriptionInfos);
		pharmacist.pharmacy = null;
		pharmacist.update();
		pharmacy.adminPharmacist = null;
		pharmacy.update();
		//pharmacy.adminPharmacist.delete();
		pharmacist.delete();
		final List<Patient> patients = Patient.find.all();
		for (final Patient patient : patients) {
			if(patient.pharmacyList.contains(pharmacy)){
				patient.pharmacyList.remove(pharmacy);
				patient.update();
			}
		}
		final List<Doctor> doctors = Doctor.find.all();
		for (final Doctor doctor : doctors) {
			if(doctor.pharmacyList.contains(pharmacy)){
				doctor.pharmacyList.remove(pharmacy);
				doctor.update();
			}
		}
		pharmacy.delete();
		appUser.delete();
		flash().put("alert", new Alert("alert-info","Pharmacy Puged Successfully.").toString());
		return redirect(routes.MednetworkAdminController.purgePharmacy());
	}
	/**
	 * @author lakshmi
	 * Action to render purgeDoctorDetails scala template
	 * GET    /secure-admin/purge-diagnostic-find
	 */
	public static Result purgeDiagnosticCentre(){
		return ok(views.html.mednetAdmin.purgeDiagnosticCentreDetails.render(null,false));
	}
	/*public static Result getDiagnosticCentreDetails(){
		if(request().body().asFormUrlEncoded().get("appUserId")[0] != null && !(request().body().asFormUrlEncoded().get("appUserId")[0].isEmpty())){
			if(AppUser.find.byId(Long.parseLong(request().body().asFormUrlEncoded().get("appUserId")[0])) != null){
				final AppUser appUser = AppUser.find.byId(Long.parseLong(request().body().asFormUrlEncoded().get("appUserId")[0]));
				return ok(views.html.mednetAdmin.purgeDiagnosticCentreDetails.render(appUser,true));
			}
			else{
				flash().put("alert", new Alert("alert-info","With this Id No Pharmacy Is Available.").toString());
			}
		}
		return redirect(routes.MednetworkAdminController.purgeDiagnosticCentre());

	}*/
	/**
	 * @author lakshmi
	 * Action to purge the Pharmacy details
	 * 
	 */
	public static Result processPurgeDiagnosticCentre(final Long id){
		if(!(LoginController.getLoggedInUser().role.equals(Role.MEDNETWORK_ADMIN))){
			flash().put("alert", new Alert("alert-info","Not Authorised.").toString());
			return redirect(routes.Application.index());
		}

		final AppUser appUser = AppUser.find.byId(id);
		final DiagnosticRepresentative diagnosticRepresentative = DiagnosticRepresentative.find.where().eq("appUser", appUser).findUnique();
		final DiagnosticCentre diagnosticCentre = DiagnosticCentre.find.where().eq("diagnosticRepAdmin.appUser", appUser).findUnique();
		final List<DiagnosticCentrePrescriptionInfo> diagnosticCentrePrescriptionInfos = DiagnosticCentrePrescriptionInfo.find.where().eq("diagnosticCentre", diagnosticCentre).findList();
		Ebean.delete(diagnosticCentrePrescriptionInfos);
		diagnosticRepresentative.diagnosticCentre = null;
		diagnosticRepresentative.update();
		diagnosticCentre.diagnosticRepAdmin = null;
		diagnosticCentre.update();
		//pharmacy.adminPharmacist.delete();
		diagnosticRepresentative.delete();
		final List<Patient> patients = Patient.find.all();
		for (final Patient patient : patients) {
			if(patient.diagnosticCenterList.contains(diagnosticCentre)){
				patient.diagnosticCenterList.remove(diagnosticCentre);
				patient.update();
			}
		}
		final List<Doctor> doctors = Doctor.find.all();
		for (final Doctor doctor : doctors) {
			if(doctor.diagnosticCentreList.contains(diagnosticCentre)){
				doctor.diagnosticCentreList.remove(diagnosticCentre);
				doctor.update();
			}
		}
		diagnosticCentre.delete();
		appUser.delete();
		flash().put("alert", new Alert("alert-info","Diagnostic Centre Puged Successfully.").toString());
		return redirect(routes.MednetworkAdminController.purgeDiagnosticCentre());
	}


	/**
	 * Action to render list of Primary Cities and a form to add one
	 * GET	/primary-cities
	 */
	public static Result getPrimaryCitiesList(){
		return ok(views.html.mednetAdmin.primaryCitiesList.render());
	}

	/**
	 * Action create a Primary City
	 * POST	/add-primary-city
	 */
	public static Result addPrimaryCity(){
		if(
				request().body().asFormUrlEncoded().get("state")!= null &&
				request().body().asFormUrlEncoded().get("city")!= null &&
				!request().body().asFormUrlEncoded().get("state")[0].trim().isEmpty() &&
				!request().body().asFormUrlEncoded().get("city")[0].trim().isEmpty()

				){

			final State state = State.valueOf(request().body().asFormUrlEncoded().get("state")[0].trim());
			final String city = request().body().asFormUrlEncoded().get("city")[0].trim();
			final PrimaryCity primaryCity = new PrimaryCity();
			primaryCity.name = city;
			primaryCity.state = state;
			primaryCity.save();
			flash().put("alert", new Alert("alert-info","Primary City Added").toString());
		}
		return redirect(routes.MednetworkAdminController.getPrimaryCitiesList());
	}

}
