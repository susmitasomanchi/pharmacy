package models.doctor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import models.BaseEntity;
import play.db.ebean.Model.Finder;

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
		
		public static Finder<Long, DoctorAward> find = new Finder<Long, DoctorAward>(Long.class, DoctorAward.class);

		public static void update(final Long id, final DoctorAward doctorAward) {
			doctorAward.update(id);
		}
}
