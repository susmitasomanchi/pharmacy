package beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import models.Clinic;
import models.Day;
import models.DayOfTheWeek;
import models.DoctorClinicInfo;
import play.data.validation.Constraints.Max;
import play.data.validation.Constraints.Min;

@SuppressWarnings("serial")
public class ClinicBean implements Serializable{


	public Long id;

	public String name;

	@Min(1)
	public Integer fromHrs;

	@Max(24)
	public Integer toHrs;

	public List<Day> daysOfWeek=new ArrayList<Day>();

	@Min(1)
	public Integer toHrsMr;

	@Max(24)
	public Integer fromHrsMr;


	public List<Day> daysOfWeekMr=new ArrayList<Day>();



	public DoctorClinicInfo toDoctorClinicInfoList()
	{

		final DoctorClinicInfo doctorClinicInfo=new  DoctorClinicInfo();

		if(this.id != null) {
			doctorClinicInfo.id= this.id;
		}
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

		if(this.name != null) {
			final Clinic clinic = new Clinic();
			clinic.name = this.name;
			doctorClinicInfo.clinic= clinic;

		}

		final List<DayOfTheWeek> dayOfTheWeeks=new ArrayList<DayOfTheWeek>();
		final DayOfTheWeek dayOfTheWeek=new DayOfTheWeek();


		for (final Day day : this.daysOfWeek) {
			dayOfTheWeek.day=day;
			dayOfTheWeeks.add(dayOfTheWeek);
		}
		doctorClinicInfo.daysOfWeek=dayOfTheWeeks;


		final List<DayOfTheWeek> dayOfTheWeeksMr=new ArrayList<DayOfTheWeek>();
		final DayOfTheWeek dayOfTheWeekMr=new DayOfTheWeek();

		for (final Day day : this.daysOfWeekMr) {
			dayOfTheWeekMr.day=day;
			dayOfTheWeeksMr.add(dayOfTheWeekMr);
		}
		doctorClinicInfo.daysOfWeekMr=dayOfTheWeeksMr;



		return doctorClinicInfo;
	}
}
