package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import play.data.validation.Constraints.Required;

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
	
}
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


	public Long appointmentId;


	public String doctorAvailability;


	public String isUrgentPatient;

	public static Model.Finder<Long, Patient> find = new Finder<Long, Patient>(Long.class, Patient.class);

}
