package controllers;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import models.Role;
import models.doctor.Appointment;
import models.doctor.AppointmentStatus;
import models.doctor.Day;
import models.doctor.DaySchedule;
import models.doctor.Doctor;
import models.doctor.DoctorClinicInfo;

import org.joda.time.DateTime;

import play.Logger;
import play.libs.Akka;
import play.mvc.Controller;
import play.mvc.Result;
import scala.concurrent.duration.Duration;
import actor.MyActor;
import akka.actor.ActorRef;
import akka.actor.Props;

@SuppressWarnings("deprecation")
public class CronController extends Controller {

	/*static ActorSystem actorSystem = ActorSystem.create( "play" );
	 */
	/*static {
		// Create our local actors
		actorSystem.actorOf( Props.create( MyActor.class ), "MyActor" );
	}*/
	/*
	public static Promise<Result> localHello( final String name )
	{
		// Look up the actor
		final ActorSelection myActor =
				actorSystem.actorSelection( "user/HelloLocalActor" );
		Akka.system().scheduler().scheduleOnce(
				Duration.create(10, TimeUnit.MILLISECONDS),
				new Runnable() {
					public void run() {
						//file.delete();
					}
				},
				Akka.system().dispatcher()
				);

	}*/

	public static Result startCron(){
		final ActorRef myActor = Akka.system().actorOf(new Props(MyActor.class));

		Akka.system().scheduler().schedule(
				Duration.create(0, TimeUnit.MILLISECONDS), //Initial delay 0 milliseconds
				Duration.create(1, TimeUnit.SECONDS),     //Frequency 30 minutes
				myActor,
				"tick",
				Akka.system().dispatcher(),
				null
				);
		return ok("cron has been started");
	}


	public static Result deleteOutdatedAppointments() {

		/*
		@SuppressWarnings("deprecation")
		ActorRef myActor = Akka.system().actorOf(new Props(AppUser.class));

		Logger.info("running");
		Akka.system().scheduler().schedule(
				Duration.create(0, TimeUnit.MILLISECONDS), //Initial delay 0 milliseconds
				Duration.create(30, TimeUnit.MINUTES),     //Frequency 30 minutes
				myActor,
				"tick",
				Akka.system().dispatcher(),
				null
				);*/

		/*Akka.system().scheduler().scheduleOnce(
				Duration.create(10, TimeUnit.MILLISECONDS),
				new Runnable() {
					public void run() {
						Logger.debug("created");
					}
				},
				Akka.system().dispatcher()
				);*/
		/*ce();
		calendar.setTime(new Date());
		calendar.set(Calendar.HOUR_OF_DAY, 00);
		calendar.set(Calendar.MINUTE, 00);

		final List<Appointment> outdatedAppt = Appointment.find.where().isNull("requestedBy").lt("appointmentTime", calendar.getTime()).findList();

		Ebean.delete(outdatedAppt);*/


		return ok("Deleted");
	}

	public static Result createNextDayAppointment() {

		final Doctor doctor = Doctor.find.byId(1L);

		DateTime dateTime = new DateTime();
		dateTime = dateTime.plusMonths(3);;
		dateTime = dateTime.minusHours(dateTime.getHourOfDay());
		dateTime = dateTime.minusMinutes(dateTime.getMinuteOfHour());
		dateTime = dateTime.minusSeconds(dateTime.getSecondOfMinute());

		final Calendar toDate=Calendar.getInstance();
		toDate.setTime(dateTime.toDate());

		final Date date = Appointment.find.where().eq("doctorClinicInfo.doctor", doctor).orderBy("appointmentTime desc").findList().get(0).appointmentTime;


		Logger.debug("3 month later"+toDate.getTime());
		Logger.debug("date next to last appt"+date);

		final Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, 1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND,0);


		final Calendar calendar1 = Calendar.getInstance();
		final Calendar calendar2 = Calendar.getInstance();
		final SimpleDateFormat dateFormat = new SimpleDateFormat("kk:mm");


		if(toDate.after(calendar)){

			int dayss = toDate.get(Calendar.DATE)-calendar.get(Calendar.DATE);
			Logger.debug("while count"+dayss);

			while ( dayss >= 0) {


				for(final DoctorClinicInfo docClinicInfo : doctor.getActiveClinic()){


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

				}
				Logger.info("***end of shedules");
				calendar.add(Calendar.DATE, 1);
				dayss--;
			}
		}




		return ok(dateTime.toDate().toString());
	}


}
