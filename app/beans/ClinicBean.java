package beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import models.Clinic;
import models.Day;
import models.DayOfTheWeek;

@SuppressWarnings("serial")
public class ClinicBean implements Serializable{


	public Long id;

	public String name;

	public int fromHrs;

	public int toHrs;

	public List<Day> daysOfWeek=new ArrayList<Day>();

	public Clinic toEntity(){
		final Clinic clinic = new Clinic();
		clinic.name = this.name;
		return clinic;
	}
	public List<DayOfTheWeek> toDayOfTheWeek(){
	
		final List<DayOfTheWeek> dayOfTheWeeks=new ArrayList<DayOfTheWeek>();
		for (final Day day : this.daysOfWeek) {
			final DayOfTheWeek dayOfTheWeek=new DayOfTheWeek();
			dayOfTheWeek.day=day;
			dayOfTheWeeks.add(dayOfTheWeek);
		}
		return dayOfTheWeeks;
	}
}
