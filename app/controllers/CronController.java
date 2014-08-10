package controllers;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import models.doctor.Appointment;
import models.doctor.AppointmentStatus;
import models.doctor.Day;
import models.doctor.DaySchedule;
import models.doctor.Doctor;
import models.doctor.DoctorClinicInfo;
import play.mvc.Controller;
import play.mvc.Result;

import com.avaje.ebean.Ebean;

public class CronController extends Controller {

	public static Result deleteOutdatedAppointments() {

		final Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.set(Calendar.HOUR_OF_DAY, 00);
		calendar.set(Calendar.MINUTE, 00);

		final List<Appointment> outdatedAppt = Appointment.find.where().isNull("requestedBy").lt("appointmentTime", calendar.getTime()).findList();

		Ebean.delete(outdatedAppt);


		return ok("Deleted");
	}

	public static Result createNextDayAppointment(final Long docId) {

		final Doctor doctor = Doctor.find.byId(1L);

		final Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.DATE, 1);
		calendar.set(Calendar.HOUR_OF_DAY, 00);
		calendar.set(Calendar.MINUTE, 00);

		final List<Appointment> appointments = Appointment.find.where().ge("appointmentTime", calendar.getTime()).findList();

		if(appointments.size() == 0){
			for (final DoctorClinicInfo docclinicInfo : doctor.doctorClinicInfoList) {
				for (final DaySchedule daySchedule : docclinicInfo.scheduleDays) {
					if (daySchedule.day == Day.getDay(calendar
							.get(Calendar.DAY_OF_WEEK) - 1)){

						final Appointment appointment = new Appointment();
						appointment.appointmentStatus = AppointmentStatus.AVAILABLE;
						appointment.appointmentTime = calendar.getTime();
						appointment.doctorClinicInfo = docclinicInfo;
					}
				}
			}


		}



		return ok("");
	}


}
