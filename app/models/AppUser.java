/*****

 THIS IS AN AUTO GENERATED CODE
 PLEASE DO NOT MODIFY IT BY HAND

 *****/
package models;

import javax.persistence.Entity;
import javax.persistence.Id;

import play.data.validation.Constraints.Email;
import play.db.ebean.Model;


@SuppressWarnings("serial")
@Entity

public class AppUser extends BaseEntity{

	@Id
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

	/*public static Map<String, String> options() {
		final LinkedHashMap<String, String> vals = new LinkedHashMap<String, String>();
		final AppUser user = LoginController.getLoggedInUser();
		if(user.role.equals(Role.USER) || user.role.equals(Role.COORDINATOR)){
			for (final AppUser appUser : AppUser.find.where().eq("location.id", user.location.id).findList()) {
				vals.put(appUser.id + "", appUser.name);
			}
			return vals;
		}
		for (final AppUser appUser : AppUser.find.all()) {
			vals.put(appUser.id + "", appUser.name);
		}
		return vals;
	}*/


	/*public AppUserBean toBean() {

		final AppUserBean userBean = new AppUserBean();
		userBean.id = this.id;

		if (this.name != null) {
			userBean.name = this.name;
		}

		if (this.username != null) {
			userBean.username = this.username;
		}

		if (this.email != null) {
			userBean.email = this.email;
		}

		if (this.password != null) {
			userBean.password = this.password;
		}

		if (this.gender != null) {
			userBean.gender = this.gender;
		}

		if (this.age != null) {
			userBean.age = this.age;
		}


		return userBean;
	}*/

}
