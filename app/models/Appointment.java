package models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

import play.data.validation.Constraints.Required;

@Entity
public class Appointment extends BaseEntity {

	@Required
	public Date fromTime;

	@Required
	public Date toTime;

	public Status appointmentStatus;

	@OneToOne
	public AppUser requestedBy;

	@OneToOne
	public AppUser apporovedBy;

	public String remarks;

	public static Finder<Long, Appointment> find = new Finder<>(Long.class, Appointment.class);







}
