package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import play.db.ebean.Model;
import play.db.ebean.Model.Finder;

@Entity
public class LanguageAppUser extends BaseEntity {

	@Id
	public Long id;
	
	public Language language;
	
	public static Model.Finder<Long,LanguageAppUser> find = new Finder<Long, LanguageAppUser>(Long.class, LanguageAppUser.class);
	

}
