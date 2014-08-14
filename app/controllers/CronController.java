package controllers;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import models.Role;
import models.doctor.Appointment;
import models.doctor.AppointmentStatus;
import models.doctor.Day;
import models.doctor.DaySchedule;
import models.doctor.Doctor;
import models.doctor.DoctorClinicInfo;

import org.joda.time.DateTime;

import play.Logger;
import play.mvc.Controller;
import play.mvc.Result;

import com.avaje.ebean.Ebean;

public class CronController extends Controller {



	public static Result startCron(){
		CronController.createNextDayAppointment();
		return ok("cron has been started");
	}


	public static void deleteOutdatedAppointments() {



		final Calendar calendar=Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.set(Calendar.HOUR_OF_DAY, 00);
		calendar.set(Calendar.MINUTE, 00);

		final List<Appointment> outdatedAppt = Appointment.find.where().isNull("requestedBy").lt("appointmentTime", calendar.getTime()).findList();

		Ebean.delete(outdatedAppt);


		Logger.info("Deleted");
	}

	public static void createNextDayAppointment() {
		try {



			final List<Doctor> doctors = Doctor.find.all();
			for (final Doctor doctor : doctors) {


				final Calendar calendar1 = Calendar.getInstance();
				final Calendar calendar2 = Calendar.getInstance();
				final SimpleDateFormat dateFormat = new SimpleDateFormat("kk:mm");



				for(final DoctorClinicInfo docClinicInfo : doctor.getActiveClinic()){


					DateTime dateTime = new DateTime();
					dateTime = dateTime.plusMonths(3);;
					dateTime = dateTime.minusHours(dateTime.getHourOfDay());
					dateTime = dateTime.minusMinutes(dateTime.getMinuteOfHour());
					dateTime = dateTime.minusSeconds(dateTime.getSecondOfMinute());

					final Calendar toDate=Calendar.getInstance();
					toDate.setTime(dateTime.toDate());
					Logger.debug(""+doctor.appUser.email);
					final List<Appointment> appointments = Appointment.find.where().eq("doctorClinicInfo", docClinicInfo).orderBy("appointmentTime desc").findList();
					if(appointments.size() > 0){

						final Date date = appointments.get(0).appointmentTime;


						Logger.debug("3 month later"+toDate.getTime());
						Logger.debug("date next to last appt"+date);

						final Calendar calendar = Calendar.getInstance();
						calendar.setTime(date);
						calendar.add(Calendar.DATE, 1);
						calendar.set(Calendar.HOUR_OF_DAY, 0);
						calendar.set(Calendar.MINUTE, 0);
						calendar.set(Calendar.SECOND,0);

						if(toDate.after(calendar)){

							int dayss = toDate.get(Calendar.DATE)-calendar.get(Calendar.DATE);
							Logger.debug("while count"+dayss);
							while ( dayss >= 0) {


								for (final DaySchedule schedule : docClinicInfo.scheduleDays) {
									Logger.debug(schedule.day+"=="+Day.getDay(calendar
											.get(Calendar.DAY_OF_WEEK) - 1));
									if (schedule.day == Day.getDay(calendar
											.get(Calendar.DAY_OF_WEEK) - 1)) {
										try {
											calendar1
											.setTime(dateFormat.parse(schedule.toTime));
											calendar2.setTime(dateFormat
													.parse(schedule.fromTime));
										} catch (final Exception e) {
											e.printStackTrace();
										}
										final int hoursToClinic = calendar1
												.get(Calendar.HOUR_OF_DAY)
												- calendar2.get(Calendar.HOUR_OF_DAY);
										final int minutsToClinic = calendar1
												.get(Calendar.MINUTE)
												- calendar2.get(Calendar.MINUTE);
										Logger.info("total minutes***"
												+ calendar1.get(Calendar.MINUTE));
										calendar.set(Calendar.HOUR_OF_DAY,
												calendar2.get(Calendar.HOUR_OF_DAY));
										calendar.set(Calendar.MINUTE,
												calendar2.get(Calendar.MINUTE));
										calendar.set(Calendar.SECOND, 0);
										calendar.set(Calendar.MILLISECOND, 0);
										if (schedule.requester.equals(Role.PATIENT)) {
											for (int j2 = 0; j2 < (((hoursToClinic * 60) + minutsToClinic) / docClinicInfo.slot); j2++) {
												if (Appointment.find
														.where()
														.eq("doctorClinicInfo", docClinicInfo)
														.eq("appointmentTime",
																calendar.getTime())
																.findUnique() == null) {
													Logger.info("  " + calendar.getTime());
													final Appointment appointment = new Appointment();
													appointment.appointmentStatus = AppointmentStatus.AVAILABLE;
													appointment.appointmentTime = calendar.getTime();
													appointment.doctorClinicInfo = docClinicInfo;
													appointment.save();
													calendar.add(Calendar.MINUTE,
															docClinicInfo.slot);
												} else {
													calendar.add(Calendar.MINUTE,
															docClinicInfo.slot);
												}
											}
										} else {
											for (int j2 = 0; j2 < (((hoursToClinic * 60) + minutsToClinic) / docClinicInfo.slotMR); j2++) {
												if (Appointment.find
														.where()
														.eq("doctorClinicInfo", docClinicInfo)
														.eq("appointmentTime",
																calendar.getTime())
																.findUnique() == null) {
													Logger.info("  " + calendar.getTime());
													final Appointment appointment = new Appointment();
													appointment.appointmentStatus = AppointmentStatus.AVAILABLE;
													appointment.appointmentTime = calendar
															.getTime();
													appointment.doctorClinicInfo = docClinicInfo;
													appointment.save();
													calendar.add(Calendar.MINUTE,
															docClinicInfo.slotMR);
												} else {
													calendar.add(Calendar.MINUTE,
															docClinicInfo.slotMR);
												}
											}
										}
									}
								}
								Logger.info("***end of shedules");
								calendar.add(Calendar.DATE, 1);
								dayss--;
							}
						}
					}
				}
			}
			Logger.info("Created");

		} catch (final Exception e) {
			e.printStackTrace();
		}
	}







}
