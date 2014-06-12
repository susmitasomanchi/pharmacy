package models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

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
		public Date workedFrom;

		@Required
		public Date workedTo;
}
