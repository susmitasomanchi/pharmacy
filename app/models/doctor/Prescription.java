package models.doctor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import models.BaseEntity;
import models.diagnostic.DiagnosticCentrePrescriptionInfo;
import models.patient.Patient;
import models.pharmacist.PharmacyPrescriptionInfo;
import play.db.ebean.Model;


@SuppressWarnings("serial")
@Entity
public class Prescription extends BaseEntity{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public  Long id;

	@OneToOne
	public Doctor doctor;

	@OneToOne
	public Clinic clinic;

	@OneToOne
	public Patient patient;

	@OneToOne
	public Appointment appointment;

	public Date prescriptionDate;

	@Column(columnDefinition="TEXT")
	public String problemStatement;

	@Column(columnDefinition="TEXT")
	public String prognosis;

	@OneToMany(cascade=CascadeType.ALL)
	public List<MedicineLineItem> medicineLineItemList = new ArrayList<MedicineLineItem>();

	@OneToMany(cascade=CascadeType.ALL)
	public List<DiagnosticTestLineItem> diagnosticTestLineItemList = new ArrayList<DiagnosticTestLineItem>();

	@Column(columnDefinition="TEXT")
	public String remarks;

	public static Model.Finder<Long, Prescription> find = new Finder<Long, Prescription>(Long.class, Prescription.class);

	public List<PharmacyPrescriptionInfo> getPharmacyInfoList(){
		return PharmacyPrescriptionInfo.find.where().eq("prescription", this).findList();
	}

	public List<DiagnosticCentrePrescriptionInfo> getDiagnoticInfoList(){
		return DiagnosticCentrePrescriptionInfo.find.where().eq("prescription", this).findList();
	}

	/**
	 * Not required in this format. Gotta come up with a better way to
	 * edit existing prescriptions (if that is desired in the first place)
	 * 
	public PrescriptionBean toBean() {

		final PrescriptionBean bean = new PrescriptionBean();

		if(this.id != null) {
			bean.id = this.id;
		}
		if(this.doctor != null) {
			bean.doctorId = this.doctor.id;
		}
		if(this.clinic != null) {
			bean.clinicId = this.clinic.id;
		}
		if(this.patient != null) {
			bean.patientId = this.patient.id;
		}
		if(this.appointment != null) {
			bean.appointmentId = this.appointment.id;
		}
		if(this.prescriptionDate != null) {
			bean.prescriptionDate = this.prescriptionDate;
		}
		if(this.problemStatement != null) {
			bean.problemStatement = this.problemStatement;
		}
		if(this.prognosis != null) {
			bean.prognosis = this.prognosis;
		}
		for(final MedicineLineItem item:this.medicineLineItemList) {
			bean.medicineLineItemListIds.add(item.id);
		}
		for(final DiagnosticTestLineItem item:this.diagnosticTestLineItemList) {
			bean.diagnosticTestLineItemListIds.add(item.id);
		}
		if(this.remarks != null) {
			bean.remarks = this.remarks;
		}

		return bean;
	}
	 */
}
