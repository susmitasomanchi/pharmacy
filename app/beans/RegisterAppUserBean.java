package beans;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import models.RegisterAppUser;
import play.data.validation.Constraints.Email;
import play.data.validation.Constraints.Required;

public class RegisterAppUserBean {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Long id;

	@Required
	public String name;

	@Required
	public String username;

	@Required
	public String password;

	@Email
	public String  email;

	@Required
	public String gender;

	@Required
	public Integer age;


	public RegisterAppUser toEntity(){

		final RegisterAppUser appUser = new RegisterAppUser();

		appUser.id = this.id;

		if(this.name != null) {
			appUser.name= this.name;
		}

		if(this.username != null) {
			appUser.username= this.username;
		}

		if(this.password != null) {
			appUser.password= this.password;
		}

		if(this.email != null) {
			appUser.email= this.email;
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
