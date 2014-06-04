package models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToOne;

import play.db.ebean.Model;

@Entity
public class Patient extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long id;

	@Lob
	public byte[] picture;

	@OneToOne(mappedBy="patient")
	public AppUser appUser;

	public String disease;

	@OneToOne
	public Appointment appointment;

	public Long appointmentId;


	public String doctorAvailability;


	public String isUrgentPatient;

	public static Model.Finder<Long, Patient> find = new Finder<Long, Patient>(Long.class, Patient.class);

}
