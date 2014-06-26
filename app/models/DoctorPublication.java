package models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import play.db.ebean.Model.Finder;

@SuppressWarnings("serial")
@Entity
public class DoctorPublication extends BaseEntity{
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public  Long id;
	
	public String articleName;
	
	public String articleFor;
	
	public String year;
	
	public String commentForArticle;
	
	public static Finder<Long, DoctorPublication> find = new Finder<Long, DoctorPublication>(Long.class, DoctorPublication.class);

	public static void update(final Long id, final DoctorPublication doctorPublication) {
		doctorPublication.update(id);
	}
}
