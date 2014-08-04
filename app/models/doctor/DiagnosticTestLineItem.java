package models.doctor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import models.BaseEntity;
import models.MasterDiagnosticTest;
import play.db.ebean.Model;

@SuppressWarnings("serial")
@Entity
public class DiagnosticTestLineItem extends BaseEntity{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public  Long id;

	@OneToOne
	public MasterDiagnosticTest masterDiagnosticTest;

	public String fullNameOfDiagnosticTest;

	@Column(columnDefinition="TEXT")
	public String remarks;

	public static Model.Finder<Long, DiagnosticTestLineItem> find = new Finder<Long, DiagnosticTestLineItem>(Long.class, DiagnosticTestLineItem.class);
}
