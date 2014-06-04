
/*****

 THIS IS AN AUTO GENERATED CODE
 PLEASE DO NOT MODIFY IT BY HAND

 *****/
package models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import play.data.validation.Constraints.Email;
import play.db.ebean.Model;


@SuppressWarnings("serial")
@Entity

public class AppUser extends BaseEntity{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long id;

	public String name;

	public String username;


	@Email
	public String email;


	public String password;

	public String mobileno;

	public String gender;


	public Date dob;

	public static Model.Finder<Long, AppUser> find = new Finder<Long, AppUser>(Long.class, AppUser.class);




}
