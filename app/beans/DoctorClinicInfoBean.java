package beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import models.Address;
import models.Locality;
import models.PrimaryCity;
import models.Role;
import models.State;
import models.clinic.ClinicUser;
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

	public Long clinicUserId;

	public Long localityId;

	public String name;

	public String contactPersonName;

	public String contactNo;

	public Long addressId;

	public String street;

	public String area;

	public Long cityId;

	public String state;

	public String fetchpin;

	public String pinCode;

	public List<String> fromHrs = new ArrayList<String>();

	public List<String> toHrs = new ArrayList<String>();

	public List<String> fromHrsMR = new ArrayList<String>();

	public List<String> toHrsMR = new ArrayList<String>();

	public List<String> daysOfWeek = new ArrayList<String>();

	public List<String> daysOfWeekMr = new ArrayList<String>();

	public String lat;

	public String lng;

	public Integer slot;

	public Integer slotMR;

	public ClinicUser clinicAdminstrator;

	public DoctorClinicInfo toDoctorClinicInfo() {
		Logger.info("hello entering");
		DoctorClinicInfo doctorClinicInfo;
		if (this.id != null) {
			Logger.info(this.id+"idssssssssss");
			doctorClinicInfo = DoctorClinicInfo.find.byId(this.id);
		}
		else{
			doctorClinicInfo = new DoctorClinicInfo();
		}

		Clinic clinic;
		if(this.clinicId != null){
			Logger.info("clinic idsssssssss"+this.clinicId);
			clinic = Clinic.find.byId(this.clinicId);
		}
		else{
			clinic = new Clinic();
		}
		if(this.name != null){
			clinic.name = this.name;

		}
		if(this.contactNo != null){
			clinic.contactNo = this.contactNo;
		}
		if(this.contactPersonName != null){
			clinic.contactPersonName = this.contactPersonName;
		}
		if(this.clinicUserId != null){
			clinic.clinicAdminstrator = ClinicUser.find.byId(this.clinicUserId);
			Logger.info(clinic.clinicAdminstrator.appUser.name);
		}
		if(this.doctorId != null){
			doctorClinicInfo.doctor = Doctor.find.byId(this.doctorId);
		}
		if (this.cityId != null) {
			clinic.primaryCity = PrimaryCity.find.byId(this.cityId);
		}

		//Logger.info("" + this.id);
		//Logger.info("name null");



		doctorClinicInfo.clinic = clinic;

		//Logger.info(doctorClinicInfo.clinic.name);
		//Logger.info("from hrs" + this.fromHrs.size());
		//Logger.info("" + this.toHrs);
		//Logger.info(this.daysOfWeek.size() + "");

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

		//Logger.info(this.daysOfWeekMr.size() + "mr size");

		for (int index = 0; index < this.daysOfWeekMr.size(); index++) {
			if (this.daysOfWeekMr.get(index) != null) {
				final DaySchedule schedule = new DaySchedule();
				schedule.fromTime = this.fromHrsMR.get(index);
				schedule.toTime = this.toHrsMR.get(index);
				schedule.requester = Role.MR;
				schedule.day = Day.valueOf(this.daysOfWeekMr.get(index));
				doctorClinicInfo.scheduleDays.add(schedule);
			}
		}

		if (this.slot != null) {
			doctorClinicInfo.slot = this.slot;

		}
		if (this.slotMR != null) {
			doctorClinicInfo.slotMR = this.slotMR;
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
		if (this.state != null && !this.state.trim().isEmpty()) {
			address.state = State.valueOf(this.state);
		}

		if(this.lat != null){
			address.latitude = this.lat;
		}
		if(this.lng != null){
			address.longitude = this.lng;
		}

		if(this.fetchpin != null){
			address.fetchedPinCode = this.fetchpin;
		}
		if(this.pinCode != null){
			address.pinCode = this.pinCode;
		}
		if(this.localityId != null){
			address.locality = Locality.find.byId(this.localityId);
		}
		if (this.cityId != null) {
			address.primaryCity = PrimaryCity.find.byId(this.cityId);
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
		Logger.info("hello exiting");
		return doctorClinicInfo;
	}
}
