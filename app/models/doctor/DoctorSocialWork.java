package models.doctor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import models.BaseEntity;
import play.data.validation.Constraints.Required;
import play.db.ebean.Model;


@SuppressWarnings("serial")
@Entity
public class DoctorSocialWork extends BaseEntity{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public  Long id;

	@Required
	public String title;

	@Required
	@Column(columnDefinition="TEXT")
	public String description;

	public static Model.Finder<Long, DoctorSocialWork> find = new Finder<Long, DoctorSocialWork>(Long.class, DoctorSocialWork.class);

}
