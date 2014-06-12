package models;

import javax.persistence.Entity;

import play.db.ebean.Model;

@SuppressWarnings("serial")
@Entity
public class PrescribedMedicine extends BaseEntity{


	SigCode sigCode;
	String remarks;

	public static Model.Finder<Long, PrescribedMedicine> find = new Finder<Long, PrescribedMedicine>(Long.class, PrescribedMedicine.class);
}
