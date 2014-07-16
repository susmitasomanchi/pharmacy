package models.pharmacist;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import models.BaseEntity;
import play.data.validation.Constraints.Required;


@Entity
public class PharmacyProduct extends BaseEntity{


	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public  Long id;

	@Required
	public String medicineName;

	//@Required
	public String brandName;

	public String salt;

	//@Required
	public String strength;

	//@Required
	public String typeOfMedicine;

	public String description;

	//@Required
	public Long unitsPerPack;

	public String fullName;

	@ManyToOne(cascade=CascadeType.ALL)
	public Pharmacy pharmacy;

	public static Finder<Long, PharmacyProduct> find = new Finder<Long, PharmacyProduct>(Long.class, PharmacyProduct.class);



}
