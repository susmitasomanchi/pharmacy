package models.clinic;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import models.AppUser;
import models.BaseEntity;
import models.doctor.Clinic;
import models.doctor.Doctor;

@SuppressWarnings("serial")
@Entity
public class ClinicUser extends BaseEntity{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long id;

	@OneToOne
	public AppUser appUser;

	@ManyToOne(cascade=CascadeType.ALL)
	public Clinic clinic;

	@ManyToMany(cascade = CascadeType.ALL)
	public List<Doctor> doctorsList = new ArrayList<Doctor>();

	public static Finder<Long, ClinicUser> find = new Finder<Long, ClinicUser>(Long.class, ClinicUser.class);

}
