package models.bloodBank;

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
import models.AppUser;
import models.BaseEntity;
import models.FileEntity;
import models.PrimaryCity;

@SuppressWarnings("serial")
@Entity
public class BloodBank extends BaseEntity{

	@GeneratedValue(strategy=GenerationType.AUTO)
	@Id
	public Long id;

	public String name;

	public String contactPersonName;

	public String contactNo;

	@OneToOne
	public Address address;

	@OneToOne
	public PrimaryCity primaryCity;

	@OneToOne
	public BloodBankUser bloodBankAdmin;

	@OneToMany(cascade=CascadeType.ALL)
	List<BloodBankUser> bloodBankUserList = new ArrayList<BloodBankUser>();

	@Lob
	public byte[] backgroudImage;

	@Column(columnDefinition="TEXT")
	public String description;

	@Column(columnDefinition="TEXT")
	public String searchIndex;

	@Column(columnDefinition="TEXT")
	public String slugUrl;


	@ManyToMany(cascade = CascadeType.ALL)
	public List<FileEntity> profileImageList = new ArrayList<FileEntity>();

	@ManyToMany(cascade = CascadeType.ALL)
	public List<AppUser> bloodDonorsList = new ArrayList<AppUser>();


	public static Finder<Long, BloodBank> find = new Finder<Long, BloodBank>(Long.class, BloodBank.class);


	@Override
	public void save(){
		final StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(this.name.trim().toLowerCase());
		if(this.address != null){
			if(this.address.addressLine1 != null){
				stringBuilder.append(this.address.addressLine1.trim().toLowerCase());
			}
			if(this.address.locality != null){
				stringBuilder.append(this.address.locality.name.trim().toLowerCase());
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
		if(this.contactNo!= null){
			stringBuilder.append(this.contactNo.trim().toLowerCase());
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
			if(this.address.locality != null){
				stringBuilder.append(this.address.locality.name.trim().toLowerCase());
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
		if(this.contactNo!= null){
			stringBuilder.append(this.contactNo.trim().toLowerCase());
		}
		stringBuilder.append(this.slugUrl.toLowerCase());
		this.searchIndex = stringBuilder.toString();
		super.update();

	}



}
