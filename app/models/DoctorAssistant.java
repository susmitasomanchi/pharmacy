package models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import play.data.validation.Constraints.Required;
import play.db.ebean.Model;
import play.db.ebean.Model.Finder;

@Entity
public class DoctorAssistant extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public  Long id;

	@Required
	public String degree;

	public String experience;


	@OneToOne(mappedBy="assistant")
	public AppUser appUser;

	public static Model.Finder<Long,DoctorAssistant> find = new Finder<Long, DoctorAssistant>(Long.class, DoctorAssistant.class);
}
