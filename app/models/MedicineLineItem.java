package models;

import javax.persistence.Entity;

import models.doctor.SigCode;
import play.db.ebean.Model;

@SuppressWarnings("serial")
@Entity
public class MedicineLineItem extends BaseEntity{

	public Product medicine;
	public String frequency;
	public SigCode sigCode;
	public String remarks;

	public static Model.Finder<Long, MedicineLineItem> find = new Finder<Long, MedicineLineItem>(Long.class, MedicineLineItem.class);
}
