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

import models.BaseEntity;
import models.DaySchedule;
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


		if(this.schedulDays.size()!=0){/*
			for (final SchedulDay	 schedulDay : this.schedulDays) {
				if(schedulDay.dayStatus.equals(DayStatus.mr)){
					bean.daysOfWeekMr.add(schedulDay.day);
				}else if (schedulDay.dayStatus.equals(DayStatus.patient)) {
					bean.daysOfWeek.add(schedulDay.day);
				}
			}
		 */}
		return bean;

	}

}
