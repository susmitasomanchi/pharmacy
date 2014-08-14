package models.doctor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import models.BaseEntity;
import play.db.ebean.Model;

@SuppressWarnings("serial")
@Entity
public class DoctorSpecialization extends BaseEntity{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long id;

	@Column(columnDefinition="TEXT")
	public String name;

	@Column(columnDefinition="TEXT")
	public String remarks;

	public static Model.Finder<Long,DoctorSpecialization> find = new Finder<Long, DoctorSpecialization>(Long.class, DoctorSpecialization.class);

}
