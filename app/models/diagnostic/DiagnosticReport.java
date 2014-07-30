package models.diagnostic;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToOne;

import models.BaseEntity;
import play.db.ebean.Model;

@SuppressWarnings("serial")
@Entity
public class DiagnosticReport extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long id;

	public String fileName;

	@Lob
	public byte[] fileContent;
    
	@OneToOne
	public MasterDiagnosticTest masterDiagnosticTest;

	public DiagnosticReportStatus reportStatus = DiagnosticReportStatus.SAMPLE_NOT_COLLECTED;

	public Date sampleCollectedDate;

	public Date reportGeneratedDate;

	public static Model.Finder<Long, DiagnosticReport> find = new Finder<Long, DiagnosticReport>(
			Long.class, DiagnosticReport.class);

}
