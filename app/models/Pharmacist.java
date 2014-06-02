package models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Pharmacist extends BaseEntity{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long id;

	public String category;

	public static Finder<Long, Pharmacist> find = new Finder<>(Long.class, Pharmacist.class);

}
