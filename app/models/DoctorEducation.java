package models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import play.data.validation.Constraints.Required;


@SuppressWarnings("serial")
@Entity
public class DoctorEducation extends BaseEntity{
	

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public  Long id;
	

	//Education
	
	
	@Required
	public String collegeName;
	
	@Required
	public String degree;
	
	@Required
	public Integer fromYear;
	
	@Required
	public Integer toYear;
	
	

}
