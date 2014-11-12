package models.doctor;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import controllers.LoginController;
import models.AppUser;
import models.BaseEntity;
import models.Locality;
import models.PrimaryCity;
import models.diagnostic.DiagnosticCentre;
import models.diagnostic.DiagnosticCentrePrescriptionInfo;
import models.patient.Patient;
import models.patient.PatientDoctorInfo;
import models.pharmacist.Pharmacy;
import play.Logger;
import play.data.validation.Constraints.Required;
import play.db.ebean.Model;
import play.mvc.Result;

@SuppressWarnings("serial")
@Entity
public class Doctor extends BaseEntity{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long id;

	@OneToOne
	public AppUser appUser;

	@OneToMany(cascade = CascadeType.ALL)
	public List<DoctorClinicInfo> doctorClinicInfoList = new ArrayList<DoctorClinicInfo>();

	@Required
	public String registrationNumber;

	@ManyToMany(cascade = CascadeType.ALL)
	public List<MasterSpecialization> specializationList = new ArrayList<MasterSpecialization>();

	@Required
	public String position;

	@Required
	public String degree;

	@Column(columnDefinition="TEXT")
	public String description;

	@Lob
	public byte[] backgroundImage;

	@Lob
	public byte[] profileImage;

	//education
	@OneToMany(cascade=CascadeType.ALL)
	public List<DoctorEducation> doctorEducationList = new ArrayList<DoctorEducation>();

	//experience
	@OneToMany(cascade=CascadeType.ALL)
	public List<DoctorExperience> doctorExperienceList = new ArrayList<DoctorExperience>();

	//publications
	@OneToMany(cascade=CascadeType.ALL)
	public List<DoctorPublication> doctorPublicationList = new ArrayList<DoctorPublication>();

	//awards
	@OneToMany(cascade=CascadeType.ALL)
	public List<DoctorAward> doctorAwardList = new ArrayList<DoctorAward>();

	//socialwork
	@OneToMany(cascade=CascadeType.ALL)
	public List<DoctorSocialWork> doctorSocialWorkList = new ArrayList<DoctorSocialWork>();

	@OneToMany(cascade=CascadeType.ALL)
	public List<DoctorProduct> myProductList = new ArrayList<DoctorProduct>();

	@OneToMany(cascade=CascadeType.ALL)
	public List<DoctorDiagnosticTest> myDiagnosticTestList = new ArrayList<DoctorDiagnosticTest>();

	@ManyToMany(cascade=CascadeType.ALL)
	public List<Pharmacy> pharmacyList=new ArrayList<Pharmacy>();

	@ManyToMany(cascade=CascadeType.ALL)
	public List<DiagnosticCentre> diagnosticCentreList=new ArrayList<DiagnosticCentre>();

	/**
	 * 	Using it to capture starting year of experience.
	 * 	So, Doctor's experience = currentYear - this.experience
	 * 	Check out doctor.getYearsOfExperience()
	 */
	public Integer experience;


	@Column(columnDefinition="TEXT")
	public String searchIndex;

	@Column(columnDefinition="TEXT")
	public String slugUrl;

	@OneToOne
	public PrimaryCity primaryCity;

	/*@OneToOne
	public Locality locality;
	 */
	public boolean isRegVerified = false;

	public static Model.Finder<Long,Doctor> find = new Model.Finder<Long, Doctor>(Long.class, Doctor.class);

	@OneToMany(cascade = CascadeType.ALL)
	public List<SigCode> sigCodeList = new ArrayList<SigCode>();






	public List<DoctorExperience> getExperienceListInOrder(){
		return DoctorExperience.find.where().eq("doctor_id", this.id).orderBy("workedFrom DESC").findList();
	}

	public List<DoctorEducation> getEducationListInOrder(){
		return DoctorEducation.find.where().eq("doctor_id", this.id).orderBy("fromYear DESC").findList();
	}

	public List<DoctorSocialWork> getDoctorSocialWorkList() {
		return this.doctorSocialWorkList;
	}

	public List<DoctorClinicInfo> getActiveClinic(){
		return DoctorClinicInfo.find.where().eq("doctor", this).eq("active", true).findList();
	}

