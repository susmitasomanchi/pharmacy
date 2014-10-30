package models.doctor;

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
import models.Locality;
import models.PrimaryCity;
import models.clinic.ClinicUser;
import play.Logger;
import play.db.ebean.Model;

@SuppressWarnings("serial")
@Entity
public class Clinic extends BaseEntity{

	@GeneratedValue(strategy=GenerationType.AUTO)
	@Id
	public Long id;

	public String name;

	public String contactPersonName;

	public String contactNo;

	@OneToOne
	public Address address;

	@OneToOne
	public ClinicUser clinicAdminstrator;

	@OneToOne
	public PrimaryCity primaryCity;

	@Lob
	public byte[] backgroudImage;

	@Column(columnDefinition="TEXT")
	public String description;

	@Column(columnDefinition="TEXT")
	public String searchIndex;

	@Column(columnDefinition="TEXT")
	public String slugUrl;

	/*@OneToOne
	public Locality locality;*/

	@OneToMany(cascade=CascadeType.ALL)
	public List<ClinicUser> clinicUserList = new ArrayList<ClinicUser>();

	@ManyToMany(cascade = CascadeType.ALL)
	public List<FileEntity> profileImageList = new ArrayList<FileEntity>();

	public static Model.Finder<Long, Clinic> find = new Finder<Long, Clinic>(Long.class, Clinic.class);

	public List<DoctorClinicInfo> getDoctorsOfClinic(){
		return DoctorClinicInfo.find.where().eq("clinic", this).findList();
	}
	@Override
	public boolean equals(final Object arg0) {
		if(!this.name.equals(((Clinic)arg0).name)){
			return this.name.equals(((Clinic)arg0).name);
		}
		if(!this.contactPersonName.equals(((Clinic)arg0).contactPersonName)){
			return this.contactPersonName.equals(((Clinic)arg0).contactPersonName);
		}

		if(!this.contactNo.equals(((Clinic)arg0).contactNo)){
			return this.contactNo.equals(((Clinic)arg0).contactNo);
		}

		return super.equals(arg0);
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
		if(this.contactNo!= null){
			stringBuilder.append(this.contactNo.trim().toLowerCase());
		}
		if(this.slugUrl != null){
			stringBuilder.append(this.slugUrl.toLowerCase());
		}
		this.searchIndex = stringBuilder.toString();
		super.save();
	}

	@Override
	public void update() {
		final StringBuilder stringBuilder = new StringBuilder();
		if(this.name != null){
			stringBuilder.append(this.name.trim().toLowerCase());
		}

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
		if(this.contactNo!= null){
			stringBuilder.append(this.contactNo.trim().toLowerCase());
		}
		if(this.slugUrl != null){
			stringBuilder.append(this.slugUrl.toLowerCase());
		}
		this.searchIndex = stringBuilder.toString();
		super.update();
		Logger.info(this.slugUrl);

	}


}
