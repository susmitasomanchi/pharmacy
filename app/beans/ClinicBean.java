package beans;

import java.io.Serializable;

import models.Clinic;

@SuppressWarnings("serial")
public class ClinicBean implements Serializable{


	public Long id;

	public String name;

	public int fromHrs;

	public int toHrs;


	public Clinic toEntity(){
		final Clinic clinic = new Clinic();
		clinic.name = this.name;
		return clinic;
	}

}
