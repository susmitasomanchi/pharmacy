package models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import play.data.validation.Constraints.Required;

@SuppressWarnings("serial")
@Entity
public class DoctorDetail extends BaseEntity{
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public  Long id;
	
	
	
	
	
	public Long a;
	
	
	//publication/ articles
	
	public String articleOn;
	
	public String publishedOn;
	
	public String commentForArticle;
	
	
	
	//Languages
	
	@Required
	public String language;
	
	
	
	//Social Work
	
	public String socialWorkTittle;
	
	public String CommentSocialWork;
	
	

}
