package models.clinic;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import models.AppUser;
import models.BaseEntity;
import models.doctor.Clinic;
import models.pharmacist.Pharmacist;

@SuppressWarnings("serial")
@Entity
public class ClinicAdministrator extends BaseEntity{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long id;

	@OneToOne
	public AppUser appUser;

	@ManyToOne(cascade=CascadeType.ALL)
	public Clinic clinic;

	public static Finder<Long, ClinicAdministrator> find = new Finder<Long, ClinicAdministrator>(Long.class, ClinicAdministrator.class);

}
