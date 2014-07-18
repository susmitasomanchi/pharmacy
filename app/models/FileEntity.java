package models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

import models.pharmacist.Pharmacy;

@SuppressWarnings("serial")
@Entity
public class FileEntity extends BaseEntity{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long id;

	public String fileName;

	public String mimeType;

	@Lob
	public byte[] byteContent;

	@ManyToOne
	public Pharmacy	 pharmacy;

	public static Finder<Long, FileEntity> find = new Finder<Long, FileEntity>(Long.class, FileEntity.class);

}
