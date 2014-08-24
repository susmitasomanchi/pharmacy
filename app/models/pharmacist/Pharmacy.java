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
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import models.Address;
import models.BaseEntity;
import models.FileEntity;
import models.PrimaryCity;
import models.doctor.Doctor;
import models.patient.Patient;
import play.db.ebean.Model;
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

	public String contactNumber;

	@Column(columnDefinition="TEXT")
	public String description;

	@OneToOne
	public Pharmacist adminPharmacist;

	@Lob
	public byte[] backgroundImage;

	@Column(columnDefinition="TEXT")
	public String searchIndex;

	@Column(columnDefinition="TEXT")
	public String slugUrl;

	@ManyToMany(cascade=CascadeType.ALL)
	public List<FileEntity> profileImageList = new ArrayList<FileEntity>();

	@OneToMany(cascade=CascadeType.ALL)
	public List<PharmacyInfo> pharmacyInfoList = new ArrayList<PharmacyInfo>();

	@OneToMany(cascade=CascadeType.ALL)
	public List<PharmacyProduct> pharmacyProductList = new ArrayList<PharmacyProduct>();

	@OneToMany(cascade=CascadeType.ALL)
	public List<ShowCasedProduct> showCaseProductList = new ArrayList<ShowCasedProduct>();

	@OneToMany(cascade=CascadeType.ALL)
	public List<Pharmacist> pharmacistList = new ArrayList<Pharmacist>();

	/*@OneToMany(cascade=CascadeType.ALL)
	public List<PharmacyOrder> pharmacyOrderList = new ArrayList<PharmacyOrder>();
	 */
	@OneToMany(cascade=CascadeType.ALL)
	public List<Inventory> inventoryList = new ArrayList<Inventory>();

	@OneToOne
	PrimaryCity primaryCity;

	public static Model.Finder<Long, Pharmacy> find = new Model.Finder<Long, Pharmacy>(Long.class, Pharmacy.class);

	public PharmacyBean toBean(){

		final PharmacyBean pharmacyBean = new PharmacyBean();
		pharmacyBean.id = this.id;


		if(this.name != null) {
			pharmacyBean.name= this.name;
		}
		if(this.address != null) {
			pharmacyBean.address= this.address;
		}
		if(this.contactNumber != null) {
			pharmacyBean.contactNumber= this.contactNumber;
		}

		if(this.contactPerson != null) {
			pharmacyBean.contactPerson= this.contactPerson;
		}

		if(this.description != null) {
			pharmacyBean.description= this.description;
		}

		return pharmacyBean;
	}

	@Override
	public void save(){
		final StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(this.name.trim().toLowerCase());
		if(this.address != null){
			if(this.address.addressLine1 != null){
				stringBuilder.append(this.address.addressLine1.trim().toLowerCase());
			}
			if(this.address.area != null){
				stringBuilder.append(this.address.area.trim().toLowerCase());
			}
			if(this.address.city != null){
				stringBuilder.append(this.address.city.trim().toLowerCase());
			}
			if(this.address.pinCode != null){
				stringBuilder.append(this.address.pinCode.trim().toLowerCase());
			}
			if(this.address.fetchedPinCode != null){
				stringBuilder.append(this.address.fetchedPinCode.trim().toLowerCase());
			}
		}
		if(this.contactNumber!= null){
			stringBuilder.append(this.contactNumber.trim().toLowerCase());
		}
		stringBuilder.append(this.slugUrl.toLowerCase());
		this.searchIndex = stringBuilder.toString();
		super.save();
	}

	@Override
	public void update() {
		final StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(this.name.trim().toLowerCase());
		if(this.address != null){
			if(this.address.addressLine1 != null){
				stringBuilder.append(this.address.addressLine1.trim().toLowerCase());
			}
			if(this.address.area != null){
				stringBuilder.append(this.address.area.trim().toLowerCase());
			}
			if(this.address.city != null){
				stringBuilder.append(this.address.city.trim().toLowerCase());
			}
			if(this.address.pinCode != null){
				stringBuilder.append(this.address.pinCode.trim().toLowerCase());
			}
			if(this.address.fetchedPinCode != null){
				stringBuilder.append(this.address.fetchedPinCode.trim().toLowerCase());
			}
		}
		if(this.contactNumber!= null){
			stringBuilder.append(this.contactNumber.trim().toLowerCase());
		}
		stringBuilder.append(this.slugUrl.toLowerCase());
		this.searchIndex = stringBuilder.toString();
		super.update();

	}

	public List<Doctor> getDoctorsAddedAsFavoritePharmacies(){
		final List<Doctor> doctorList = new ArrayList<Doctor>();
		for (final Doctor doctor : Doctor.find.all()) {
			if(doctor.pharmacyList.contains(this)){
				doctorList.add(doctor);
			}
		}
		return doctorList;
	}
	public List<Patient> getPatientsAddedAsFavoritePharmacies(){
		final List<Patient> patientList = new ArrayList<Patient>();
		for (final Patient patient : Patient.find.all()) {
			if(patient.pharmacyList.contains(this)){
				patientList.add(patient);
			}
		}
		return patientList;
	}

}
