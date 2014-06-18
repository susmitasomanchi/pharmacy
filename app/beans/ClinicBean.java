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

	public int fromHrs;

	public int toHrs;

	public List<Day> daysOfWeek=new ArrayList<Day>();

	public int fromHrsMr;

	public int toHrsMr;

	public List<Day> daysOfWeekMr=new ArrayList<Day>();


	public Clinic toEntity(){
		final Clinic clinic = new Clinic();
		clinic.name = this.name;
		return clinic;
	}
	public List<DoctorClinicInfo> toDoctorClinicInfoList()
	{
		final List<DoctorClinicInfo> doctorClinicInfos=new ArrayList<DoctorClinicInfo> ();

		DoctorClinicInfo doctorClinicInfo=new  DoctorClinicInfo();


		List<DayOfTheWeek> dayOfTheWeeks=new ArrayList<DayOfTheWeek>();
		final DayOfTheWeek dayOfTheWeek=new DayOfTheWeek();

		for (final Day day : this.daysOfWeek) {
			dayOfTheWeek.day=day;
			dayOfTheWeeks.add(dayOfTheWeek);
		}
		doctorClinicInfo.fromHrs=this.fromHrs;
		doctorClinicInfo.toHrs=this.toHrs;
		doctorClinicInfo.daysOfWeek=dayOfTheWeeks;
		doctorClinicInfos.add(doctorClinicInfo);

		doctorClinicInfo=null;
		dayOfTheWeeks=null;
		doctorClinicInfo=new DoctorClinicInfo();
		dayOfTheWeeks=new ArrayList<DayOfTheWeek>();

		for (final Day day : this.daysOfWeekMr) {
			dayOfTheWeek.day=day;
			dayOfTheWeeks.add(dayOfTheWeek);
		}

		doctorClinicInfo.fromHrs=this.fromHrsMr;
		doctorClinicInfo.toHrs=this.toHrsMr;
		doctorClinicInfo.daysOfWeek=dayOfTheWeeks;

		doctorClinicInfos.add(doctorClinicInfo);
		return doctorClinicInfos;
	}
}
