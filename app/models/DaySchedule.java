package models;

import javax.persistence.Entity;
import javax.persistence.Id;

import play.db.ebean.Model;

@SuppressWarnings("serial")
@Entity
public class DaySchedule extends BaseEntity {

	@Id
	public Long id;

	public Day day;

	public Integer fromTime;

	public Integer toTime;

	public Role requester;

	@Override
	public boolean equals(final Object schedule) {
		if(!this.day.equals(((DaySchedule)schedule).day)){
			return this.day.equals(((DaySchedule)schedule).day);
		}else if (!this.fromTime.equals(((DaySchedule)schedule).fromTime)) {
			return this.fromTime.equals(((DaySchedule)schedule).fromTime);
		}else if(!this.toTime.equals(((DaySchedule)schedule).toTime)){
			return this.toTime.equals(((DaySchedule)schedule).toTime);
		}else{
			return true;
		}
	};

	public static Model.Finder<Long, DayOfTheWeek> find = new Finder<Long, DayOfTheWeek>(Long.class, DayOfTheWeek.class);

}