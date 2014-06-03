
/*****

 THIS IS AN AUTO GENERATED CODE
 PLEASE DO NOT MODIFY IT BY HAND

 *****/
package models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToOne;

import play.data.validation.Constraints.Email;
import play.db.ebean.Model;


@SuppressWarnings("serial")
@Entity

public class AppUser extends BaseEntity{

	@Id
	public Long id;

	@Lob
	public byte[] picture;

	public String name;

	@OneToOne
	public Patient patient;

	@OneToOne
	public Doctor doctor;

	@OneToOne
	public DiagnosticRep diagnosticRep;

	@OneToOne
	public Pharmacist pharmacist;

	@OneToOne
	public SalesRep salesRep;

	@OneToOne
	public DoctorAssistant assistant;


	public String username;


	@Email
	public String email;


	public String password;


	public String gender;


	public Integer age;



	public static Model.Finder<Long, AppUser> find = new Finder<Long, AppUser>(Long.class, AppUser.class);




}
