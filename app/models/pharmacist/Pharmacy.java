package models.pharmacist;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import models.Address;
import models.BaseEntity;
import models.FileEntity;
import beans.PharmacyBean;

@SuppressWarnings("serial")
@Entity
public class Pharmacy extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public  Long id;

	public String name;

	@OneToOne
	public Address address;

	public String contactPerson;

	public String contactNo;

	@Column(columnDefinition="TEXT")
	public String description;

	@OneToOne
	public Pharmacist adminPharmacist;

	@Lob
	public byte[] backgroundImage;

	@OneToMany(cascade=CascadeType.ALL)
	public List<FileEntity> profileImageList = new ArrayList<FileEntity>();

	@OneToMany(cascade=CascadeType.ALL)
	public List<PharmacyProduct> pharmacyProductList = new ArrayList<PharmacyProduct>();

	@OneToMany(cascade=CascadeType.ALL)
	public List<ShowCasedProduct> showCaseProductList = new ArrayList<ShowCasedProduct>();

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

		if(this.contactPerson != null) {
			pharmacyBean.contactPerson= this.contactPerson;
		}

		if(this.description != null) {
			pharmacyBean.description= this.description;
		}

		return pharmacyBean;
	}


}
