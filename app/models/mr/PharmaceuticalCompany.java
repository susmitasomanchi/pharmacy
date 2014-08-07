package models.mr;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import models.Address;
import models.BaseEntity;

@SuppressWarnings("serial")
@Entity
public class PharmaceuticalCompany extends BaseEntity {

	@GeneratedValue(strategy = GenerationType.AUTO)
	@Id
	public Long id;

	public String name;

	public Address address;

	@OneToOne
	public MedicalRepresentative adminMR;

	@OneToMany(cascade=CascadeType.ALL)
	public List<PharmaceuticalProduct> pharmaceuticalProductList = new ArrayList<PharmaceuticalProduct>();

	@OneToMany(cascade = CascadeType.ALL)
	public List<MedicalRepresentative> mrList = new ArrayList<MedicalRepresentative>();

	public static Finder<Long, PharmaceuticalCompany> find = new Finder<Long, PharmaceuticalCompany>(
			Long.class, PharmaceuticalCompany.class);

}
