package models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import play.data.validation.Constraints.Required;
import play.db.ebean.*;

@Entity
public class Doctor extends AppUser {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public  Long id;

	@Required
	public String specialization;

	@Required
	public String degree;

	public String doctorType;								//government or private

	public String experience;

	public String homeFacility;

	public Integer fees;


	@OneToOne
	public String clinicAddress;

	@OneToOne
	public String hospitalAddress;

	@Required
	public String timings;


}
