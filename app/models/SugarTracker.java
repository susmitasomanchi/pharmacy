package models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import play.db.ebean.Model;

@SuppressWarnings("serial")
@Entity
public class SugarTracker extends BaseEntity{

	@GeneratedValue(strategy=GenerationType.AUTO)
	@Id
	public Long id;
	
	public Date date;

	public Float sugarLevel;
	
	@OneToOne
	public AppUser appUser;

	public static Model.Finder<Long,SugarTracker> find = new Model.Finder<Long, SugarTracker>(Long.class, SugarTracker.class);


}
