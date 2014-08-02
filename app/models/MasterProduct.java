package models;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import play.data.validation.Constraints.Required;

@SuppressWarnings("serial")
@Entity
public class MasterProduct extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public  Long id;

	@Required
	public String medicineName;

	public String brandName;

	public String salt;

	public String strength;

	@Column(columnDefinition="TEXT")
	public String description;

	public Long unitsPerPack;

	public String fullName;

	public static Finder<Long, MasterProduct> find = new Finder<Long, MasterProduct>(Long.class, MasterProduct.class);


	public static Map<String, String> options() {

		final LinkedHashMap<String, String> vals = new LinkedHashMap<String, String>();
		for (final MasterProduct val : MasterProduct.find.all()) {
			vals.put(val.toString(), val.toString());
		}
		return vals;
	}

}
