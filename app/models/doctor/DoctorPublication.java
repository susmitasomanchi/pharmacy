package models.doctor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;

import models.BaseEntity;

@SuppressWarnings("serial")
@Entity
public class DoctorPublication extends BaseEntity{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public  Long id;

	public String name;

	public String articleFor;

	public String year;

	public Integer position;

	@Column(columnDefinition="TEXT")
	public String briefDescription;

	@Column(columnDefinition="TEXT")
	public String content;

	@Lob
	public byte[] image;

	@Lob
	public byte[] file;

	public static Finder<Long, DoctorPublication> find = new Finder<Long, DoctorPublication>(Long.class, DoctorPublication.class);

}
