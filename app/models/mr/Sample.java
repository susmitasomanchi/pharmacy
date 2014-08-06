package models.mr;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import models.BaseEntity;



@SuppressWarnings("serial")
@Entity
public class Sample extends BaseEntity{

	@GeneratedValue(strategy=GenerationType.AUTO)
	@Id
	public Long id;

	@ManyToOne(cascade = CascadeType.ALL)
	public DCRLineItem dcrLineItem;

	@OneToOne
	public PharmaceuticalProduct pharmaceuticalProduct;

	public Integer quantity;

	public static Finder<Long, Sample> find = new Finder<Long, Sample>(Long.class, Sample.class);


}
