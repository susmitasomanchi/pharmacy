package models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;

import play.db.ebean.Model;
import play.db.ebean.Model.Finder;

@Entity
public class DiagnosticReport extends BaseEntity{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long diagReportId;
	
	public String fileName;
	
	@Lob
	public byte[] fileContent;
	
	public DiagnosticTest diagnosticTest;
	
	public static Model.Finder<Long, DiagnosticReport> find = new Finder<Long, DiagnosticReport>(
			Long.class, DiagnosticReport.class);

}
