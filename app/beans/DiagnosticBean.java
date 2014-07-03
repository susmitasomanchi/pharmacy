package beans;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import controllers.LoginController;
import play.Logger;
import play.data.validation.Constraints.Email;
import play.data.validation.Constraints.Required;
import models.Address;
import models.AppUser;
import models.Patient;
import models.Role;
import models.Sex;
import models.diagnostic.DiagnosticCentre;
import models.diagnostic.DiagnosticRepresentative;

public class DiagnosticBean {
	

	@GeneratedValue(strategy=GenerationType.AUTO)
	@Id
	public Long id;

	@Lob
	public byte[] file;	

	public String name;

	public String address;

	public String mobileNo;

	@Email
	@Required
	public String emailId;

	public String websiteName;
	
	
	public DiagnosticCentre toDiagnosticCentre(){

	final DiagnosticCentre diagnosticCentre = LoginController.getLoggedInUser().getDiagnosticRepresentative().diagnosticCentre;
		
		if(this.name != null) {
			diagnosticCentre.name= this.name;
		}
		if(this.address != null) {
			diagnosticCentre.address= this.address;
		}
		if(this.mobileNo != null) {
			diagnosticCentre.mobileNo= this.mobileNo;
		}
		if(this.emailId != null) {
			diagnosticCentre.emailId= this.emailId;
		}
		if(this.websiteName != null) {
			diagnosticCentre.websiteName= this.websiteName;
		}
		
		
		
		return diagnosticCentre;

	}

}
