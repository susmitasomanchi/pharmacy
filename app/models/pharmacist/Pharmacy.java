package models.pharmacist;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import models.BaseEntity;
import models.Product;

@SuppressWarnings("serial")
@Entity
public class Pharmacy extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public  Long id;

	public String name;

	public String address;

	public String contactNo;

	public String testField;

	//@OneToOne
	//public PharmacyProductInfo pharmacyProductInfo;

	@OneToMany(cascade=CascadeType.ALL)
	public List<Product> productList = new ArrayList<Product>();


	@OneToMany(cascade=CascadeType.ALL)
	public List<Pharmacist> pharmacistList = new ArrayList<Pharmacist>();

	@OneToMany(cascade=CascadeType.ALL)
	public List<Inventory> inventoryList = new ArrayList<Inventory>();



	public static Finder<Long, Pharmacy> find = new Finder<Long, Pharmacy>(Long.class, Pharmacy.class);

}
