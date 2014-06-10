package models;



import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import play.db.ebean.Model;


@SuppressWarnings("serial")
@Entity
public class DoctorSchedule extends BaseEntity{

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Long id;

	@ManyToOne
	public Clinic clinic;

	public Date appointmentTime;

	public static Model.Finder<Long, DoctorSchedule> find = new Finder<Long, DoctorSchedule>(
			Long.class, DoctorSchedule.class);




}
