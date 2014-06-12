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

@Entity
public class Pharmacy extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public  Long id;

	public String name;

	public String address;

	public String contactNo;

	public String testField;

	@OneToOne
	public Pharmacist adminPharmacist;

	@ManyToOne(cascade=CascadeType.ALL)
	public List<Pharmacist> pharmacistList = new ArrayList<Pharmacist>();

	@ManyToOne(cascade=CascadeType.ALL)
	public List<Inventory> inventoryList = new ArrayList<Inventory>();

	public static Finder<Long, Pharmacy> find = new Finder<Long, Pharmacy>(Long.class, Pharmacy.class);

}
