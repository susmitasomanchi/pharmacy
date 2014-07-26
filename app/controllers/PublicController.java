package controllers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import models.Alert;
import models.Role;
import models.doctor.Appointment;
import models.doctor.Day;
import models.doctor.DaySchedule;
import models.doctor.Doctor;
import models.doctor.DoctorClinicInfo;
import models.patient.Patient;
import models.patient.PatientDoctorInfo;
import models.pharmacist.Pharmacy;
import play.Logger;
import play.mvc.Controller;
import play.mvc.Result;

public class PublicController extends Controller{




	/**
	 * Action to render search doctor page
	 * GET /doctor/search
	 */
	public static Result searchDoctorsPage(){
		return ok(views.html.doctor.searchedDoctors.render(false,"", new ArrayList<Doctor>()));
	}

	/**
	 * @author Mitesh
	 * Action to search Doctor and display it
	 * GET /doctor/search/:key
	 */
	public static Result processSearchDoctors(final String key) {
		final String cleanKey = key.trim().toLowerCase();
		if(cleanKey.length() < 4){
			flash().put("alert", new Alert("alert-info", "Searched word should be atleast 4 characters long.").toString());
			return ok(views.html.doctor.searchedDoctors.render(false,key, new ArrayList<Doctor>()));
		}
		else{
			final List<Doctor> doctors = Doctor.find.where().like("searchIndex","%"+cleanKey+"%").findList();
			return ok(views.html.doctor.searchedDoctors.render(true, key, doctors));
		}
	}

	/**
	 * @author Mitesh
	 * Action to add doctor to Users's page
	 * GET /doctor/add-to-Favorite/:docId
	 */
	public static Result addToFavDoctor(final Long docId) {
		if(!LoginController.isLoggedIn()){
			flash().put("alert", new Alert("alert-info","Please Login To Add Your Favorite Doctor").toString());
			return redirect(routes.LoginController.blogAdminLoginForm());
		}else{
			final String loggedInRole=LoginController.getLoggedInUserRole();
			if(loggedInRole.equalsIgnoreCase(Role.PATIENT.toString())){
				final Patient patient=LoginController.getLoggedInUser().getPatient();
				final PatientDoctorInfo patientInfo=new PatientDoctorInfo();
				patientInfo.patient=patient;
				patientInfo.doctor=Doctor.find.byId(docId);
				patient.patientDoctorInfos.add(patientInfo);
				flash().put("alert", new Alert("alert-success","Added to Your Favorite Doctor").toString());

				return redirect(routes.UserActions.dashboard());

			}
			else if(loggedInRole.equalsIgnoreCase(Role.ADMIN_PHARMACIST.toString())){

			}
			else if(loggedInRole.equalsIgnoreCase(Role.ADMIN_DIAGREP.toString())){

			}
		}
		return ok();
	}


	/**
	 * @author Mitesh
	 * Action to search Doctor and display his profile page
	 * GET /doctor/:slugUrl
	 */
	public static Result getDoctorWithSlug(final String slug) {
		final String cleanSlug = slug.trim().toLowerCase();
		final Doctor doctor = Doctor.find.where().eq("slugUrl",cleanSlug).findUnique();
		return ok(views.html.doctor.publicDoctorProfile.render(doctor));
	}


	/**
	 * @author lakshmi
	 * Action to render the searchedPharmacies scala template
	 * GET/pharmacy/search
	 */
	public static Result searchPhamacy(){
		return ok(views.html.pharmacist.searched_pharmacies.render(false,"", new ArrayList<Pharmacy>()));
	}

	/**
	 * @author lakshmi
	 * Action to perform search operation for finding pharmacies based on
	 * name and area
	 * GET/pharmacy/search/:searchString
	 */
	public static Result searchPharmacies(final String searchString) {
		final String searchStr = searchString.toLowerCase().trim();
		List<Pharmacy> pharmacyList = new ArrayList<Pharmacy>();
		if(searchStr.length()>=4){
			pharmacyList = Pharmacy.find.where().like("searchIndex","%"+searchStr+"%").findList();
		}
		else{
			flash().put("alert", new Alert("alert-danger", "The searck key should contain atleast four charecters").toString());
		}
		return ok(views.html.pharmacist.searched_pharmacies.render(true,searchStr,pharmacyList));
	}
	/**
	 *@author lakshmi
	 * Action to add pharmacy to the Logged in user list
	 * 
	 */
	public static Result myFavoritePharmacy(final Long pharmacyId,final String searchKey){
		if(LoginController.getLoggedInUserRole().equals("DOCTOR")){
			return redirect(routes.DoctorController.addFavoritePharmacy(pharmacyId,searchKey));
		}
		else if(LoginController.getLoggedInUserRole().equals("PATIENT")){
			return redirect(routes.PatientController.addFavoritePharmacy(pharmacyId,searchKey));
		}else{

			return redirect(routes.UserController.processJoinUs());
		}
	}

