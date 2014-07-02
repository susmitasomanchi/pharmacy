package models;
import java.util.List;
import java.util.ArrayList;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import models.doctor.Doctor;
import play.data.validation.Constraints.Email;
import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

@SuppressWarnings("serial")
@Entity
public class DiagnosticCentre extends BaseEntity {

		

	@GeneratedValue(strategy = GenerationType.AUTO)
	@Id
	public Long id;

	@Required
	public String name;

	@Required
	public String services;

	@Required
	public String costOfServices;

	@Required
	public String contactPersonName;

	@Required
	public String address;

	@Required
	public String mobileNo;

	@Email
	@Required
	public String emailId;

	public String websiteName;
	
	
	public List<Doctor> doctorList = new ArrayList<Doctor>();

	@OneToOne
	public DiagnosticRepresentative diagnosticRepAdmin;

	@OneToMany(cascade=CascadeType.ALL, mappedBy="diagnosticCentre")
	public List<DiagnosticRepresentative> diagnosticRepresentativelist = new ArrayList<DiagnosticRepresentative>();

	@OneToMany(cascade=CascadeType.ALL)
	public List<DiagnosticTest> diagnosticTestList = new ArrayList<DiagnosticTest>();
	
	@OneToMany(cascade=CascadeType.ALL)
	public List<DiagnosticOrder> diagnosticOrderList = new ArrayList<DiagnosticOrder>();

	public static Model.Finder<Long, DiagnosticCentre> find = new Finder<Long, DiagnosticCentre>(
			Long.class, DiagnosticCentre.class);


	

	@Override
	public String toString() {
		return this.id + "  " + this.name + "  " + this.services + "  "
				+ this.contactPersonName + "  " + this.address + "  "
				+ this.mobileNo + "  " + this.emailId + "  " + this.websiteName;
	}

	
}
