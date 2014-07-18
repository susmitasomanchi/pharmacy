package models.doctor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import models.BaseEntity;
import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

@SuppressWarnings("serial")
@Entity
public class DoctorExperience extends BaseEntity{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public  Long id;

	@Required
	public String institutionName;

	public String position;

	@Column(columnDefinition="TEXT")
	public String description;

	@Required
	public Integer workedFrom;

	public Integer workedTo;

	public static Model.Finder<Long,DoctorExperience> find = new Finder<Long, DoctorExperience>(Long.class, DoctorExperience.class);
}
