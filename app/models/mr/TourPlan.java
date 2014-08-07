package models.mr;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import models.BaseEntity;


@SuppressWarnings("serial")
@Entity
public class TourPlan extends BaseEntity{

	@GeneratedValue(strategy=GenerationType.AUTO)
	@Id
	public Long id;

	public Date forMonth;

	@OneToMany(cascade=CascadeType.ALL)
	public List<TPLineItem> tpLineItemList  = new ArrayList<TPLineItem>();

	public Date submitDate;

	public static Finder<Long, TourPlan> find = new Finder<Long, TourPlan>(Long.class, TourPlan.class);
}
