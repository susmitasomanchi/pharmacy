package models.doctor;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import play.Logger;
import models.AppUser;
import models.BaseEntity;


@SuppressWarnings("serial")
@Entity
public class Appointment extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public  Long id;

	public Date appointmentTime;

	public AppointmentStatus appointmentStatus;

	@OneToOne
	public AppUser requestedBy;

	@OneToOne
	public AppUser apporovedBy;

	@Column(columnDefinition="TEXT")
	public String problemStatement;

	@OneToOne
	public DoctorClinicInfo	doctorClinicInfo;

	public Date bookedOn;

	public static Finder<Long, Appointment> find = new Finder<Long, Appointment>(Long.class, Appointment.class);

	public static List<Appointment> getAvailableAppointmentList( final Long docClincId, final Date fromDate, final Date toDate) {
		final List<Appointment>  list=Appointment.find.where().eq("doctorClinicInfo.id", docClincId).between("appointmentTime", fromDate, toDate).order().asc("appointmentTime").findList();
		return list;
	}


	public static List<Appointment> getAvailableMrAppointmentList(final Doctor doctor,final Date date) {
		final List<Appointment> list=null;
		/*	final Calendar calendar=Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY,doctor.doctorClinicInfoList.get(0).toHrsMr);
		calendar.set(Calendar.MINUTE,59);
		calendar.set(Calendar.SECOND,59);
		calendar.set(Calendar.MILLISECOND,59);

		list=Appointment.find.where().eq("doctor", doctor).between("appointmentTime", date, calendar.getTime()).
				order().asc("appointmentTime").findList();
		 */
		return list;
	}

	public Prescription getPrescription(){
		final int rows = Prescription.find.where().eq("appointment", this).findRowCount();
		Logger.error("No Prescription found for appointment Id: "+this.id);
		if(rows==0){
			return null;
		}
		return Prescription.find.where().eq("appointment", this).findList().get(0);
	}
	/*public static List<Day> getAvailableAppointmentTime(){
		final List<Appointment> appointments = Appointment.find.where().eq(arg0, arg1)
				for (final Day day : days) {
					if()

				}*/







}
