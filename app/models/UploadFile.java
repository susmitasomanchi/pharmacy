package models;

import java.io.File;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToOne;

import play.db.ebean.Model;
import play.db.ebean.Model.Finder;
@Entity
public class UploadFile extends Model{
	@Id
	public Long id;
	public String fileName;
	
	@Lob
	public byte[] fileContent;
	
	public static Model.Finder<Long, UploadFile> find = new Finder<Long, UploadFile>(
			Long.class, UploadFile.class);

}
