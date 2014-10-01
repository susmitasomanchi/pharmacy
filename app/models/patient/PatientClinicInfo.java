package models.patient;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import models.BaseEntity;
import models.doctor.Clinic;
import play.db.ebean.Model;

@SuppressWarnings("serial")
@Entity
public class PatientClinicInfo extends BaseEntity{

	@Id
	public Long id;

	@OneToOne
	public Clinic clinic;

	@OneToOne
	public Patient  patient;

	public static Model.Finder<Long, PatientClinicInfo> find = new Finder<Long, PatientClinicInfo>(Long.class, PatientClinicInfo.class);

	public List<PatientClinicInfo> getPatientClinicInfos(final Long patientId){
		return PatientClinicInfo.find.where().eq("patient", Patient.find.byId(patientId)).findList();
	}

}
