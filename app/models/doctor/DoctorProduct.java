package models.doctor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import models.BaseEntity;
import play.data.validation.Constraints.Required;

@SuppressWarnings("serial")
@Entity
public class DoctorProduct extends BaseEntity{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public  Long id;

	@Required
	public String medicineName;

	public String brandName;

	public String salt;

	public String strength;

	public String description;

	public Long unitsPerPack;

	public String fullName;

	public static Finder<Long, DoctorProduct> find = new Finder<Long, DoctorProduct>(Long.class, DoctorProduct.class);

}
