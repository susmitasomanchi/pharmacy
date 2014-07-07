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
import beans.MedicalRepresentativeBean;
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
	
	public String noOfDoctorsVisit;
	
	public String designation;
		
	@OneToOne
	public MedicalRepresentative manager;

	@ManyToOne
	public PharmaceuticalCompany pharmaceuticalCompany = new PharmaceuticalCompany();

	@ManyToMany(cascade=CascadeType.ALL)
	public List<Doctor> doctorList = new ArrayList<Doctor>();

	@OneToMany(cascade=CascadeType.ALL)
	public List<DailyCallReport> dcrList = new ArrayList<DailyCallReport>();

	public MedicalRepresentativeBean toBean(){
		MedicalRepresentativeBean bean=new MedicalRepresentativeBean();
		
			bean.id = this.id;
		
			bean.appid=this.appUser.id;
			
		if(this.appUser != null){
			bean.name=this.appUser.name;
		}
		if(this.appUser != null){
			bean.username=this.appUser.username;
		}
		
		if(this.appUser != null){
			bean.email=this.appUser.email;			
		}
		
		if(this.appUser != null){
			bean.password=this.appUser.password;			
		}		
		
		if(this.regionAlloted!= null){
			bean.regionAlloted=this.regionAlloted;
		}
		
		if(this.designation!= null){
			bean.designation=this.designation;
		}
		
		if(this.age == 0){	
			bean.age=this.age;
		}
		if(this.sex != null){	
			bean.sex=this.sex;
		}
		
		if(this.companyName!= null){
			bean.companyName=this.companyName;
		}
		if(this.typesOfMedecine!= null){
			bean.typesOfMedecine=this.typesOfMedecine;
		}
		if(this.noOfDoctorsVisit!= null){
			bean.noOfDoctorsVisit=this.noOfDoctorsVisit;
		}
		return bean;
	}

	

	public static Finder<Long, MedicalRepresentative> find = new Finder<Long, MedicalRepresentative>(Long.class, MedicalRepresentative.class);

}
