package beans;

import java.io.Serializable;
import java.util.Date;

import javax.annotation.Generated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import models.AppUser;
import models.Role;
import models.Sex;
import models.mr.MedicalRepresentative;

@SuppressWarnings("serial")
public class MedicalRepresentativeBean implements Serializable {

	@Id
	public Long id;
	public Long appid;
	public String name;
	public String designation;
	public String username;
	public String email;
	public String password;
	public int age;
	// public String role;
	public String sex;
	public String regionAlloted;
	public String companyName;
	public String typesOfMedecine;
	public String noOfDoctorsVisit;
	public Long mrAdminId;

	public AppUser toAppUser() {
		final AppUser appUser = new AppUser();
		appUser.id=this.appid;
		appUser.name = this.name;
		appUser.email = this.email;
		appUser.password = this.password;
		appUser.role = Role.MR;
		// appUser.sex = Sex.valueOf(this.sex);
		appUser.username = this.username;
		return appUser;
	}
	
	public MedicalRepresentative toMedicalRepresentative(){
		final  MedicalRepresentative mr = new MedicalRepresentative();

		if(this.id != null){
		mr.id=this.id;
		}
		
		if(this.designation != null) {
			mr.designation= this.designation;
		}
		if(this.age != 0) {
			mr.age= this.age;
		}
		if(this.sex != null) {
			mr.sex= this.sex;
		}
		if(this.regionAlloted != null) {
			mr.regionAlloted= this.regionAlloted;
		}
		if(this.companyName != null) {
			mr.companyName= this.companyName;
		}
		
		if(this.typesOfMedecine != null) {
			mr.typesOfMedecine= this.typesOfMedecine;
		}
		if(this.noOfDoctorsVisit != null) {
			mr.noOfDoctorsVisit= this.noOfDoctorsVisit;
		}
		if(this.mrAdminId != null) {
			mr.mrAdminId= this.mrAdminId;
		}
		
		return mr;
		
	}

}
