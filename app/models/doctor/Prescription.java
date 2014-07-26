package models.doctor;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import models.BaseEntity;
import models.diagnostic.DiagnosticReport;
import models.diagnostic.DiagnosticTestLineItem;
import models.pharmacist.MedicineLineItem;
import play.db.ebean.Model;


@SuppressWarnings("serial")
@Entity
public class Prescription extends BaseEntity{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public  Long id;

	@OneToOne
	public Appointment appointment;

	public String problemStatement;

	public String prognosis;

	@OneToMany(cascade=CascadeType.ALL)
	public List<MedicineLineItem> medicineLineItemList = new ArrayList<MedicineLineItem>();

	@OneToMany(cascade=CascadeType.ALL)
	public List<DiagnosticTestLineItem> diagnosticTestLineItemList = new ArrayList<DiagnosticTestLineItem>();

	@ManyToMany(cascade=CascadeType.ALL)
	public List<DiagnosticReport> diagnosticReportList = new ArrayList<>();

	public String remarks;

	public static Model.Finder<Long, Prescription> find = new Finder<Long, Prescription>(Long.class, Prescription.class);
}
