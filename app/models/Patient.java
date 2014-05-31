package models;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;

import play.db.ebean.Model;

@SuppressWarnings("serial")
@Entity
public class Patient extends AppUser {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long id;

	@Lob
	public byte[] picture;


	public String disease;


	public Long appointmentId;


	

	public static Model.Finder<Long, Patient> find = new Finder<>(Long.class, Patient.class);

}
