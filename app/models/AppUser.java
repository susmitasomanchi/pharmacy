
/*****

 THIS IS AN AUTO GENERATED CODE
 PLEASE DO NOT MODIFY IT BY HAND

 *****/
package models;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.SqlRow;

import play.data.validation.Constraints.Email;
import play.data.validation.Constraints.Required;
import play.db.ebean.Model;
import play.db.ebean.Model.Finder;
import beans.AppUserBean;


@SuppressWarnings("serial")
@MappedSuperclass
@Entity
public class AppUser extends BaseEntity{

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Long id;


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
