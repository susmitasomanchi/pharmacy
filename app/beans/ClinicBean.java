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
import models.doctor.DoctorClinicInfo;
import play.Logger;

@SuppressWarnings("serial")
public class ClinicBean implements Serializable {

	public Long id;

	public String name;

	public String contactPersonName;

	public String contactNo;

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
		Logger.info("" + this.id);
		Logger.info("name null");

		final Clinic clinic = new Clinic();
		clinic.name = this.name;
		clinic.contactNo = this.contactNo;
		clinic.contactPersonName = this.contactPersonName;
		doctorClinicInfo.clinic = clinic;
		clinic.save();

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
				doctorClinicInfo.schedulDays.add(schedule);
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
				doctorClinicInfo.schedulDays.add(schedule);
			}
		}

		if (this.slot != null) {
			doctorClinicInfo.slot = this.slot;

		}
		if (this.slotmr != null) {
			doctorClinicInfo.slotmr = this.slotmr;
		}

		final Address address = new Address();
		if (this.street != null) {
			address.addrressLine1 = this.street;

		}
		if (this.area != null) {
			address.addrressLine2 = this.area;
		}
		if (this.city != null) {
			address.city = this.city;
		}
		if (this.state != null) {
			address.state = State.valueOf(this.state);
		}

		address.latitude = this.lat;
		address.longitude = this.lng;
		doctorClinicInfo.address = address;
		doctorClinicInfo.address.save();
		Logger.info("" + this.lat);
		return doctorClinicInfo;
	}
}
