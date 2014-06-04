package models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import play.data.validation.Constraints.Required;
import play.db.ebean.Model;


@Entity
public class Doctor extends Model {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public  Long id;

	@Required
	public String specialization;

	@OneToOne
	public AppUser appUser;

	@Required
	public String degree;

	public String doctorType;								//government or private

	public String experience;

	public String homeFacility;

	public Integer fees;


	public String clinicAddress;

	public String hospitalAddress;

	@Required
	public String timings;

	public String categoryOfDoctor;						// homeopathic or ayurvedic or etc.






	public static Model.Finder<Long,Doctor> find = new Finder<Long, Doctor>(Long.class, Doctor.class);

}

