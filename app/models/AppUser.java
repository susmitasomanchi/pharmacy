/*****

 THIS IS AN AUTO GENERATED CODE
 PLEASE DO NOT MODIFY IT BY HAND

 *****/
package models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;

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

	public Patient getPatient() {
		final Patient patient=Patient.find.where().eq("appUser.id", this.id).findUnique();
		return patient;

	}
	public Doctor getDoctor() {
		final Doctor doctor=Doctor.find.where().eq("appUser.id", this.id).findUnique();
		return doctor;

	}
	public Pharmacist getPharmacist() {
		final Pharmacist pharmacist=Pharmacist.find.where().eq("appUser.id", this.id).findUnique();
		return pharmacist;

	}
	public MedicalRepresentative getMedicalRepresentative() {
		final MedicalRepresentative medicalRepresentative=MedicalRepresentative.find.where().eq("appUser.id", this.id).findUnique();
		return medicalRepresentative;

	}
	public DiagnosticRepresentative getDiagnosticRepresentative() {
		final DiagnosticRepresentative diagnosticRepresentative=DiagnosticRepresentative.find.where().eq("appUser.id", this.id).findUnique();
		return diagnosticRepresentative;

	}

	public static Model.Finder<Long, AppUser> find = new Finder<Long, AppUser>(
			Long.class, AppUser.class);

}
