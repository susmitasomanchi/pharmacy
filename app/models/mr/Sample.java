package models.mr;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import models.BaseEntity;
import models.MasterProduct;



@SuppressWarnings("serial")
@Entity
public class Sample extends BaseEntity{

	@GeneratedValue(strategy=GenerationType.AUTO)
	@Id
	public Long id;

	@OneToOne
	public MasterProduct product;

	public Integer quantity;

	public static Finder<Long, Sample> find = new Finder<Long, Sample>(Long.class, Sample.class);


}
