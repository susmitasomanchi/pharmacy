package models.patient;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import models.AppUser;
import models.BaseEntity;
import models.doctor.Doctor;
import play.db.ebean.Model;

@SuppressWarnings("serial")
@Entity
public class PatientDoctorInfo extends BaseEntity {

	@Id
	public Long id;

	@OneToOne
	public Doctor doctor;

	@OneToOne
	public Patient  patient;

	@OneToOne
	public AppUser createdBy;

	public static Model.Finder<Long, PatientDoctorInfo> find = new Finder<Long, PatientDoctorInfo>(Long.class, PatientDoctorInfo.class);

}
