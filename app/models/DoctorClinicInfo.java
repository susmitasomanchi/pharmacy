package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

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
		}
		final List<Integer> fromHrs = new ArrayList<Integer>();

		final List<Integer> toHrs = new ArrayList<Integer>();

		final List<Integer> fromHrsMr = new ArrayList<Integer>();

		final List<Integer> toHrsMr = new ArrayList<Integer>();

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

		bean.slot=this.slot;
		bean.slotmr=this.slotmr;

		return bean;

	}

}
