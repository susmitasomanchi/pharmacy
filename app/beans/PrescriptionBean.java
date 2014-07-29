package beans;



import java.io.Serializable;

import java.util.ArrayList;

import java.util.Date;

import java.util.List;



import models.diagnostic.DiagnosticTestLineItem;

import models.doctor.Appointment;

import models.doctor.Clinic;

import models.doctor.Doctor;

import models.doctor.Prescription;

import models.patient.Patient;

import models.pharmacist.MedicineLineItem;



@SuppressWarnings("serial")

public class PrescriptionBean implements Serializable{



	public  Long id;



	public Long doctorId;



	public Long clinicId;



	public Long patientId;



	public Long appointmentId;



	public Date prescriptionDate;



	public String problemStatement;



	public String prognosis;



	public List<Long> medicineLineItemListIds       = new ArrayList<Long>();



	public List<Long> diagnosticTestLineItemListIds = new ArrayList<Long>();



	public String remarks;



	public Prescription toEntity() {



		final Prescription prescription = new Prescription();



		if(this.id != null) {

			prescription.id = this.id;

		}

		if(this.doctorId != null) {

			prescription.doctor = Doctor.find.byId(this.doctorId);

		}

		if(this.clinicId != null) {

			prescription.clinic = Clinic.find.byId(this.clinicId);

		}

		if(this.patientId != null) {

			prescription.patient = Patient.find.byId(this.patientId);

		}

		if(this.appointmentId != null) {

			prescription.appointment = Appointment.find.byId(this.appointmentId);

		}

		if(this.prescriptionDate != null) {

			prescription.prescriptionDate = this.prescriptionDate;

		}

		if(this.problemStatement != null) {

			prescription.problemStatement = this.problemStatement;

		}

		if(this.prognosis != null) {

			prescription.prognosis = this.prognosis;

		}

		for(final Long id:this.medicineLineItemListIds) {

			prescription.medicineLineItemList.add(MedicineLineItem.find.byId(id));

		}

		for(final Long id:this.diagnosticTestLineItemListIds) {

			prescription.diagnosticTestLineItemList.add(DiagnosticTestLineItem.find.byId(id));

		}

		if(this.remarks != null) {

			prescription.remarks = this.remarks;

		}



		return prescription;

	}

}

