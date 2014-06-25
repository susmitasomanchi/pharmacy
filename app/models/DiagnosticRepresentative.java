package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToOne;

import models.Doctor;

@SuppressWarnings("serial")
@Entity
public class DiagnosticRepresentative extends BaseEntity{

	@GeneratedValue(strategy=GenerationType.AUTO)
	@Id
	public Long id;

	@OneToOne
	public AppUser appUser;
	
	@OneToOne
	public Patient patient;

	public String diagnosticType;
	@Lob
	public byte[] file;
	
	public List<Doctor> doctorList = new ArrayList<Doctor>();

	

	public static Finder<Long, DiagnosticRepresentative> find = new Finder<Long, DiagnosticRepresentative>(Long.class, DiagnosticRepresentative.class);


}
