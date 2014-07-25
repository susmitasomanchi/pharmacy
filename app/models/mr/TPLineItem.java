package models.mr;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import models.BaseEntity;

@Entity
@SuppressWarnings("serial")
public class TPLineItem extends BaseEntity{

	@GeneratedValue(strategy = GenerationType.AUTO)
	@Id
	public Long id;

	public Date month;

	public static Finder<Long, TPLineItem> find = new Finder<Long,TPLineItem>(Long.class,TPLineItem.class);


}
