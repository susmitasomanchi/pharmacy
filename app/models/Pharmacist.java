package models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import play.db.ebean.Model;

@Entity
public class Pharmacist extends Model {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long id;


	public String category;
	public static Finder<Long, Pharmacist> find = new Finder<Long, Pharmacist>(Long.class, Pharmacist.class);

}
