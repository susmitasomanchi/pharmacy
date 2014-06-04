package models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import play.data.validation.Constraints.Email;
import play.data.validation.Constraints.Required;
import play.db.ebean.Model;
import beans.RegisterAppUserBean;

@SuppressWarnings("serial")
@Entity
public class RegisterAppUser extends BaseEntity{

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
	public String email;

	@Required
	public String gender;

	@Required
	public Integer age;

	//@OneToOne(mappedBy="regAppUsr")
	//public Doctor doctor;

	public static Model.Finder<Long, RegisterAppUser> find = new Finder<>(Long.class, RegisterAppUser.class);

	public RegisterAppUserBean toBean() {

		final RegisterAppUserBean userBean = new RegisterAppUserBean();
		userBean.id = this.id;

		if (this.name != null) {
			userBean.name = this.name;
		}

		if (this.username != null) {
			userBean.username = this.username;
		}

		if (this.password != null) {
			userBean.password = this.password;
		}

		if (this.email!= null) {
			userBean.email= this.email;
		}

		if (this.gender != null) {
			userBean.gender = this.gender;
		}

		if (this.age != null) {
			userBean.age = this.age;
		}


		return userBean;
	}

}
