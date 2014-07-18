package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;

import models.diagnostic.DiagnosticTest;
import play.db.ebean.Model;

@SuppressWarnings("serial")
@Entity
public class DoctorsPrescription extends BaseEntity{
	@Id
	public Long doctorsPrescriptionId;

	public List<DiagnosticTest> diagnosticTestList=new ArrayList<DiagnosticTest>();

	public static Model.Finder<Long, DoctorsPrescription> find = new Finder<Long, DoctorsPrescription>(
			Long.class, DoctorsPrescription.class);
}
