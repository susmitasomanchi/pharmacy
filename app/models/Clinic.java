package models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import play.db.ebean.Model;

@SuppressWarnings("serial")
@Entity
public class Clinic extends Model{

	@GeneratedValue(strategy=GenerationType.AUTO)
	@Id
	public Long id;

	public Address clinicAddress;

	@ManyToOne
	public Doctor doctor;

	@OneToMany(mappedBy="clinic")
	public DoctorSchedule timeSchedule;


	public static Model.Finder<Long, Clinic> find = new Finder<Long, Clinic>(
			Long.class, Clinic.class);

}
