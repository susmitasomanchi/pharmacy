/*****

 THIS IS AN AUTO GENERATED CODE

 PLEASE DO NOT MODIFY IT BY HAND

 *****/
package models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;

import models.doctor.Doctor;
import play.data.validation.Constraints.Email;
import play.db.ebean.Model;

@SuppressWarnings("serial")
@Entity
public class AppUser extends BaseEntity {

	@Id
	public Long id;

	@Lob
	public byte[] image;

	public String name;

	public String username;

	@Email
	public String email;

	public String password;

	public Sex sex;

	public Date dob;

	public Role role;

	public static Model.Finder<Long, AppUser> find = new Finder<Long, AppUser>(Long.class, AppUser.class);

	public Patient getPatient() {
		return Patient.find.where().eq("appUser.id", this.id).findUnique();
	}

	public Doctor getDoctor() {
		return Doctor.find.where().eq("appUser.id", this.id).findUnique();
	}

	public Pharmacist getPharmacist() {
		return Pharmacist.find.where().eq("appUser.id", this.id).findUnique();
	}

	public MedicalRepresentative getMedicalRepresentative() {
		return MedicalRepresentative.find.where().eq("appUser.id", this.id).findUnique();
	}

	public DiagnosticRepresentative getDiagnosticRepresentative() {
		return DiagnosticRepresentative.find.where().eq("appUser.id", this.id).findUnique();
	}


}
