package models.mr;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import models.BaseEntity;

@SuppressWarnings("serial")
@Entity
public class Designation extends BaseEntity{

	@GeneratedValue(strategy=GenerationType.AUTO)
	@Id
	public Long id;

	@ManyToOne(cascade = CascadeType.ALL)
	public PharmaceuticalCompany pharmaceuticalCompany;

	public String name;

	@Column(columnDefinition="TEXT")
	public String description;

	public static Finder<Long, Designation> find = new Finder<Long, Designation>(Long.class, Designation.class);

}
