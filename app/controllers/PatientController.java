package controllers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import models.AppUser;
import models.Patient;
import play.Logger;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import utils.Constants;
import actions.BasicAuth;

@BasicAuth
public class PatientController extends Controller {


	public static Result scheduleAppointment(){

		final int startingHrs = 15;
		final int endingHrs = 20;

		final int availableHrs = endingHrs - startingHrs;
		final int slots =  (availableHrs)*(60/Constants.DOCTOR_APPOINTMENT_INTERVAL);
		final Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, startingHrs);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);

		final Map<Date, List<String>> dateMap = new LinkedHashMap<Date, List<String>>();

		final Calendar calDate = Calendar.getInstance();
		calDate.set(Calendar.HOUR_OF_DAY, 0);
		calDate.set(Calendar.MINUTE, 0);
		calDate.set(Calendar.SECOND, 0);
		calDate.set(Calendar.MILLISECOND, 0);

		for(int i=0; i<Constants.DOCTOR_APPOINTMENT_DEFAULT_DAYS; i++){
			final List<String> timeList = new ArrayList<String>();
			final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
			for(int s=0; s<slots; s++){
				timeList.add(sdf.format(cal.getTime()));
				cal.add(Calendar.MINUTE, Constants.DOCTOR_APPOINTMENT_INTERVAL);
			}
			cal.set(Calendar.HOUR_OF_DAY, startingHrs);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			dateMap.put(calDate.getTime(), timeList);
			calDate.add(Calendar.DATE, 1);
		}

		calDate.add(Calendar.DATE, -1);

		return ok(views.html.patient.scheduleAppointment.render(dateMap, slots));
	}




	public static Result processAppointment(){
		return ok();
	}














	public static Form<Patient> form = Form.form(Patient.class);

	public static Form<AppUser> registrationForm=Form.form(AppUser.class);

	public static Result register(){
		return ok(views.html.registerAppUser.render(registrationForm));
	}

	public static Result registerProccess(){

		final Form<AppUser> filledForm=registrationForm.bindFromRequest();

		if(filledForm.hasErrors()) {
			Logger.info("*** user bad request");
			return badRequest(views.html.registerAppUser.render(filledForm));
		}
		else {
			final AppUser appUser  = filledForm.get();
			final Patient patient=new Patient();
			//appUser.patient=patient;
			patient.appUser=appUser;
			patient.save();
			appUser.save();
			Logger.info("*** user object ");
			return ok("Registerd ");
		}

	}



	public static Result form() {
		return ok(views.html.createPatient.render(form));
		//return TODO;
	}
	public static Result enterForm() {
		return ok(views.html.enter.render("hello"));
		//return TODO;
	}


	public static Result process() {
		final Form<Patient> filledForm = form.bindFromRequest();
		//Logger.info("enteredt");

		if(filledForm.hasErrors()) {
			Logger.info("bad request");
			//System.out.println("dsdshillllllll");
			System.out.println(filledForm.errors());
			return badRequest(views.html.createPatient.render(filledForm));
		}
		else {
			final Patient patient= filledForm.get();

			if(patient.id == null) {


				patient.save();
			}
			else {
			
				patient.update();
			}
		}
		//return ok(views.html.scheduleAppointment.render("hello"));
		//return redirect(routes.UserController.list());
		return TODO;

	}


}
