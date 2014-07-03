package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import models.mr.PharmaceuticalCompany;
import play.data.validation.Constraints.Required;

@SuppressWarnings("serial")
@Entity
public class Product extends BaseEntity {

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
	public PharmaceuticalCompany pharmaceuticalCompany;
	
	@ManyToOne(cascade=CascadeType.ALL)
	public Pharmacy pharmacy;

	public static Finder<Long, Product> find = new Finder<Long, Product>(Long.class, Product.class);

	public static void update(final Long id, final Product product) {
		product.update(id);
	}
	
	public static Map<String, String> options() {

		final LinkedHashMap<String, String> vals = new LinkedHashMap<String, String>();
		for (final Product val : Product.find.all()) {
			vals.put(val.toString(), val.toString());
		}
		return vals;
	}

}
