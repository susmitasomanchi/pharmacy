package models.doctor;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import models.Address;
import models.BaseEntity;
import models.Role;
import play.Logger;
import play.db.ebean.Model;
import beans.ClinicBean;

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

	public boolean  active=true;

	@OneToOne
	public Address address;
	@OneToMany(cascade=CascadeType.ALL)
	public List<DaySchedule> schedulDays = new ArrayList<DaySchedule>();


	public static Model.Finder<Long, DoctorClinicInfo> find = new Finder<Long, DoctorClinicInfo>(Long.class, DoctorClinicInfo.class);

	public ClinicBean toBean(){
		final ClinicBean bean=new ClinicBean();

		if(this.id != null) {
			bean.id= this.id;
		}
		if(this.clinic != null) {
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


		if(this.schedulDays.size()!=0){

			for (final DaySchedule  schedule : this.schedulDays) {
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
		Logger.info(this.address+"****");
		if(this.address.addrressLine1!=null){
			bean.street=this.address.addrressLine1;
		}
		if(this.address.addrressLine2!=null){
			bean.area=this.address.addrressLine2;
		}
		if(this.address.addrressLine1!=null){
			bean.state=this.address.state.toString();
		}
		if(this.address.city!=null){
			bean.city=this.address.city;
		}

		bean.lat=this.address.lat;

		bean.lng=this.address.lng;


		return bean;

	}

}
