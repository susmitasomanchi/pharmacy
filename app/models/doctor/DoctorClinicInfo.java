package models.doctor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import models.BaseEntity;
import models.Role;
import play.Logger;
import play.db.ebean.Model;
import beans.DoctorClinicInfoBean;

@SuppressWarnings("serial")
@Entity
public class DoctorClinicInfo extends BaseEntity {

	@GeneratedValue(strategy = GenerationType.AUTO)
	@Id
	public Long id;

	@OneToOne
	public Clinic clinic;

	@OneToOne
	public Doctor doctor;

	public Integer slot;

	public Integer	slotmr;

	public boolean active = true;

	@OneToMany(cascade=CascadeType.ALL)
	public List<DaySchedule> scheduleDays = new ArrayList<DaySchedule>();

	public static Model.Finder<Long, DoctorClinicInfo> find = new Finder<Long, DoctorClinicInfo>(Long.class, DoctorClinicInfo.class);

	public Map<String, String> getScheduleMap(){
		final Map<String, String> dayScheduleMap = new HashMap<String, String>();
		for (final Day day : Day.values()) {
			final List<DaySchedule> daysSchedules = DaySchedule.find.where().eq("doctor_clinic_info_id", this.id).eq("day", day).findList();
			if(daysSchedules.size()>0){
				dayScheduleMap.put(day.toString(), daysSchedules.get(0).fromTime+"-"+daysSchedules.get(0).toTime);
			}
			else{
				dayScheduleMap.put(day.toString(), null);
			}
		}
		return dayScheduleMap;
	}

	public DoctorClinicInfoBean toBean(){
		final DoctorClinicInfoBean bean = new DoctorClinicInfoBean();

		if(this.id != null) {
			bean.id = this.id;
		}

		if(this.doctor != null) {
			bean.doctorId = this.doctor.id;
		}

		if(this.id != null) {
			bean.id = this.id;
		}

		if(this.clinic != null) {
			bean.clinicId = this.clinic.id;
			bean.name= this.clinic.name;
			bean.contactPersonName=this.clinic.contactPersonName;
			bean.contactNo=this.clinic.contactNo;
		}
		final List<String> fromHrs = new ArrayList<String>();

		final List<String> toHrs = new ArrayList<String>();

		final List<String> fromHrsMr = new ArrayList<String>();

		final List<String> toHrsMr = new ArrayList<String>();

		final List<String> daysOfWeek	= new ArrayList<String>();

		final List<String> daysOfWeekMr	= new ArrayList<String>();


		if(this.scheduleDays.size()!=0){

			for (final DaySchedule  schedule : this.scheduleDays) {
				if(schedule.requester.equals(Role.PATIENT)){
					fromHrs.add(schedule.fromTime);
					toHrs.add(schedule.toTime);
					daysOfWeek.add(schedule.day.toString());

				}else{
					fromHrsMr.add(schedule.fromTime);
					toHrsMr.add(schedule.toTime);
					daysOfWeekMr.add(schedule.day.toString());
				}
			}

		}

		bean.fromHrs=fromHrs;
		bean.toHrs=toHrs;
		bean.fromHrsMr=fromHrsMr;
		bean.toHrsMr=toHrsMr;
		bean.daysOfWeek=daysOfWeek;
		bean.daysOfWeekMr=daysOfWeekMr;
		if(this.slot!=null){
			bean.slot=this.slot;

		}
		if(this.slot!=null){
			bean.slotmr=this.slotmr;
		}
		Logger.info(this.clinic.address+"****");
		if(this.clinic.address != null){
			bean.addressId = this.clinic.address.id;
		}
		if(this.clinic.address.addrressLine1!=null){
			bean.street=this.clinic.address.addrressLine1;
		}
		if(this.clinic.address.area!=null){
			bean.area=this.clinic.address.area;
		}
		if(this.clinic.address.state!=null){
			bean.state=this.clinic.address.state.toString();
		}
		if(this.clinic.address.city!=null){
			bean.city=this.clinic.address.city;
		}

		bean.lat = this.clinic.address.latitude;

		bean.lng = this.clinic.address.longitude;

		return bean;

	}

}