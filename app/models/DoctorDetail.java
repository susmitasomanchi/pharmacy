package models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import play.data.validation.Constraints.Required;

@SuppressWarnings("serial")
@Entity
public class DoctorDetail extends BaseEntity{
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public  Long id;
	
	
	
	
	
	
	

	//Languages
	
	
	@OneToMany(cascade=CascadeType.ALL)
	public List<LanguageAppUser> languageAppUsers=new ArrayList<LanguageAppUser>();
	
	
	
	

}
