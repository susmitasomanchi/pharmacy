package beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import models.Address;
import models.Role;
import models.State;
import models.doctor.Clinic;
import models.doctor.Day;
import models.doctor.DaySchedule;
import models.doctor.Doctor;
import models.doctor.DoctorClinicInfo;
import play.Logger;

@SuppressWarnings("serial")
public class DoctorClinicInfoBean implements Serializable {

	public Long id;

	public Long doctorId;

	public Long clinicId;

	public String name;

	public String contactPersonName;

	public String contactNo;

	public Long addressId;

	public String street;

	public String area;

	public String city;

	public String state;

	public List<String> fromHrs = new ArrayList<String>();

	public List<String> toHrs = new ArrayList<String>();

	public List<String> fromHrsMr = new ArrayList<String>();

	public List<String> toHrsMr = new ArrayList<String>();

	public List<String> daysOfWeek = new ArrayList<String>();

	public List<String> daysOfWeekMr = new ArrayList<String>();

	public String lat;

	public String lng;

	public Integer slot;

	public Integer slotmr;

	public DoctorClinicInfo toDoctorClinicInfo() {

		final DoctorClinicInfo doctorClinicInfo = new DoctorClinicInfo();

		if (this.id != null) {
			doctorClinicInfo.id = this.id;
		}

		if(this.doctorId != null){
			doctorClinicInfo.doctor = Doctor.find.byId(this.doctorId);
		}


		Logger.info("" + this.id);
		Logger.info("name null");

		final Clinic clinic = new Clinic();
		clinic.name = this.name;
		clinic.contactNo = this.contactNo;
		clinic.contactPersonName = this.contactPersonName;
		if(this.clinicId != null){
			clinic.id = this.clinicId;
		}
		doctorClinicInfo.clinic = clinic;

		Logger.info(doctorClinicInfo.clinic.name);
		Logger.info("from hrs" + this.fromHrs.size());
		Logger.info("" + this.toHrs);
		Logger.info(this.daysOfWeek.size() + "");

		for (int index = 0; index < this.daysOfWeek.size(); index++) {

			if (this.daysOfWeek.get(index) != null) {
				final DaySchedule schedule = new DaySchedule();
				schedule.fromTime = this.fromHrs.get(index);
				schedule.toTime = this.toHrs.get(index);
				schedule.day = Day.valueOf(this.daysOfWeek.get(index));
				Logger.info(schedule.day.toString());
				schedule.requester = Role.PATIENT;
				doctorClinicInfo.scheduleDays.add(schedule);
			}
		}

		Logger.info(this.daysOfWeekMr.size() + "mr size");

		for (int index = 0; index < this.daysOfWeekMr.size(); index++) {
			if (this.daysOfWeekMr.get(index) != null) {
				final DaySchedule schedule = new DaySchedule();
				schedule.fromTime = this.fromHrsMr.get(index);
				schedule.toTime = this.toHrsMr.get(index);
				schedule.requester = Role.MR;
				schedule.day = Day.valueOf(this.daysOfWeekMr.get(index));
				doctorClinicInfo.scheduleDays.add(schedule);
			}
		}

		if (this.slot != null) {
			doctorClinicInfo.slot = this.slot;

		}
		if (this.slotmr != null) {
			doctorClinicInfo.slotmr = this.slotmr;
		}

		final Address address = new Address();

		if(this.addressId != null){
			address.id = this.addressId;
		}

		if (this.street != null) {
			address.addressLine1 = this.street;

		}
		if (this.area != null) {
			address.area = this.area;
		}
		if (this.city != null) {
			address.city = this.city;
		}
		if (this.state != null) {
			address.state = State.valueOf(this.state);
		}

		if(this.lat != null){
			address.latitude = this.lat;
		}
		if(this.lng != null){
			address.longitude = this.lng;
		}

		doctorClinicInfo.clinic.address = address;
		if(doctorClinicInfo.clinic.address.id == null){
			doctorClinicInfo.clinic.address.save();
		}
		else{
			doctorClinicInfo.clinic.address.update();
		}
		if(clinic.id == null){
			clinic.save();
		}
		else{
			clinic.update();
		}

		return doctorClinicInfo;
	}
}
