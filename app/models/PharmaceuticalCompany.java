package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@SuppressWarnings("serial")
@Entity
public class PharmaceuticalCompany extends BaseEntity {
	@Id
	public Long id;
	public String name;
	public Address address;

	@OneToOne
	public MedicalRepresentative adminMR;

	@ManyToOne(cascade=CascadeType.ALL)
	public List<MedicalRepresentative> mrList = new ArrayList<MedicalRepresentative>();

	

}
