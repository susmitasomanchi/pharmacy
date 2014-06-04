package models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

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

	public int noOfDoctorsVisit;

	public static Finder<Long, MedicalRepresentative> find = new Finder<>(Long.class, MedicalRepresentative.class);

	@Override
	public void save(){
		super.save();
	}
}
