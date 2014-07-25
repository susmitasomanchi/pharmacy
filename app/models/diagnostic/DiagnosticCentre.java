package models.diagnostic;
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
import models.doctor.Doctor;
import play.data.validation.Constraints.Email;
import play.data.validation.Constraints.Required;
import play.db.ebean.Model;
import beans.DiagnosticBean;

@SuppressWarnings("serial")
@Entity
public class DiagnosticCentre extends BaseEntity {



	@GeneratedValue(strategy = GenerationType.AUTO)
	@Id
	public Long id;

	@Required
	public String name;

	public String contactPerson;

	@OneToOne
	public Address address;

	public String mobileNo;

	@Column(columnDefinition="TEXT")
	public String description;
	@Lob
	public byte[] backgroudImage;

	@ManyToMany(cascade = CascadeType.ALL)
	public List<FileEntity> profileImageList = new ArrayList<FileEntity>();

	@OneToMany(cascade = CascadeType.ALL)
	public List<ShowCasedService> showCasedServiceList = new ArrayList<ShowCasedService>();

	@Email
	@Required
	public String emailId;

	public String websiteName;

	public List<Doctor> doctorList = new ArrayList<Doctor>();

	@OneToOne
	public DiagnosticRepresentative diagnosticRepAdmin;

	@OneToMany(cascade=CascadeType.ALL,mappedBy="diagnosticCentre")
	public List<DiagnosticRepresentative> diagnosticRepresentativelist = new ArrayList<DiagnosticRepresentative>();


	@OneToMany(cascade=CascadeType.ALL)
	public List<DiagnosticTest> diagnosticTestList = new ArrayList<DiagnosticTest>();

	//	@OneToMany(cascade=CascadeType.ALL)
	//	 * 	public List<Patient> patientList = new ArrayList<Patient>();


	@OneToMany(cascade=CascadeType.ALL)
	public List<DiagnosticOrder> diagnosticOrderList = new ArrayList<DiagnosticOrder>();

	@Column(columnDefinition="TEXT")
	public String searchIndex;

	@Column(columnDefinition="TEXT")
	public String slugUrl;

	public static Model.Finder<Long, DiagnosticCentre> find = new Finder<Long, DiagnosticCentre>(
			Long.class, DiagnosticCentre.class);


	public DiagnosticBean toBean(){

		final DiagnosticBean diagnosticBean = new DiagnosticBean();
		diagnosticBean.id = this.id;


		if(this.name != null) {
			diagnosticBean.name= this.name;
		}
		if(this.address != null) {
			diagnosticBean.address= this.address;
		}
		if(this.mobileNo != null) {
			diagnosticBean.mobileNo= this.mobileNo;
		}
		if(this.emailId != null) {
			diagnosticBean.emailId= this.emailId;
		}
		if(this.websiteName != null) {
			diagnosticBean.websiteName= this.websiteName;
		}
		return diagnosticBean;
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
		stringBuilder.append(this.slugUrl.toLowerCase());
		this.searchIndex = stringBuilder.toString();
		super.update();
	}

}
