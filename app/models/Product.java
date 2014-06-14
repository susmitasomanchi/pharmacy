package models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import play.data.validation.Constraints.Required;
import play.db.ebean.Model.Finder;

@SuppressWarnings("serial")
@Entity
public class Product extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public  Long id;

	@Required
	public String medicineName;

	@Required
	public String brandName;

	public String salt;

	@Required
	public String strength;

	@Required
	public String typeOfMedicine;

	public String description;

	@Required
	public Long unitsPerPack;

	public String fullName;


	public static Finder<Long, Product> find = new Finder<Long, Product>(Long.class, Product.class);

	public static void update(final Long id, final Product product) {
		product.update(id);
	}

}
