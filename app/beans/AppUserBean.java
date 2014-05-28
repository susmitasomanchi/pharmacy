/*
*//*****

THIS IS AN AUTO GENERATED CODE
PLEASE DO NOT MODIFY IT BY HAND

 *****//*
package beans;

import java.io.Serializable;

import models.AppUser;
import models.Role;
import play.data.validation.Constraints.Email;
import play.data.validation.Constraints.Required;

@SuppressWarnings("serial")
public class AppUserBean implements Serializable {

	public Long id;


	@Required
	public String name;




	@Required
	public String username;



	@Required @Email
	public String email;



	@Required
	public String password;




	@Required
	public Role role;

	@Required
	public String gender;

	@Required
	public Integer age;

	public AppUser toEntity(){

		final AppUser appUser = new AppUser();
		appUser.id = this.id;

		if(this.name != null) {
			appUser.name= this.name;
		}



		if(this.username != null) {
			appUser.username= this.username;
		}

		if(this.email != null) {
			appUser.email= this.email;
		}


		if(this.password != null) {
			appUser.password= this.password;
		}



		if(this.role != null) {
			appUser.role= this.role;
		}


		if(this.gender != null) {
			appUser.gender= this.gender;
		}

		if(this.age != null) {
			appUser.age= this.age;
		}
		return appUser;

	}






}

*/