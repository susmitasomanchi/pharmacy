package models.diagnostic;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;

import models.AppUser;
import models.BaseEntity;
import models.FileEntity;
import models.doctor.Prescription;
import play.db.ebean.Model;
@SuppressWarnings("serial")
@Entity
public class DiagnosticCentrePrescriptionInfo extends BaseEntity{
	String s;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public  Long id;

	@OneToOne
	public DiagnosticCentre diagnosticCentre;

	@OneToOne
	public Prescription prescription;

	public DiagnosticCentrePrescritionStatus diagnosticCentrePrescritionStatus = DiagnosticCentrePrescritionStatus.RECEIVED;

	@OneToOne
	public AppUser sharedBy;

	public Date sharedDate;
	
	public Date servedDate;

	public Boolean patientsConsent = Boolean.FALSE;

	@ManyToMany(cascade=CascadeType.ALL)
	public List<FileEntity> fileEntities = new ArrayList<FileEntity>();

	public static Model.Finder<Long, DiagnosticCentrePrescriptionInfo> find = new Finder<Long, DiagnosticCentrePrescriptionInfo>(Long.class, DiagnosticCentrePrescriptionInfo.class);
}
