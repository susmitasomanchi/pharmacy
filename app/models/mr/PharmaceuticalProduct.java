package models.mr;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import models.BaseEntity;
import play.data.validation.Constraints.Required;

@SuppressWarnings("serial")
@Entity
public class PharmaceuticalProduct extends BaseEntity{

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

	public static Finder<Long, PharmaceuticalProduct> find = new Finder<Long, PharmaceuticalProduct>(Long.class, PharmaceuticalProduct.class);


	public static Map<String, String> options() {

		final LinkedHashMap<String, String> vals = new LinkedHashMap<String, String>();
		for (final PharmaceuticalProduct val : PharmaceuticalProduct.find.all()) {
			vals.put(val.toString(), val.toString());
		}
		return vals;
	}

}
