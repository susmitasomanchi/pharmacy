package models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

@SuppressWarnings("serial")
@Entity
public class DoctorAssistant extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long id;

	@OneToOne
	public AppUser appUser;

	@Required
	public String degree;

	public String experience;

	public static Model.Finder<Long, DoctorAssistant> find = new Finder<Long, DoctorAssistant>(Long.class, DoctorAssistant.class);
}
