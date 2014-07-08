package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Entity
public class Inventory extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long id;

	@OneToOne
	public Product product;

	public String shelfNo;


	@OneToMany(cascade=CascadeType.ALL)
	public List<Batch> batchList = new ArrayList<Batch>();

	//	@Required
	//	public ProductInventoryStatus productInventoryStatus;


	public Integer productQuantity;

	public String remarks;

	public static Finder<Long, Inventory> find = new Finder<Long, Inventory>(Long.class, Inventory.class);
}
