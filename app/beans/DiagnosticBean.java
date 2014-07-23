package beans;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;

import models.Address;
import models.diagnostic.DiagnosticCentre;
import play.data.validation.Constraints.Email;
import play.data.validation.Constraints.Required;
import controllers.LoginController;

public class DiagnosticBean {


	@GeneratedValue(strategy=GenerationType.AUTO)
	@Id
	public Long id;

	@Lob
	public byte[] file;

	public String name;

	public Address address;

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