	/**
	 * @author Mitesh
	 * Action to show a forms which have Doctor and it will show the available and booked appointment
	 *  GET /doctor/schedule-appointment/:docclinicid
	 */
	public static Result scheduleAppointment(final Long docclinicid) {
		final DoctorClinicInfo clinicInfo=DoctorClinicInfo.find.byId(docclinicid);
		return ok(views.html.patient.patientNewAppointment.render(clinicInfo));
	}

	/**
	 * @author Mitesh
	 * Action to display a form which has lists of appointment as per date is provided
	 *  GET/patient/display-appointment/:docClinicId/:days/:timeMillis
	 */
	public static Result displayAppointment(final Long docClinId,int days,Long timeMillis) {

		int slots=0;

		final Map<Date, List<Appointment>> appointmentMap = new LinkedHashMap<Date, List<Appointment>>();

		final DoctorClinicInfo doctorClinicInfo = DoctorClinicInfo.find.byId(docClinId);

		Logger.debug(doctorClinicInfo.scheduleDays.size()+"");
		final Calendar calendarFrom = Calendar.getInstance();
		calendarFrom.setTime(new Date(timeMillis));

		final Calendar calendarTo = Calendar.getInstance();
		calendarTo.setTime(new Date(timeMillis));

		final Calendar calendar1=Calendar.getInstance();
		final Calendar calendar2=Calendar.getInstance();

		final SimpleDateFormat dateFormat=new SimpleDateFormat("kk:mm");
		int i=0;
		while(true){

			if(i==7){
				break;
			}

			Logger.warn("i:"+i);
			for (final DaySchedule schedule : doctorClinicInfo.scheduleDays) {
				Logger.error("schedule"+schedule.day);
				Logger.error("calender"+Day.getDay(calendarFrom.get(Calendar.DAY_OF_WEEK)-1));
				if(schedule.day == Day.getDay(calendarFrom.get(Calendar.DAY_OF_WEEK)-1)){
					Logger.debug("schedule"+schedule.day);
					try{
						calendar1.setTime(dateFormat.parse(schedule.fromTime));

						calendar2.setTime(dateFormat.parse(schedule.toTime));

					}
					catch(final ParseException exception){
						exception.printStackTrace();
					}


					calendarFrom.set(Calendar.HOUR_OF_DAY, calendar1.get(Calendar.HOUR_OF_DAY));
					calendarFrom.set(Calendar.MINUTE,calendar1.get(Calendar.MINUTE));
					calendarFrom.set(Calendar.SECOND, 0);
					calendarFrom.set(Calendar.MILLISECOND, 0);

					calendarTo.set(Calendar.HOUR_OF_DAY, calendar2.get(Calendar.HOUR_OF_DAY));
					calendarTo.set(Calendar.MINUTE,calendar2.get(Calendar.MINUTE));
					calendarTo.set(Calendar.SECOND, 0);
					calendarTo.set(Calendar.MILLISECOND, 0);


					final List<Appointment> listAppointments = Appointment.getAvailableAppointmentList(doctorClinicInfo.id,calendarFrom.getTime(),calendarTo.getTime());
					if(listAppointments.size() != 0 ){
						Logger.debug("fetched",calendarFrom);
						appointmentMap.put(calendarFrom.getTime(), listAppointments);
						slots=Math.max(slots,listAppointments.size());
						i++;

					}

					Logger.warn("if end");
				}

				Logger.warn("schedule  end");
			}
			Logger.warn("i  end");

			calendarFrom.add(Calendar.DATE, 1);
			calendarTo.add(Calendar.DATE, 1);

		}


		/*return ok(views.html.patient.scheduleAppointment.render(appointmentMap,
				 slots));*/
		Logger.warn(""+appointmentMap.size());

		return ok(views.html.patient.appointmentForm.render(appointmentMap,slots));
	}

}
