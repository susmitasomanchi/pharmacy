
package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import play.db.ebean.Model;

@SuppressWarnings("serial")
@Entity
public class Patient extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long id;

	@OneToOne
	public AppUser appUser;

	public String mbno;

	public String date;

	
	public String disease;

	public String appointmentId;

	public String doctorAvailability;

	public String isUrgentPatient;
	
	@ManyToMany(cascade=CascadeType.ALL)
	public List<DiagnosticCentre> diagnosticCenterList=new ArrayList<DiagnosticCentre>();

	public static Model.Finder<Long, Patient> find = new Finder<Long, Patient>(Long.class, Patient.class);

}
