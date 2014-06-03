package models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

import play.data.validation.Constraints.Required;
import play.db.ebean.*;

@Entity
public class Appointment extends BaseEntity {

	@Required
	public Date from;

	@Required
	public Date to;

	@Required
	public Status appointmentStatus;

	public AppUser requestedBy;

	public AppUser apporovedBy;


	public String remarks;

	public static Finder<Long, Appointment> find = new Finder<>(Long.class, Appointment.class);







}
