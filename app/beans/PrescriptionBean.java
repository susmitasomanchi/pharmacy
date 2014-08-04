package beans;

import java.io.Serializable;
import java.util.Date;

import models.doctor.Appointment;
import models.doctor.DiagnosticTestLineItem;
import models.doctor.Doctor;
import models.doctor.MedicineLineItem;
import models.doctor.Prescription;



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

	public String[] medicineNameArray = new String[20];

	public String[] medicineDosageArray = new String[20];

	public String[] medicineRemarksArray = new String[20];

	public String[] diagnosticTestNameArray = new String[20];

	public String[] diagnosticTestRemarksArray = new String[20];

	public String remarks;

	public Prescription toEntity() {

		final Prescription prescription = new Prescription();

		if(this.id != null) {
			prescription.id = this.id;
		}

		if(this.doctorId != null) {
			prescription.doctor = Doctor.find.byId(this.doctorId);
		}


		/*
		 * Clinic and patient will be pull from appointment
		 * 
		if(this.clinicId != null) {
			prescription.clinic = Clinic.find.byId(this.clinicId);
		}

		if(this.patientId != null) {
			prescription.patient = Patient.find.byId(this.patientId);
		}
		 */

		if(this.appointmentId != null) {
			final Appointment appointment = Appointment.find.byId(this.appointmentId);
			prescription.appointment = appointment;
			prescription.patient = appointment.requestedBy.getPatient();
			prescription.clinic = appointment.doctorClinicInfo.clinic;
		}

		if(this.prescriptionDate != null) {
			prescription.prescriptionDate = this.prescriptionDate;
		}
		else{
			prescription.prescriptionDate = new Date();
		}

		if(this.problemStatement != null) {
			prescription.problemStatement = this.problemStatement;
		}

		if(this.prognosis != null) {
			prescription.prognosis = this.prognosis;
		}

		/*
		for(final String medName: this.medicineNameArray){
			if(medName != null && !medName.trim().isEmpty()){
				final MedicineLineItem medLineItem = new MedicineLineItem();
				medLineItem.medicineFullName = medName.trim();
				prescription.medicineLineItemList.add(medLineItem);
			}
		}
		 */

		for(int m=0; m<this.medicineNameArray.length; m++){
			final String medName = this.medicineNameArray[m];
			if(medName != null && !medName.trim().isEmpty()){
				final MedicineLineItem medLineItem = new MedicineLineItem();
				medLineItem.medicineFullName = medName.trim();
				if(this.medicineDosageArray != null && this.medicineDosageArray[m]!= null && !this.medicineDosageArray[m].isEmpty()){
					medLineItem.dosage = this.medicineDosageArray[m].trim();
				}
				if(this.medicineRemarksArray != null && this.medicineRemarksArray[m]!= null && !this.medicineRemarksArray[m].isEmpty()){
					medLineItem.remarks = this.medicineRemarksArray[m].trim();
				}
				prescription.medicineLineItemList.add(medLineItem);
			}
		}

		/*
		for(final String dTestName: this.diagnosticTestNameArray){
			if(dTestName != null && !dTestName.trim().isEmpty()){
				final DiagnosticTestLineItem dTestLineItem = new DiagnosticTestLineItem();
				dTestLineItem.diagnosticTestFullName = dTestName;
				prescription.diagnosticTestLineItemList.add(dTestLineItem);
			}
		}

		 */

		for(int d=0; d<this.diagnosticTestNameArray.length; d++){
			final String dTestName = this.diagnosticTestNameArray[d];
			if(dTestName != null && !dTestName.trim().isEmpty()){
				final DiagnosticTestLineItem dTestLineItem = new DiagnosticTestLineItem();
				dTestLineItem.fullNameOfDiagnosticTest = dTestName;
				if(this.diagnosticTestRemarksArray != null && this.diagnosticTestRemarksArray[d]!= null && !this.diagnosticTestRemarksArray[d].isEmpty()){
					dTestLineItem.remarks = this.diagnosticTestRemarksArray[d].trim();
				}
				prescription.diagnosticTestLineItemList.add(dTestLineItem);
			}
		}


		if(this.remarks != null) {
			prescription.remarks = this.remarks;
		}

		return prescription;

	}

}

