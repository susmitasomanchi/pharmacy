package models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import play.db.ebean.Model;

@SuppressWarnings("serial")
@Entity
public class DoctorClinicInfo extends BaseEntity {

	@GeneratedValue(strategy = GenerationType.AUTO)
	@Id
	public Long id;

	@OneToOne
	public Clinic clinic;

	@OneToOne
	public Doctor doctor;

	public Integer fromHrs;

	public Integer toHrs;

	@OneToOne
	DoctorAssistant assistant;

	public static Model.Finder<Long, DoctorClinicInfo> find = new Finder<Long, DoctorClinicInfo>(Long.class, DoctorClinicInfo.class);

}