	public int getPrescriptionRowCount(){
		return Prescription.find.where().eq("doctor", this).findRowCount();
	}
	public int getAppointmentRowCount(){
		return Appointment.find.where().eq("doctorClinicInfo.doctor", this).eq("appointmentStatus", AppointmentStatus.APPROVED).findRowCount();
	}
	public List<PatientDoctorInfo> getPatientDoctorInfo(){
		return PatientDoctorInfo.find.where().eq("doctor", this).findList();
	}
	public int getDoctorClinicAppointmentCount(final Long clinicId){
		final Date now = new Date();
		final Calendar calendarFrom = Calendar.getInstance();
		calendarFrom.setTime(now);
		calendarFrom.set(Calendar.HOUR_OF_DAY, 0);
		calendarFrom.set(Calendar.MINUTE, 0);
		calendarFrom.set(Calendar.SECOND,0);
		calendarFrom.set(Calendar.MILLISECOND,0);

		final Calendar calendarTo = Calendar.getInstance();
		calendarTo.setTime(now);
		calendarTo.set(Calendar.HOUR_OF_DAY, 23);
		calendarTo.set(Calendar.MINUTE, 59);
		calendarTo.set(Calendar.SECOND,59);
		calendarTo.set(Calendar.MILLISECOND,999);

		final int appointments =
				Appointment.find.where()
				.eq("doctorClinicInfo.doctor", this)
				.eq("doctorClinicInfo.clinic", Clinic.find.byId(clinicId))
				.ge("bookedOn", calendarFrom.getTime())
				.le("bookedOn", calendarTo.getTime())
				.orderBy("appointmentTime")
				.findRowCount();
		return appointments;

	}
	public int getDoctorClinicPrescriptionCount(final Long clinicId){
		final Date now = new Date();
		final Calendar calendarFrom = Calendar.getInstance();
		calendarFrom.setTime(now);
		calendarFrom.set(Calendar.HOUR_OF_DAY, 0);
		calendarFrom.set(Calendar.MINUTE, 0);
		calendarFrom.set(Calendar.SECOND,0);
		calendarFrom.set(Calendar.MILLISECOND,0);

		final Calendar calendarTo = Calendar.getInstance();
		calendarTo.setTime(now);
		calendarTo.set(Calendar.HOUR_OF_DAY, 23);
		calendarTo.set(Calendar.MINUTE, 59);
		calendarTo.set(Calendar.SECOND,59);
		calendarTo.set(Calendar.MILLISECOND,999);

		final int appointments =
				Appointment.find.where()
				.eq("doctorClinicInfo.doctor", this)
				.eq("doctorClinicInfo.clinic", Clinic.find.byId(clinicId))
				.eq("appointmentStatus", AppointmentStatus.SERVED)
				.ge("bookedOn", calendarFrom.getTime())
				.le("bookedOn", calendarTo.getTime())
				.orderBy("appointmentTime")
				.findRowCount();
		return appointments;

	}
	public int getDoctorClinicPendingAppointmentCount(final Long clinicId){
		final Date now = new Date();
		final Calendar calendarFrom = Calendar.getInstance();
		calendarFrom.setTime(now);
		calendarFrom.set(Calendar.HOUR_OF_DAY, 0);
		calendarFrom.set(Calendar.MINUTE, 0);
		calendarFrom.set(Calendar.SECOND,0);
		calendarFrom.set(Calendar.MILLISECOND,0);

		final Calendar calendarTo = Calendar.getInstance();
		calendarTo.setTime(now);
		calendarTo.set(Calendar.HOUR_OF_DAY, 23);
		calendarTo.set(Calendar.MINUTE, 59);
		calendarTo.set(Calendar.SECOND,59);
		calendarTo.set(Calendar.MILLISECOND,999);

		final int appointments =
				Appointment.find.where()
				.eq("doctorClinicInfo.doctor", this)
				.eq("doctorClinicInfo.clinic", Clinic.find.byId(clinicId))
				.eq("appointmentStatus", AppointmentStatus.APPROVED)
				.ge("bookedOn", calendarFrom.getTime())
				.le("bookedOn", calendarTo.getTime())
				.orderBy("appointmentTime")
				.findRowCount();
		return appointments;

	}

