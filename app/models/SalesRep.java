package models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@SuppressWarnings("serial")
@Entity
public class SalesRep extends AppUser{
	
	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
	public Long id;
    
    public String regionAlloted;
    
    public String companyName;
    
    public String typesOfMedecine;
    
    public int noOfDoctorsVisit;
    
   
    
}
