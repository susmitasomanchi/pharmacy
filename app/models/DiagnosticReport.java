package models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;

import play.db.ebean.Model;

@Entity
public class DiagnosticReport extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long id;

	public String fileName;

	@Lob
	public byte[] fileContent;

	public DiagnosticTest diagnosticTest;

	public DiagnosticOrderStatus reportStatus = DiagnosticOrderStatus.SAMPLE_NOT_COLLECTED;

	public Date sampleCollectionDate;

	public Date reportGenerationDate;

	public static Model.Finder<Long, DiagnosticReport> find = new Finder<Long, DiagnosticReport>(
			Long.class, DiagnosticReport.class);

}
