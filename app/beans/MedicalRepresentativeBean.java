package beans;

import java.io.Serializable;
import models.AppUser;
import models.Role;
import models.mr.Designation;
import models.mr.MedicalRepresentative;

@SuppressWarnings("serial")
public class MedicalRepresentativeBean implements Serializable {


	public Long   id;
	public Long   appid;
	public String name;
	public Long designationId;
	public String username;
	public String email;
	public int age;
	public String sex;
	public String regionAlloted;
	public String companyName;
	public Long mrAdminId;
	public Long manager;
	public String status;

	public AppUser toAppUser() {
		if(this.appid==null){
			final AppUser appUser = new AppUser();
			appUser.name = this.name;
			appUser.email = this.email;
			appUser.role = Role.MR;
			appUser.username = this.username;
			return appUser;
		}
		else{
			final AppUser appUser=AppUser.find.byId(this.appid);
			appUser.name = this.name;
			appUser.email = this.email;
			appUser.role = Role.MR;
			appUser.username = this.username;
			return appUser;
		}
	}


	public MedicalRepresentative toMedicalRepresentative(){

		MedicalRepresentative mr = new MedicalRepresentative();

		if(this.id != null){
			mr = MedicalRepresentative.find.byId(this.id);
		}

		if(this.designationId != null) {
			mr.designation = Designation.find.byId(this.designationId);
		}
		else{
			mr.designation = new Designation();
		}

		if(this.regionAlloted != null) {
			mr.regionAlloted= this.regionAlloted;
		}


		if(this.status != null) {
			mr.status= this.status;
		}

		return mr;

	}

}
