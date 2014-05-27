package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import play.data.validation.Constraints.Required;

@Entity
public class Patients extends AppUser {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long id;
	
	
	@Required
	public String disease;
	
	@Id
	public Long appointmentId;
	
	@Required
	public String doctorAvailability;
	
	@Required
	public String isUrgentPatient;
	
}
