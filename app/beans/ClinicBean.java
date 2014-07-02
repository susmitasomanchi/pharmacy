package beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import models.Clinic;
import models.Day;
import models.DayOfTheWeek;
import models.DoctorClinicInfo;

@SuppressWarnings("serial")
public class ClinicBean implements Serializable{


	public Long id;

	public String name;

	public Integer fromHrs;

	public Integer toHrs;

	public List<Day> daysOfWeek=new ArrayList<Day>();

	public Integer fromHrsMr;

	public Integer toHrsMr;

	public List<Day> daysOfWeekMr=new ArrayList<Day>();


	public Clinic toEntity(){
		final Clinic clinic = new Clinic();
		clinic.name = this.name;
		return clinic;
	}
	public DoctorClinicInfo toDoctorClinicInfoList()
	{

		final DoctorClinicInfo doctorClinicInfo=new  DoctorClinicInfo();


		if(this.toHrs != null) {
			doctorClinicInfo.toHrs= this.toHrs;
		}
		if(this.fromHrs != null) {
			doctorClinicInfo.fromHrs= this.fromHrs;
		}
		if(this.toHrsMr != null) {
			doctorClinicInfo.toHrsMr= this.toHrsMr;
		}
		if(this.fromHrsMr != null) {
			doctorClinicInfo.fromHrsMr= this.fromHrsMr;
		}

		List<DayOfTheWeek> dayOfTheWeeks=new ArrayList<DayOfTheWeek>();
		DayOfTheWeek dayOfTheWeek=new DayOfTheWeek();


		for (final Day day : this.daysOfWeek) {
			dayOfTheWeek.day=day;
			dayOfTheWeeks.add(dayOfTheWeek);
		}
		doctorClinicInfo.daysOfWeek=dayOfTheWeeks;

		dayOfTheWeeks=null;
		dayOfTheWeek=null;
		dayOfTheWeeks=new ArrayList<DayOfTheWeek>();
		dayOfTheWeek=new DayOfTheWeek();
		for (final Day day : this.daysOfWeek) {
			dayOfTheWeek.day=day;
			dayOfTheWeeks.add(dayOfTheWeek);
		}
		doctorClinicInfo.daysOfWeekMr=dayOfTheWeeks;



		return doctorClinicInfo;
	}
}
