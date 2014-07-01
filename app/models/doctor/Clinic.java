package models.doctor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import models.Address;
import play.db.ebean.Model;

@SuppressWarnings("serial")
@Entity
public class Clinic extends Model{

	@GeneratedValue(strategy=GenerationType.AUTO)
	@Id
	public Long id;

	public String name;

	public Address clinicAddress;


	public static Model.Finder<Long, Clinic> find = new Finder<Long, Clinic>(Long.class, Clinic.class);

}
