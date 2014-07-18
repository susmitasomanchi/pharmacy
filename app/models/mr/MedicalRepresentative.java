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

@SuppressWarnings("serial")
@Entity
public class MedicalRepresentative extends BaseEntity{

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Long id;

	@OneToOne
	public AppUser appUser;

	public String regionAlloted;

	public String companyName;

	public String typesOfMedecine;

	public Long mrAdminId;

	@ManyToOne
	public PharmaceuticalCompany pharmaceuticalCompany = new PharmaceuticalCompany();

	@ManyToMany(cascade=CascadeType.ALL)
	public List<Doctor> doctorList = new ArrayList<Doctor>();

	@OneToMany(cascade=CascadeType.ALL)
	public List<DailyCallReport> dcrList = new ArrayList<DailyCallReport>();

	@OneToMany(cascade=CascadeType.ALL)
	public List<HeadQuarter> headQuarterList = new ArrayList<HeadQuarter>();

	public static Finder<Long, MedicalRepresentative> find = new Finder<Long, MedicalRepresentative>(Long.class, MedicalRepresentative.class);

}
