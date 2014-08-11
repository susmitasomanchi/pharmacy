package models.patient;

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

import models.AppUser;
import models.BaseEntity;
import models.diagnostic.DiagnosticCentre;
import models.doctor.Appointment;
import models.pharmacist.Pharmacy;
import play.db.ebean.Model;

@SuppressWarnings("serial")
@Entity
public class Patient extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long id;

	@OneToOne
	public AppUser appUser;

	public String age;

	@OneToMany(cascade = CascadeType.ALL)
	public List<PatientDoctorInfo> patientDoctorInfoList = new ArrayList<PatientDoctorInfo>();

	@ManyToMany(cascade = CascadeType.ALL)
	public List<Pharmacy> pharmacyList = new ArrayList<Pharmacy>();

	@ManyToMany(cascade = CascadeType.ALL)
	public List<DiagnosticCentre> diagnosticCenterList = new ArrayList<DiagnosticCentre>();

	public static Model.Finder<Long, Patient> find = new Finder<Long, Patient>(Long.class, Patient.class);

	public List<Appointment> getAppointments(){
		final List<Appointment> appointments = Appointment.find.where()
				.eq("requestedBy", this.appUser).orderBy().desc("appointmentTime")
				.findList();
		return appointments;
		//return ok(views.html.patient.patientAllAppointments.render(appointments,docclinicInfo));
	}

	public String getSexAndAge(){
		return this.appUser.sex.toString().substring(0,1)+"/"+this.appUser.getAge();
	}

}