	@Override
	public void save(){
		final StringBuilder stringBuilder = new StringBuilder();
		for (final DoctorClinicInfo clinicInfo : this.doctorClinicInfoList) {
			if(clinicInfo.clinic != null){
				stringBuilder.append(clinicInfo.clinic.name.toLowerCase());
			}
			if(clinicInfo.clinic.address != null && clinicInfo.clinic.address.locality != null){
				stringBuilder.append(clinicInfo.clinic.address.locality.name.toLowerCase());
			}
		}
		if(this.appUser.name != null){
			stringBuilder.append(this.appUser.name.toLowerCase());
		}
		for(final MasterSpecialization spez : this.specializationList){
			stringBuilder.append(spez.name.toLowerCase());
		}
		if(this.degree != null){
			stringBuilder.append(this.degree.toLowerCase());
		}
		if(this.slugUrl != null){
			stringBuilder.append(this.slugUrl.toLowerCase());
		}
		this.searchIndex = stringBuilder.toString();
		super.save();
	}


	@Override
	public void update() {
		final StringBuilder stringBuilder = new StringBuilder();
		for (final DoctorClinicInfo clinicInfo : this.doctorClinicInfoList) {
			if(clinicInfo.clinic != null && clinicInfo.active){
				stringBuilder.append(clinicInfo.clinic.name.toLowerCase());
				if(clinicInfo.clinic.address != null && clinicInfo.clinic.address.locality != null){
					stringBuilder.append(clinicInfo.clinic.address.locality.name.toLowerCase());
				}
			}
		}
		if(this.appUser.name != null){
			stringBuilder.append(this.appUser.name.toLowerCase());
		}
		for(final MasterSpecialization spez : this.specializationList){
			stringBuilder.append(spez.name.toLowerCase());
		}
		if(this.degree != null){
			stringBuilder.append(this.degree.toLowerCase());
		}
		if(this.slugUrl != null){
			stringBuilder.append(this.slugUrl.toLowerCase());
		}
		this.searchIndex = stringBuilder.toString();
		super.update();
	}


	public Integer getYearsOfExperience(){
		final Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		final int currentYear = cal.get(Calendar.YEAR);
		if(this.experience != null){
			if(currentYear - this.experience == 0){
				return 1;
			}
			return (currentYear - this.experience);
		}
		else{
			return 1;
		}
	}


	public List<DoctorClinicInfo> doctorActiveClinicInfoList(){
		return DoctorClinicInfo.find.where().eq("doctor", this).eq("active", true).findList();
	}
	public List<Prescription> doctorPatientPrescriptionList(final Long id){
		return Prescription.find.where().eq("doctor", this).eq("patient", AppUser.find.byId(id).getPatient()).orderBy().desc("prescriptionDate").findList();
	}

	public String getSpecializations(){
		final StringBuilder sb = new StringBuilder("");
		if(this.specializationList.size()>0){
			for (final MasterSpecialization spez : this.specializationList) {
				sb.append(spez.name+", ");
			}
			final int l = sb.length();
			sb.replace(l-2, l, "");
			return sb.toString();
		}
		else{
			return "Specialization";
		}
	}
	public static Boolean getClinicTimings(){
		final Doctor loggedInDoctor = LoginController.getLoggedInUser()
				.getDoctor();
		final List<DoctorClinicInfo> doctorClinicInfos = loggedInDoctor.getActiveClinic();

		//	final List<DoctorClinicInfo> doctorClinicInfos = DoctorClinicInfo.find.where().eq("doctor", Doctor.find.byId(id)).findList();
		Logger.info(doctorClinicInfos.size()+"   size1");
		for (final DoctorClinicInfo doctorClinicInfo : doctorClinicInfos) {
			final List<DaySchedule> daySchedules = DaySchedule.find.where().eq("doctor_clinic_info_id", doctorClinicInfo.id).findList();
			for (final DaySchedule daySchedule : daySchedules) {
				Logger.info("day=="+daySchedule.day);
				Logger.info("day=="+daySchedule.fromTime);
				Logger.info("day=="+daySchedule.toTime);

			}
		}
		return true;
	}


}

