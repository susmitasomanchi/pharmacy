package beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import models.Clinic;
import models.Day;
import models.DaySchedule;
import models.DoctorClinicInfo;
import models.Role;
import play.Logger;
import play.data.validation.Constraints.Required;

@SuppressWarnings("serial")
public class ClinicBean implements Serializable{


	public Long id;

	@Required
	public String name;


	public List<Integer> fromHrs = new ArrayList<Integer>();

	public List<Integer> toHrs = new ArrayList<Integer>();

	public List<Integer> fromHrsMr = new ArrayList<Integer>();

	public List<Integer> toHrsMr = new ArrayList<Integer>();

	public List<String> daysOfWeek	= new ArrayList<String>();

	public List<String> daysOfWeekMr	= new ArrayList<String>();

	public int slot=1;

	public int slotmr=5;

	public DoctorClinicInfo toDoctorClinicInfo(){

		final DoctorClinicInfo doctorClinicInfo = new  DoctorClinicInfo();

		if(this.id != null) {
			doctorClinicInfo.id= this.id;
		}

		if(this.name != null) {
			final Clinic clinic = new Clinic();
			clinic.name = this.name;
			doctorClinicInfo.clinic= clinic;
			clinic.save();
		}
		Logger.info(doctorClinicInfo.clinic.name);
		Logger.info(this.daysOfWeek.size()+"");
		for(int index=0;index<this.daysOfWeek.size();index++){

			if(this.daysOfWeek.get(index)!= null){
				final DaySchedule schedule = new DaySchedule();
				schedule.fromTime=this.fromHrs.get(index);
				schedule.toTime=this.toHrs.get(index);
				schedule.day=Day.valueOf(this.daysOfWeek.get(index));
				Logger.info(schedule.day.toString());
				schedule.requester=Role.PATIENT;
				doctorClinicInfo.schedulDays.add(schedule);
			}
		}


		for(int index=0;index<this.daysOfWeekMr.size();index++) {

			final DaySchedule schedule = new DaySchedule();
			schedule.fromTime=this.fromHrsMr.get(index);
			schedule.toTime=this.toHrsMr.get(index);
			schedule.requester=Role.MR;
			schedule.day=Day.valueOf(this.daysOfWeekMr.get(index));
			doctorClinicInfo.schedulDays.add(schedule);

		}

		doctorClinicInfo.slot=this.slot;
		doctorClinicInfo.slotmr=this.slotmr;


		return doctorClinicInfo;
	}
}
