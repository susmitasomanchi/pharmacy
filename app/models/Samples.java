/*package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;



@SuppressWarnings("serial")
@Entity
public class Samples extends BaseEntity{
	
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Id
	public Long id;
	
	@OneToMany
	public List<Product> productList = new ArrayList<Product>();
	
	public int qty;
	
	public static Finder<Long, Samples> find = new Finder<Long, Samples>(Long.class, Samples.class);
	

}
*/