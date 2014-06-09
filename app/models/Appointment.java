package models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

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


	public static Finder<Long, Appointment> find = new Finder<>(Long.class, Appointment.class);



}