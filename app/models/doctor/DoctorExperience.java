package models.doctor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import models.BaseEntity;
import play.data.validation.Constraints.Required;

@SuppressWarnings("serial")
@Entity
public class DoctorExperience extends BaseEntity{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public  Long id;

	//Experience

	@Required
	public String previousHospitalName;  //hospital name

	public String workedAs;

	@Required
	public String location;

	@Required
	public Integer workedFrom;

	@Required
	public Integer workedTo;
}
