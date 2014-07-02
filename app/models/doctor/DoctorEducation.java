package models.doctor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import models.BaseEntity;
import play.data.validation.Constraints.Required;
import play.db.ebean.Model;
import play.db.ebean.Model.Finder;


@SuppressWarnings("serial")
@Entity
public class DoctorEducation extends BaseEntity{
	

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public  Long id;
	

	//Education
	
	
	@Required
	public String collegeName;
	
	
	public String degree;
	
	@Required
	public Integer fromYear;
	
	@Required
	public Integer toYear;
	
	public static Model.Finder<Long,DoctorEducation> find = new Finder<Long, DoctorEducation>(Long.class, DoctorEducation.class);
	
	public static void update(final Long id, final DoctorEducation doctorEducation) {
		doctorEducation.update(id);
	}

}
