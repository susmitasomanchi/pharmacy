package models.pharmacist;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import models.Address;
import models.BaseEntity;
import models.Product;
import beans.PharmacyBean;

@SuppressWarnings("serial")
@Entity
public class Pharmacy extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public  Long id;

	public String name;

	@Lob
	public byte[] profileImage;

	public List<byte[]> backgroundImageList = new ArrayList<byte[]>();

	@OneToOne
	public Address address	= new Address();

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




	public PharmacyBean toBean(){

		final PharmacyBean pharmacyBean = new PharmacyBean();
		pharmacyBean.id = this.id;


		if(this.name != null) {
			pharmacyBean.name= this.name;
		}
		if(this.address != null) {
			pharmacyBean.address= this.address;
		}
		if(this.contactNo != null) {
			pharmacyBean.contactNo= this.contactNo;
		}
		if(this.testField != null) {
			pharmacyBean.testField= this.testField;
		}

		return pharmacyBean;
	}


}
