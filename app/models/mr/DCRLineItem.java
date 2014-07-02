package models.mr;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import models.BaseEntity;
import models.Doctor;
import models.Product;


@SuppressWarnings("serial")
@Entity
public class DCRLineItem extends BaseEntity{

	@GeneratedValue(strategy=GenerationType.AUTO)
	@Id
	public Long id;

	@OneToOne
	public Doctor doctor;

	@OneToMany(cascade=CascadeType.ALL)
	public List<Sample> sampleList = new ArrayList<Sample>();

	@ManyToMany(cascade=CascadeType.ALL)
	public List<Product> promotionList = new ArrayList<Product>();

	public Integer pob;

	public String remarks;

	public static Finder<Long, DCRLineItem> find = new Finder<Long, DCRLineItem>(Long.class, DCRLineItem.class);


}
