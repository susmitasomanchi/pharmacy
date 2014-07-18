package models;




import javax.persistence.Entity;
import javax.persistence.Id;



import play.db.ebean.Model;


@Entity
public class LanguageAppUser extends BaseEntity {

	@Id
	public Long id;
	
	public Language language;
	
	public static Model.Finder<Long,LanguageAppUser> find = new Finder<Long, LanguageAppUser>(Long.class, LanguageAppUser.class);
	

}
