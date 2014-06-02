package models;

import java.util.Date;

import javax.persistence.Entity;

import play.data.validation.Constraints.Required;

@Entity
public class Appointment extends BaseEntity {

	@Required
	public Date date;

	@Required
	public Role role;

	@Required
	public Status appointmentStatus;


	public static Finder<Long, Appointment> find = new Finder<>(Long.class, Appointment.class);







}
