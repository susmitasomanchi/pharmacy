package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;


@SuppressWarnings("serial")
@Entity
public class DCRLineItem extends BaseEntity{
	
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Id
	public Long id;
	
	@ManyToMany
	public List<Doctor> doctorList = new ArrayList<Doctor>();
	
	//@OneToMany
	//public List<Samples> sampleList = new ArrayList<Samples>();
	
	@OneToMany(cascade=CascadeType.ALL)
	public List<Product> promotionList = new ArrayList<Product>();
	
	public static Finder<Long, DCRLineItem> find = new Finder<Long, DCRLineItem>(Long.class, DCRLineItem.class);
	

}
