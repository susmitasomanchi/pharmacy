package models.diagnostic;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import models.BaseEntity;
import models.doctor.Prescription;
import play.db.ebean.Model;

@SuppressWarnings("serial")
@Entity
public class DiagnosticOrder extends BaseEntity{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long id;

	@OneToOne
	public Prescription prescription;

	@OneToMany(cascade=CascadeType.ALL)
	public List<DiagnosticReport> diagnosticReportList = new ArrayList<>();

	public DiagnosticOrderStatus diagnosticOrderStatus;

	public Date receivedDate;

	public Date confirmedDate;

	public Date cancelledDate;
	
	
	public static Model.Finder<Long, DiagnosticOrder> find = new Finder<Long, DiagnosticOrder>(
			Long.class, DiagnosticOrder.class);
}
