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
public class Inventory extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public  Long id;

	@OneToOne
	Product product;


	@Required
	public String batchNo;

	@Required
	public Double mrp;

	@Required
	public Date expiryDate;

	@Required
	public Long quantity;

	public static Finder<Long, Inventory> find = new Finder<Long, Inventory>(Long.class, Inventory.class);
}
