package beans;

import java.io.Serializable;
import models.AppUser;
import models.Role;
import play.data.validation.Constraints.Email;
import play.data.validation.Constraints.Required;

@SuppressWarnings("serial")
public class JoinUsBean implements Serializable{

	@Required
	public String name;

	@Required
	@Email
	public String email;

	@Required
	public String password;

	@Required
	public String confirmPassword;

	@Required
	public Role role;
	
	public String diagnosticCenterName;
	
	public String pharmacyName;
	
	public String pharmacutical_company_name;

	public String pharmaceuticalCompanyName;

	public AppUser toAppUser(){
		final AppUser appUser = new AppUser();

		appUser.name = this.name;
		appUser.email = this.email;
		appUser.password = this.password;
		appUser.role = this.role;

		return appUser;
	}
}
