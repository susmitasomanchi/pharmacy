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

	public Integer fromHrs;

	public Integer toHrs;

	public Integer toHrsMr;

	public Integer fromHrsMr;


	@OneToOne
	DoctorAssistant assistant;

	@OneToMany(cascade=CascadeType.ALL)
	public List<DayOfTheWeek> daysOfWeek= new ArrayList<DayOfTheWeek>();

	@OneToMany(cascade=CascadeType.ALL)
	public List<DayOfTheWeek> daysOfWeekMr= new ArrayList<DayOfTheWeek>();

	public static Model.Finder<Long, DoctorClinicInfo> find = new Finder<Long, DoctorClinicInfo>(Long.class, DoctorClinicInfo.class);

	public ClinicBean toBean(){
		final ClinicBean bean=new ClinicBean();

		if(this.id != null) {
			bean.id= this.id;
		}
		if(this.clinic != null) {
			bean.name= this.clinic.name;
		}
		if(this.toHrs != null) {
			bean.toHrs= this.toHrs;
		}
		if(this.fromHrs != null) {
			bean.fromHrs= this.fromHrs;
		}
		if(this.toHrsMr != null) {
			bean.toHrsMr= this.toHrsMr;
		}
		if(this.fromHrsMr != null) {
			bean.fromHrsMr= this.fromHrsMr;
		}
		final List<Day> days=new ArrayList<Day>();

		for (final DayOfTheWeek dayOfTheWeek : this.daysOfWeek) {
			days.add(dayOfTheWeek.day);
		}
		bean.daysOfWeek=days;

		final List<Day> daysmr=new ArrayList<Day>();
		for (final DayOfTheWeek dayOfTheWeek : this.daysOfWeekMr) {
			daysmr.add(dayOfTheWeek.day);
		}
		bean.daysOfWeekMr=daysmr;

		return bean;

	}

}
