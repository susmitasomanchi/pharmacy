package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import play.data.validation.Constraints.Required;
import play.db.ebean.Model;
import play.db.ebean.Model.Finder;

@Entity
public class Pharmacy extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public  Long id;

	public String name;

	public String address;

	public String contactNo;

	@OneToOne
	Pharmacist admminPharmacist;

	@ManyToOne(cascade=CascadeType.ALL)
	List<Pharmacist> pharmacistList = new ArrayList<Pharmacist>();

	@ManyToOne(cascade=CascadeType.ALL)
	List<Inventory> inventoryList = new ArrayList<Inventory>();

	public static Finder<Long, Pharmacy> find = new Finder<Long, Pharmacy>(Long.class, Pharmacy.class);

}
