package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import play.data.validation.Constraints.Required;
import play.db.ebean.Model;
import play.db.ebean.Model.Finder;

@Entity
public class Patient extends AppUser {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long id;


	@Required
	public String disease;

	@Id
	public Long appointmentId;

	@Required
	public String doctorAvailability;

	@Required
	public String isUrgentPatient;

	public static Model.Finder<Long, Patient> find = new Finder<>(Long.class, Patient.class);

}
