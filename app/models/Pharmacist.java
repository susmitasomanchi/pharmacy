package models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import play.db.ebean.Model;

@Entity
public class Pharmacist extends Model {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long id;

	@OneToOne
	public AppUser appUser;
	public String category;
	public static Finder<Long, Pharmacist> find = new Finder<Long, Pharmacist>(Long.class, Pharmacist.class);

}
