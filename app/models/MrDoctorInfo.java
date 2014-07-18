/*package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;


@SuppressWarnings("serial")
@Entity
public class MrDoctorInfo extends BaseEntity{
	
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Id
	public Long id;
	
	@OneToOne
	public MedicalRepresentative mr;
	
	@ManyToMany(cascade=CascadeType.ALL)
	public List<Doctor> doctorList = new ArrayList<Doctor>();
	
	public static Finder<Long, MrDoctorInfo> find = new Finder<Long, MrDoctorInfo>(Long.class, MrDoctorInfo.class);

}
*/