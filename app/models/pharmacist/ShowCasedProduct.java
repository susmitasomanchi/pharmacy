package models.pharmacist;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import models.BaseEntity;
import models.FileEntity;
@SuppressWarnings("serial")
@Entity
public class ShowCasedProduct extends BaseEntity{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long id;

	public String name;

	@Column(columnDefinition="TEXT")
	public String description;



	public List<FileEntity> showcasedImagesList = new ArrayList<FileEntity>();

	public Double mrp;


	public static Finder<Long, ShowCasedProduct> find = new Finder<Long, ShowCasedProduct>(Long.class, ShowCasedProduct.class);

}
