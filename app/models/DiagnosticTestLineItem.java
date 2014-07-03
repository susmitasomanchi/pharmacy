package models;

import javax.persistence.Entity;

import models.diagnostic.DiagnosticTest;
import play.db.ebean.Model;

@SuppressWarnings("serial")
@Entity
public class DiagnosticTestLineItem extends BaseEntity{

	public DiagnosticTest diagnosticTest;
	public String remarks;

	public static Model.Finder<Long, DiagnosticTestLineItem> find = new Finder<Long, DiagnosticTestLineItem>(Long.class, DiagnosticTestLineItem.class);
}
