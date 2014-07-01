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

	public DayStatus dayStatus;



	public static Model.Finder<Long, DayOfTheWeek> find = new Finder<Long, DayOfTheWeek>(Long.class, DayOfTheWeek.class);

}