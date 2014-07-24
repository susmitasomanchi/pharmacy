package models.mr;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import models.AppUser;
import models.BaseEntity;
import models.doctor.Doctor;
import beans.MedicalRepresentativeBean;

@SuppressWarnings("serial")
@Entity
public class MedicalRepresentative extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long id;

	@OneToOne
	public AppUser appUser;

	public String regionAlloted;

	public String companyName;

	public String designation;

	public boolean isActive = true;

	public String status;

	@OneToOne
	public MedicalRepresentative manager;

	@ManyToOne(cascade = CascadeType.ALL)
	public PharmaceuticalCompany pharmaceuticalCompany = new PharmaceuticalCompany();

	@ManyToMany(cascade = CascadeType.ALL)
	public List<Doctor> doctorList = new ArrayList<Doctor>();

	@OneToMany(mappedBy="submitter",cascade = CascadeType.ALL)
	public List<DailyCallReport> dcrList = new ArrayList<DailyCallReport>();

	@ManyToMany(cascade = CascadeType.ALL)
	public List<HeadQuarter> headQuarterList = new ArrayList<HeadQuarter>();

	public List<MedicalRepresentative> getSubordinates() {
		return MedicalRepresentative.find.where().eq("manager_id", this.id).findList();
	}

	/*public List<DailyCallReport> getSubordinatesDCRList(){
		return DailyCallReport.find.where().in("submitter", this.getSubordinates()).ne("dcrStatus",DCRStatus.DRAFT).orderBy("forDate DESC").findList();
	}*/

	public MedicalRepresentativeBean toBean() {
		final MedicalRepresentativeBean bean = new MedicalRepresentativeBean();

		bean.id = this.id;

		bean.appid = this.appUser.id;

		if (this.appUser != null) {
			bean.name = this.appUser.name;
		}
		if (this.appUser != null) {
			bean.username = this.appUser.username;
		}

		if (this.appUser != null) {
			bean.email = this.appUser.email;
		}

		if (this.appUser != null) {
			bean.password = this.appUser.password;
		}

		if (this.regionAlloted != null) {
			bean.regionAlloted = this.regionAlloted;
		}

		if (this.designation != null) {
			bean.designation = this.designation;
		}

		if (this.status != null) {
			bean.status = this.status;
		}

		if (this.companyName != null) {
			bean.companyName = this.companyName;
		}

		return bean;
	}

	public static Finder<Long, MedicalRepresentative> find = new Finder<Long, MedicalRepresentative>(
			Long.class, MedicalRepresentative.class);

	//public Map<State , List<HeadQuarter>> headQuarterMap= new LinkedHashMap<State , List<HeadQuarter>>();



}
