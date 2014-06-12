package models;

import javax.persistence.Entity;

import play.db.ebean.Model;

@SuppressWarnings("serial")
@Entity
public class PrescribedDiagnosticTest extends BaseEntity{


	String remarks;

	public static Model.Finder<Long, PrescribedDiagnosticTest> find = new Finder<Long, PrescribedDiagnosticTest>(Long.class, PrescribedDiagnosticTest.class);
}
