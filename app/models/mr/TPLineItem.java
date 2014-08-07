package models.mr;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import models.BaseEntity;
import models.doctor.Doctor;

@SuppressWarnings("serial")
@Entity
public class TPLineItem extends BaseEntity{

	@GeneratedValue(strategy = GenerationType.AUTO)
	@Id
	public Long id;

	public Date date;

	public DayStatus dayStatus;

	@ManyToMany(cascade=CascadeType.ALL)
	public List<Doctor> doctorList = new ArrayList<Doctor>();

	@OneToMany(cascade=CascadeType.ALL)
	public List<Sample> sampleList = new ArrayList<Sample>();

	@ManyToMany(cascade=CascadeType.ALL)
	public List<PharmaceuticalProduct> promotionList = new ArrayList<PharmaceuticalProduct>();

	public Integer pob;

	public String remarks;

	public boolean isAddedtoTourplan = false;

	public static Finder<Long, TPLineItem> find = new Finder<Long,TPLineItem>(Long.class,TPLineItem.class);


}
