package models.mr;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import models.BaseEntity;

@SuppressWarnings("serial")
@Entity
public class Designation extends BaseEntity{
	
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Id
	public Long id;
	
	public String name;
	
	@Column(columnDefinition="TEXT")
	public String description;
	
	/*@OneToOne
	public MedicalRepresentative mr;*/
	
	//public String test;
	
	public static Finder<Long, Designation> find = new Finder<Long, Designation>(
			Long.class, Designation.class);

}
