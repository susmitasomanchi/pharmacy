package controllers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import actions.BasicAuth;
import models.AppUser;
import models.Appointment;
import models.AppointmentStatus;
import models.Doctor;
import models.Patient;
import play.Logger;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import beans.PatientBean;

@BasicAuth
public class DoctorController extends Controller {



	public static Result requestAppointment(){
		final String datetime = request().body().asFormUrlEncoded().get("datetime")[0];
		final Long doctorId = Long.parseLong(request().body().asFormUrlEncoded().get("doctorId")[0]);
		final Doctor doctor = Doctor.find.byId(doctorId);
		final SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy~HH:mm");
		try {
			final Date date = sdf.parse(datetime);
			Logger.info("Date extracted: "+date);


			final Appointment appointment = new Appointment();
			appointment.requestedBy = LoginController.getLoggedInUser();
			appointment.appointmentTime = date;
			appointment.appointmentStatus = AppointmentStatus.REQUESTED;
			//appointment.remarks =
			appointment.save();
			doctor.appointmentList.add(appointment);
			doctor.update();






			return ok("Date extracted: "+date+" Doctor: "+doctor.appUser.name);
		} catch (final ParseException e) {
			e.printStackTrace();
		}
		return ok();
	}





	public static Result newAssistant(){
		return ok(views.html.doctor.newAssistant.render());
	}











	public static Form<Doctor> form = Form.form(Doctor.class);
	public static Form<PatientBean> patientForm = Form.form(PatientBean.class);

	public static Result form() {
		return ok(views.html.createDoctor.render(form));
		//return TODO;
	}

	public static Result process() {
		final Form<Doctor> filledForm = form.bindFromRequest();
		//Logger.info("enteredt");

		if(filledForm.hasErrors()) {
			Logger.info("bad request");

			return badRequest(views.html.createDoctor.render(filledForm));
		}
		else {
			final Doctor doctor= filledForm.get();

			if(doctor.id == null) {

				doctor.save();
			}
			else {

				doctor.update();
			}
		}
		return TODO;
		//return redirect(routes.UserController.list());

	}

	//doctor Action
	public static Result docStuff(){
		return ok(views.html.doctorStuff.render());
	}

	//register patient by doctor

	public static Result registerPatient(){

		return ok(views.html.registerPatient.render(patientForm));
	}


	public static Result registerPatientProccess(){

		final Form<PatientBean> filledForm=patientForm.bindFromRequest();

		if(filledForm.hasErrors()){
			Logger.info("*******Bad Request ************");
			return ok(views.html.registerPatient.render(filledForm));

		}else{
			final AppUser appUser=filledForm.get().toAppUserEntity();
			final Patient patient=filledForm.get().toPatientEntity();

			if((appUser.id==null) && (patient.id==null)){
				appUser.save();
				patient.save();
			}else{
				appUser.update();
				patient.update();
			}
		}


		return ok("patient register successFully");
	}


}
