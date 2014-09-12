package models.doctor;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;

import models.BaseEntity;
import models.Role;
import play.db.ebean.Model;

@SuppressWarnings("serial")
@Entity
public class DaySchedule extends BaseEntity {

	@Id
	public Long id;

	public Day day;

	public String fromTime;

	public String toTime;

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
	public static List<DaySchedule> getAvailableAppointmentsList( final Long docId) {
		final List<DaySchedule>  list=DaySchedule.find.where().eq("doctorClinicInfo.doctor", Doctor.find.byId(docId)).findList();
		return list;
	}
	public static Model.Finder<Long, DaySchedule> find = new Finder<Long, DaySchedule>(Long.class, DaySchedule.class);

}
