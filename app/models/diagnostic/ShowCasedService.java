package models.diagnostic;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import models.BaseEntity;
import models.FileEntity;

@SuppressWarnings("serial")
@Entity
public class ShowCasedService extends BaseEntity{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long id;

	public String name;

	@Column(columnDefinition="TEXT")
	public String description;

	@ManyToMany(cascade = CascadeType.ALL)
	public List<FileEntity> showcasedImagesList = new ArrayList<FileEntity>();

	public Double cost;

	public static Finder<Long, ShowCasedService> find = new Finder<Long, ShowCasedService>(Long.class, ShowCasedService.class);

}
