package models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;

@SuppressWarnings("serial")
@Entity
public class FileEntity extends BaseEntity{
	@Id
	public Long id;

	public String fileName;

	public String mimeType;

	@Lob
	public byte[] byteContent;

	public static Finder<Long, FileEntity> find = new Finder<Long, FileEntity>(Long.class, FileEntity.class);

}
