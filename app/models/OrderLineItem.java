package models;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;


@SuppressWarnings("serial")
@Entity
public class OrderLineItem extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long id;

	@OneToOne
	public Product product;

	public Double quantity;

	public String batchNo;

	public Date expiryDate;

	public Double price;

	public Double subTotal;



	public static Finder<Long, OrderLineItem> find = new Finder<Long, OrderLineItem>(Long.class, OrderLineItem.class);
}
