package models.diagnostic;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import models.BaseEntity;
import models.doctor.Doctor;
import play.data.validation.Constraints.Email;
import play.data.validation.Constraints.Required;
import play.db.ebean.Model;
import beans.DiagnosticBean;

@SuppressWarnings("serial")
@Entity
public class DiagnosticCentre extends BaseEntity {



	@GeneratedValue(strategy = GenerationType.AUTO)
	@Id
	public Long id;

	@Required
	public String name;

	public String address;

	public String mobileNo;

	@Email
	@Required
	public String emailId;

	public String websiteName;


	public List<Doctor> doctorList = new ArrayList<Doctor>();

	@OneToOne
	public DiagnosticRepresentative diagnosticRepAdmin;

	@OneToMany(cascade=CascadeType.ALL,mappedBy="diagnosticCentre")
	public List<DiagnosticRepresentative> diagnosticRepresentativelist = new ArrayList<DiagnosticRepresentative>();


	@OneToMany(cascade=CascadeType.ALL)
	public List<DiagnosticTest> diagnosticTestList = new ArrayList<DiagnosticTest>();

	//	@OneToMany(cascade=CascadeType.ALL)
	//	 * 	public List<Patient> patientList = new ArrayList<Patient>();


	@OneToMany(cascade=CascadeType.ALL)
	public List<DiagnosticOrder> diagnosticOrderList = new ArrayList<DiagnosticOrder>();

	public static Model.Finder<Long, DiagnosticCentre> find = new Finder<Long, DiagnosticCentre>(
			Long.class, DiagnosticCentre.class);


	public DiagnosticBean toBean(){

		final DiagnosticBean diagnosticBean = new DiagnosticBean();
		diagnosticBean.id = this.id;


		if(this.name != null) {
			diagnosticBean.name= this.name;
		}
		if(this.address != null) {
			diagnosticBean.address= this.address;
		}
		if(this.mobileNo != null) {
			diagnosticBean.mobileNo= this.mobileNo;
		}
		if(this.emailId != null) {
			diagnosticBean.emailId= this.emailId;
		}
		if(this.websiteName != null) {
			diagnosticBean.websiteName= this.websiteName;
		}
		return diagnosticBean;
	}




	/*
	@Override
	public String toString() {
		return this.id + "  " + this.name + "  " + this.services + "  "
				+ this.contactPersonName + "  " + this.address + "  "
				+ this.mobileNo + "  " + this.emailId + "  " + this.websiteName;
	}
	 */

}
