package models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import play.data.validation.Constraints.Required;

@SuppressWarnings("serial")
@Entity
public class Address extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public  Long id;

	@Required
	public String addrressLine1;

	public String addrressLine2;

	public String addrressLine3;

	public String area;

	public double latitude;

	public double longitude;

	@Required
	public String city;

	@Required
	public State state;

	public Long pinCode;

	public Country country;

	public static Finder<Long, Address> find = new Finder<Long, Address>(Long.class, Address.class);


}
