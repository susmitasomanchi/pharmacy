package models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import play.db.ebean.Model;

@SuppressWarnings("serial")
@Entity
public class SalesRep extends Model{

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Long id;

	@OneToOne
	public AppUser appUser;

	public String regionAlloted;

	public String companyName;

	public String typesOfMedecine;

	public int noOfDoctorsVisit;

	public static Finder<Long, SalesRep> find = new Finder<Long, SalesRep>(Long.class, SalesRep.class);
}
