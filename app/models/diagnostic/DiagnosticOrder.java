package models.diagnostic;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import models.BaseEntity;
import models.Patient;
import play.db.ebean.Model;
@Entity
public class DiagnosticOrder extends BaseEntity{
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long id;

	public Patient patient;
	
	public List<DiagnosticReport> diagnosticReportList = new ArrayList<>();
	
	public DiagnosticOrderStatus diagnosticOrderStatus;
	
	public Date receivedDate;
	
	public Date confirmedDate;
	
	public List<DiagnosticTest> diagnosticTest=new ArrayList<>();
	
	public static Model.Finder<Long, DiagnosticOrder> find = new Finder<Long, DiagnosticOrder>(
			Long.class, DiagnosticOrder.class);
	
}
