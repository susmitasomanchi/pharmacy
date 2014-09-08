/*****
 THIS IS AN AUTO GENERATED CODE
 PLEASE DO NOT MODIFY IT BY HAND
 *****/
package models;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToOne;

import models.diagnostic.DiagnosticRepresentative;
import models.doctor.Doctor;
import models.mr.MedicalRepresentative;
import models.patient.Patient;
import models.pharmacist.Pharmacist;

import org.apache.commons.codec.binary.Base64;
import org.joda.time.LocalDate;
import org.joda.time.Years;

import play.data.validation.Constraints.Email;
import play.db.ebean.Model;

@SuppressWarnings("serial")
@Entity
public class AppUser extends BaseEntity {

	@GeneratedValue(strategy=GenerationType.AUTO)
	@Id
	public Long id;

	@Lob
	public byte[] image;

	public String name;

	public String username;

	public Long mobileNumber;

	@Email
	public String email;

	@Column(columnDefinition="TEXT")
	public String password;

	@Column(columnDefinition="TEXT")
	public String salt;

	public Sex sex;

	public Date dob;

	public BloodGroup bloodGroup;

	public Boolean isBloodDonor;

	public List<Language> languageList = new ArrayList<Language>();

	public Role role;

	@OneToOne
	Address address;

	public boolean emailConfirmed = false;

	public boolean mobileNumberConfirmed = false;

	@Column(columnDefinition="TEXT")
	public String emailConfirmationKey;

	@Column(columnDefinition="TEXT")
	public String mobileNumberConfirmationKey;

	@Column(columnDefinition="TEXT")
	public String forgotPasswordConfirmationKey;
	
	@Column(columnDefinition="TEXT")
	public String allergy;
	
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

	public Boolean matchPassword(final String password){
		try{
			final String passwordWithSalt = password+this.salt;
			final MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
			final byte[] passBytes = passwordWithSalt.getBytes();
			final String hashedPassword = Base64.encodeBase64String(sha256.digest(passBytes));
			if(hashedPassword.compareTo(this.password) == 0){
				return true;
			}
			else{
				return false;
			}
		}
		catch(final Exception e){
			return false;
		}
	}

	public int getAge(){
		if(this.dob != null){
			final LocalDate birthdate = new LocalDate (this.dob);
			final LocalDate now = new LocalDate();
			return Years.yearsBetween(birthdate, now).getYears();
		}
		return 0;
	}

}
