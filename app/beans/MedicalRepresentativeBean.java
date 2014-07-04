package beans;

import java.io.Serializable;
import java.util.Date;
import models.AppUser;
import models.Role;
import models.Sex;

public class MedicalRepresentativeBean implements Serializable {
	
	public String name;
	public String designation;
	public String userName;
	public String email;
	public String password;
	public String role;
	public String sex;	
	public Date dob;
	public String regionAlloted;
	public String companyName;
	public String typesOfMedicine;
	public String noOfDoctorsVisit;
	public Long mrAdminId;

	public AppUser toAppUser(){
		final AppUser appUser = new AppUser();
		appUser.name = this.name;
		appUser.email = this.email;
		appUser.password = this.password;
		appUser.role = Role.valueOf(this.role);
		appUser.sex = Sex.valueOf(this.sex);
		appUser.username = this.userName;
		return appUser;
	}
	

}
