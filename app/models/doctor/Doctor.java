package models.doctor;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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

import models.AppUser;
import models.BaseEntity;
import models.diagnostic.DiagnosticCentre;
import models.pharmacist.Pharmacy;
import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

@SuppressWarnings("serial")
@Entity
public class Doctor extends BaseEntity{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public  Long id;

	@OneToOne
	public AppUser appUser;

	@OneToMany(cascade = CascadeType.ALL)
	public List<DoctorClinicInfo> doctorClinicInfoList = new ArrayList<DoctorClinicInfo>();

	@Required
	public String registrationNumber;

	@Required
	public String specialization;

	@Required
	public String position;

	@Required
	public String degree;

	@Column(columnDefinition="TEXT")
	public String description;

	@Lob
	public byte[] backgroundImage;

	@Lob
	public byte[] profileImage;

	//education
	@OneToMany(cascade=CascadeType.ALL)
	public List<DoctorEducation> doctorEducationList = new ArrayList<DoctorEducation>();

	//experience
	@OneToMany(cascade=CascadeType.ALL)
	public List<DoctorExperience> doctorExperienceList = new ArrayList<DoctorExperience>();

	//publications
	@OneToMany(cascade=CascadeType.ALL)
	public List<DoctorPublication> doctorPublicationList = new ArrayList<DoctorPublication>();

	//awards
	@OneToMany(cascade=CascadeType.ALL)
	public List<DoctorAward> doctorAwardList = new ArrayList<DoctorAward>();

	//language
	@ManyToMany(cascade=CascadeType.ALL)
	public List<DoctorLanguage> doctorLanguageList = new ArrayList<DoctorLanguage>();

	//socialwork
	@OneToMany(cascade=CascadeType.ALL)
	public List<DoctorSocialWork> doctorSocialWorkList = new ArrayList<DoctorSocialWork>();

	@OneToMany(cascade=CascadeType.ALL)
	public List<DoctorEducation> doctorEducation = new ArrayList<DoctorEducation>();

	@OneToMany(cascade=CascadeType.ALL)
	public List<DoctorProduct> myProductList = new ArrayList<DoctorProduct>();

	@OneToMany(cascade=CascadeType.ALL)
	public List<DoctorDiagnosticTest> myDiagnosticTestList = new ArrayList<DoctorDiagnosticTest>();

	@ManyToMany(cascade=CascadeType.ALL)
	public List<Pharmacy> pharmacyList=new ArrayList<Pharmacy>();
	
	@ManyToMany(cascade=CascadeType.ALL)
	public List<DiagnosticCentre> diagnosticCentreList=new ArrayList<DiagnosticCentre>();

	/** Using it to capture starting year of experience.
	 * 	So, Doctor's experience = currentYear - this.experience
	 */
	public Integer experience;


	@Column(columnDefinition="TEXT")
	public String searchIndex;

	@Column(columnDefinition="TEXT")
	public String slugUrl;

	public static Model.Finder<Long,Doctor> find = new Finder<Long, Doctor>(Long.class, Doctor.class);

	public List<DoctorExperience> getExperienceListInOrder(){
		return DoctorExperience.find.where().eq("doctor_id", this.id).orderBy("workedFrom DESC").findList();
	}

	public List<DoctorEducation> getEducationListInOrder(){
		return DoctorEducation.find.where().eq("doctor_id", this.id).orderBy("fromYear DESC").findList();
	}

	public List<DoctorSocialWork> getDoctorSocialWorkList() {
		return this.doctorSocialWorkList;
	}

	public List<DoctorClinicInfo> getActiveClinic(){
		return DoctorClinicInfo.find.where().eq("doctor", this).eq("active", true).findList();
	}

	@OneToMany(cascade = CascadeType.ALL)
	public List<SigCode> sigCodeList = new ArrayList<SigCode>();

	@Override
	public void save(){
		final StringBuilder stringBuilder = new StringBuilder();
		for (final DoctorClinicInfo clinicInfo : this.doctorClinicInfoList) {
			if(clinicInfo.clinic != null){
				stringBuilder.append(clinicInfo.clinic.name.toLowerCase());
			}
		}
		if(this.appUser.name != null){
			stringBuilder.append(this.appUser.name.toLowerCase());
		}
		if(this.specialization != null){
			stringBuilder.append(this.specialization.toLowerCase());
		}
		if(this.degree != null){
			stringBuilder.append(this.degree.toLowerCase());
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
		for (final DoctorClinicInfo clinicInfo : this.doctorClinicInfoList) {
			if(clinicInfo.clinic != null){
				stringBuilder.append(clinicInfo.clinic.name.toLowerCase());
			}
		}
		if(this.appUser.name != null){
			stringBuilder.append(this.appUser.name.toLowerCase());
		}
		if(this.specialization != null){
			stringBuilder.append(this.specialization.toLowerCase());
		}
		if(this.degree != null){
			stringBuilder.append(this.degree.toLowerCase());
		}
		if(this.slugUrl != null){
			stringBuilder.append(this.slugUrl.toLowerCase());
		}
		this.searchIndex = stringBuilder.toString();
		super.update();
	}


	public Integer getYearsOfExperience(){
		final Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		final int currentYear = cal.get(Calendar.YEAR);
		if(this.experience != null){
			if(currentYear - this.experience == 0){
				return 1;
			}
			return (currentYear - this.experience);
		}
		else{
			return 1;
		}
	}


}
