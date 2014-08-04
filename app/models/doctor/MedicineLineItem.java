package models.doctor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import models.BaseEntity;
import models.MasterProduct;
import play.db.ebean.Model;

@SuppressWarnings("serial")
@Entity
public class MedicineLineItem extends BaseEntity{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public  Long id;

	@OneToOne
	public MasterProduct medicine;
	
	public String fullNameOfMedicine;

	public String medicineFullName;

	@Column(columnDefinition="TEXT")
	public String dosage;

	@Column(columnDefinition="TEXT")
	public String frequency;

	@Column(columnDefinition="TEXT")
	public String remarks;

	public static Model.Finder<Long, MedicineLineItem> find = new Finder<Long, MedicineLineItem>(Long.class, MedicineLineItem.class);
}
