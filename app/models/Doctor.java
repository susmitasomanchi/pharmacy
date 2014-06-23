package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

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
	public String specialization;

	@Required
	public String position;


	@Required
	public String degree;

	@ManyToOne
	public List<DoctorEducation> doctorEducation = new ArrayList<DoctorEducation>();


	//government or private
	public String doctorType;

	public String experience;

	public String homeFacility;

	public Integer fees;

	public String clinicAddress;

	public String hospitalAddress;

	@Required
	public String timings;

	public String categoryOfDoctor;						// homeopathic or ayurvedic or etc.




	@ManyToOne(cascade=CascadeType.ALL)
	public List<Appointment> appointmentList=new ArrayList<Appointment>();

	public static Model.Finder<Long,Doctor> find = new Finder<Long, Doctor>(Long.class, Doctor.class);

}
