package models;

import java.util.Date;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import play.data.validation.Constraints.Required;
import play.db.ebean.*;
import play.db.ebean.Model.Finder;


@Entity
public class Batch extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public  Long id;

	@Required
	public BatchStatus batchStatus;

	@OneToOne
	Product product;

	@Required
	public String batchNo;

	@Required
	public Double mrp;

	@Required
	public Date expiryDate;

	@Required
	public Integer quantity;

	public Float tax;

	public Float discount;

	public static Finder<Long, Batch> find = new Finder<Long, Batch>(Long.class, Batch.class);
}
