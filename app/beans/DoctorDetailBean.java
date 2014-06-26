package beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import play.data.validation.Constraints.Required;
import models.DoctorEducation;
import models.Language;
import models.LanguageAppUser;

@SuppressWarnings("serial")
public class DoctorDetailBean implements Serializable{

	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Long id;


	public String collegeName;
	
	
	public String degree;
	

	public Integer fromYear;
	
	
	public Integer toYear;
	
	public List<Language> languageAppUser=new ArrayList<Language>();
	
	public List<LanguageAppUser> toLanguageAppUser(){
	
		final List<LanguageAppUser> languageAppUsers=new ArrayList<LanguageAppUser>();
		for (final Language language : this.languageAppUser) {
			final LanguageAppUser languageAppUser=new LanguageAppUser();
			languageAppUser.language=language;
			languageAppUsers.add(languageAppUser);
		}
		return languageAppUsers;
	}

	
}