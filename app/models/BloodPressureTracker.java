package models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import play.db.ebean.Model;

@Entity
@SuppressWarnings("serial")
public class BloodPressureTracker extends BaseEntity{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long id;

	public int lowBp;

	public int highBp;

	public Date date;

	@OneToOne
	public AppUser appUser;

	public static Model.Finder<Long,BloodPressureTracker> find = new Model.Finder<Long, BloodPressureTracker>(Long.class, BloodPressureTracker.class);

}
