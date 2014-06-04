package beans;

import java.io.Serializable;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import models.AppUser;
import models.Patient;
import models.Sex;
import play.data.validation.Constraints.Email;


@SuppressWarnings("serial")
public class PatientBean implements Serializable{

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Long id;



	public String name;

	public String username;



	@Email
	public String email;




	public String password;


	public Sex sex;;

	public String age;

	public String mbno;


	public String date;


	public String address;

	public String disease;

	public String appointmentId;

	public String doctorAvailability;

	public String isUrgentPatient;

	public AppUser toAppUserEntity(){

		final AppUser appUser = new AppUser();

		appUser.id = this.id;

		if(this.name != null) {
			appUser.name= this.name;
		}

		if(this.username != null) {
			appUser.username= this.username;
		}

		if(this.email != null) {
			appUser.email= this.email;
		}

		if(this.password != null) {
			appUser.password= this.password;
		}

		if(this.sex != null) {
			appUser.sex= this.sex;
		}

		if(this.age != null) {
			appUser.age= this.age;
		}
		return appUser;

	}


	public Patient toPatientEntity(){

		final Patient patient = new Patient();

		patient.id = this.id;

		if(this.mbno != null) {
			patient.mbno= this.mbno;
		}

		if(this.date != null) {
			patient.date= this.date;
		}

		if(this.address != null) {
			patient.address= this.address;
		}

		if(this.disease != null) {
			patient.disease= this.disease;
		}

		if(this.appointmentId != null) {
			patient.appointmentId= this.appointmentId;
		}

		if(this.doctorAvailability != null) {
			patient.doctorAvailability= this.doctorAvailability;
		}

		if(this.isUrgentPatient != null) {
			patient.isUrgentPatient= this.isUrgentPatient;
		}

		return patient;

	}






}
