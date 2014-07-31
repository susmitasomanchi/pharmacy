package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;

import models.diagnostic.MasterDiagnosticTest;
import play.db.ebean.Model;
import play.db.ebean.Model.Finder;

@Entity
public class DoctorsPrescription extends BaseEntity{
	@Id
	public Long doctorsPrescriptionId;
	
	public List<MasterDiagnosticTest> diagnosticTestList=new ArrayList<>();
	
	public static Model.Finder<Long, DoctorsPrescription> find = new Finder<Long, DoctorsPrescription>(
			Long.class, DoctorsPrescription.class);
}
