
/*****

THIS IS AN AUTO GENERATED CODE
PLEASE DO NOT MODIFY IT BY HAND

 *****/
package beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import models.AppUser;
import models.CreditScore;
import models.Location;
import models.Notification;
import models.Role;
import models.UserPreference;
import play.data.validation.Constraints.Email;
import play.data.validation.Constraints.Required;

@SuppressWarnings("serial")
public class AppUserBean implements Serializable {

	public Long id;


	@Required
	public String name;



	@Required
	public String designation;



	@Required
	public String username;



	@Required @Email
	public String email;



	@Required
	public String password;



	@Required
	public String sapNo;


	@Required
	public Role role;


	public Long location;

	public Long userPreference;

	public List<Long> notificationList = new ArrayList<>();

	public Long creditScore;

	public AppUser toEntity(){

		final AppUser appUser = new AppUser();
		appUser.id = this.id;

		if(this.name != null) {
			appUser.name= this.name;
		}


		if(this.designation != null) {
			appUser.designation= this.designation;
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


		if(this.sapNo != null) {
			appUser.sapNo= this.sapNo;
		}


		if(this.role != null) {
			appUser.role= this.role;
		}


		if(this.location != null) {
			appUser.location= Location.find.byId(this.location);
		}


		if(this.userPreference != null) {
			appUser.userPreference= UserPreference.find.byId(this.userPreference);
		}


		for (final Long nId : this.notificationList) {
			appUser.notificationList.add(Notification.find.byId(nId));
		}

		if(this.creditScore != null) {
			appUser.creditScore= CreditScore.find.byId(this.creditScore);
		}

		return appUser;

	}

}

