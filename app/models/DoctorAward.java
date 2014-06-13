package models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@SuppressWarnings("serial")
@Entity
public class DoctorAward extends BaseEntity{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public  Long id;
	
	//awards
	
		public String awardName;
		
		public String awardFor;
		
		public String year;
		
		public String commentForAwards;
}
