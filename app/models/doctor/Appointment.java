package models.doctor;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

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

	public String remarks;

	@OneToOne
	public Doctor doctor;

	@OneToOne
	public Clinic clinic;



	public static List<Appointment> getAvailableAppointmentList( final Long doctorId, final Date fromDate, final Date toDate) {



		final List<Appointment>  list=Appointment.find.where().eq("doctor.id", doctorId).between("appointmentTime", fromDate, toDate).
				order().asc("appointmentTime").findList();

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

	public static Finder<Long, Appointment> find = new Finder<Long, Appointment>(Long.class, Appointment.class);






}
