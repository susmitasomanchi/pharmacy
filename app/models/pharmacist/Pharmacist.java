package models.pharmacist;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import models.AppUser;
import models.BaseEntity;

@SuppressWarnings("serial")
@Entity
public class Pharmacist extends BaseEntity{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long id;

	@OneToOne
	public AppUser appUser;

	@ManyToOne(cascade=CascadeType.ALL)
	public Pharmacy pharmacy;

	public String category;

	public static Finder<Long, Pharmacist> find = new Finder<Long, Pharmacist>(Long.class, Pharmacist.class);

}
