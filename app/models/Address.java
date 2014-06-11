package models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import play.data.validation.Constraints.Required;
import play.db.ebean.*;
import play.db.ebean.Model.Finder;

@Entity
public class Address extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public  Long id;

	@Required
	public String addrressLine1;


	public String addrressLine2;

	public String addrressLine3;


	@Required
	public String city;


	@Required
	public State state;

	@Required
	public Long pinCode;

	@Required
	public Country country;







	public static Finder<Long, Product> find = new Finder<Long, Product>(Long.class, Product.class);


}
