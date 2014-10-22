package models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import play.data.validation.Constraints.Required;

@SuppressWarnings("serial")
@Entity
public class Address extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public  Long id;

	@Required
	public String addressLine1;

	public String addressLine2;

	public String addressLine3;

	public String area;

	public String latitude;

	public String longitude;

	@Required
	public String city;

	public State state;

	public String pinCode;

	public String fetchedPinCode;

	public Country country;

	@OneToOne
	public Locality locality;

	public static Finder<Long, Address> find = new Finder<Long, Address>(Long.class, Address.class);


}
