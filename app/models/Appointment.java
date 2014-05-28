package models;

import java.util.Date;

import javax.persistence.Entity;

import play.data.validation.Constraints.Required;
import play.db.ebean.*;

@Entity
public class Appointment extends BaseEntity {

	@Required
	public Date date;

	@Required
	public Role role;

	@Required
	public Status appointmentStatus;









}
