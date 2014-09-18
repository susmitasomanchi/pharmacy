package models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import models.AppUser;
import models.BaseEntity;
import play.db.ebean.Model;
@SuppressWarnings("serial")
@Entity
public class WeightTracker extends BaseEntity{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long id;

	public Float weight;

	public Date date;

	@OneToOne
	public AppUser appUser;

	public static Model.Finder<Long,WeightTracker> find = new Model.Finder<Long, WeightTracker>(Long.class, WeightTracker.class);


}
