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
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.SqlRow;

import controllers.LoginController;
import play.data.validation.Constraints.Email;
import play.data.validation.Constraints.Required;
import play.db.ebean.Model;
import utils.ViewsCounter;
import beans.AppUserBean;


@SuppressWarnings("serial")
@Entity
public class AppUser extends BaseEntity implements ViewsCounter{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long id;

	@Required
	public String name;

	@Required
	public String designation;

	@Required
	public String username;

	@Required
	@Email
	public String email;

	@Required
	public String password;

	@Required
	public String sapNo;

	@Required
	public Role role;

	@OneToOne
	public Location location;

	@OneToOne
	public UserPreference userPreference;

	@OneToMany(cascade = CascadeType.ALL)
	public List<Notification> notificationList = new ArrayList<>();

	@OneToOne
	public CreditScore creditScore;


	public static Model.Finder<Long, AppUser> find = new Finder<>(Long.class, AppUser.class);

	public static Map<String, String> options() {
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
	}

	public static List<AppUser> filterByLocation(final Location location){
		final List<AppUser> appUsers = AppUser.find.where().eq("location", location).findList();
		return appUsers;
	}

	public AppUserBean toBean() {

		final AppUserBean userBean = new AppUserBean();
		userBean.id = this.id;

		if (this.name != null) {
			userBean.name = this.name;
		}

		if (this.designation != null) {
			userBean.designation = this.designation;
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

		if (this.sapNo != null) {
			userBean.sapNo = this.sapNo;
		}

		if (this.role != null) {
			userBean.role = this.role;
		}

		if (this.sapNo != null) {
			userBean.sapNo = this.sapNo;
		}

		if (this.location != null) {
			userBean.location = this.location.id;
		}

		if (this.userPreference != null) {
			userBean.userPreference = this.userPreference.id;
		}

		for (final Notification noti : this.notificationList) {
			userBean.notificationList.add(noti.id);
		}

		if (this.creditScore != null) {
			userBean.creditScore = this.creditScore.id;
		}

		return userBean;
	}

	@Override
	public Long getViews() {
		final String sqlsum1 = "select sum(views) as sum from kunit where author_id ="+this.id;
		final SqlRow row1 = Ebean.createSqlQuery(sqlsum1).findUnique();
		final Long view1=row1.getLong("sum");
		return view1;
	}
}
