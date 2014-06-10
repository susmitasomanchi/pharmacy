package models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import play.db.ebean.Model;

@SuppressWarnings("serial")
@Entity
public class DoctorClinicInfo extends Model {

	@GeneratedValue(strategy = GenerationType.AUTO)
	@Id
	public Long id;

	public Clinic clinic;

	public Date fromTime;

	public Date toTime;

	public static Model.Finder<Long, DoctorClinicInfo> find = new Finder<Long, DoctorClinicInfo>(Long.class, DoctorClinicInfo.class);

}
