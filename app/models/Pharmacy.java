package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class Pharmacy {
	public String name;
	public String Address;


	@ManyToOne
	List<Pharmacist> farmacistlist=new ArrayList<Pharmacist>();
}
