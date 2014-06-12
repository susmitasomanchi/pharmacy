package models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;


@SuppressWarnings("serial")
@Entity
public class AdminMR extends BaseEntity{
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Long id;

	@OneToOne
	public AppUser appUser;
	
	public String pharmacutical_company_name;
	
	public static Finder<Long, AdminMR> find = new Finder<Long, AdminMR>(Long.class, AdminMR.class);



}
