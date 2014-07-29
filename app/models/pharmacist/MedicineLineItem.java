package models.pharmacist;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import models.BaseEntity;
import models.MasterProduct;
import models.doctor.SigCode;
import play.db.ebean.Model;

@SuppressWarnings("serial")
@Entity
public class MedicineLineItem extends BaseEntity{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public  Long id;

	@OneToOne
	public MasterProduct medicine;
	public String frequency;
	public SigCode sigCode;
	public String remarks;

	public static Model.Finder<Long, MedicineLineItem> find = new Finder<Long, MedicineLineItem>(Long.class, MedicineLineItem.class);
}
