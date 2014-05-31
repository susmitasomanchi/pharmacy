
/*****

 THIS IS AN AUTO GENERATED CODE
 PLEASE DO NOT MODIFY IT BY HAND

 *****/
package models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import play.data.validation.Constraints.Email;
import play.db.ebean.Model;


@SuppressWarnings("serial")
@Entity

public class AppUser extends BaseEntity{

	@Id
	public Long id;

	@OneToOne
	public Pharmacist pharmacist;

	public String name;



	public String username;


	@Email
	public String email;


	public String password;



	public Role role;


	public String gender;


	public Integer age;



	public static Model.Finder<Long, AppUser> find = new Finder<>(Long.class, AppUser.class);



}
