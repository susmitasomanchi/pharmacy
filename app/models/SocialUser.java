package models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;

import play.data.validation.Constraints.Email;
import play.db.ebean.Model;
import play.db.ebean.Model.Finder;

@SuppressWarnings("serial")
@Entity
public class SocialUser extends BaseEntity{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long id;

	@Lob
	public byte[] image;

	public String name;

	public String username;

	@Email
	public String email;

	public Sex sex;

	public Date dob;

	public BlogCommentatorType firstLogInVia;

	public static Model.Finder<Long, SocialUser> find = new Finder<Long, SocialUser>(Long.class, SocialUser.class);
}
