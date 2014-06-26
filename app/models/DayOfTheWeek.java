package models;

import javax.persistence.Entity;
import javax.persistence.Id;

import play.db.ebean.Model;

@Entity
public class DayOfTheWeek extends BaseEntity {

	@Id
	public Long id;

	public Day day;


	public static Model.Finder<Long, DayOfTheWeek> find = new Finder<Long, DayOfTheWeek>(Long.class, DayOfTheWeek.class);


	@Override
	public boolean equals(final Object arg0) {
		return this.day.equals(((DayOfTheWeek)arg0).day);

	}


}
