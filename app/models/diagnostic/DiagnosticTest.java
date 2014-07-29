package models.diagnostic;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import models.BaseEntity;
import play.db.ebean.Model;


@SuppressWarnings("serial")
@Entity
public class DiagnosticTest extends BaseEntity{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long id;

	public String name;

	@Column(columnDefinition="TEXT")
	public String description;

	public static Model.Finder<Long, DiagnosticTest> find = new Finder<Long, DiagnosticTest>(Long.class, DiagnosticTest.class);

}
