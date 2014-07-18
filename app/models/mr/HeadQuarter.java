package models.mr;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import models.BaseEntity;
import models.State;

@SuppressWarnings("serial")
@Entity
public class HeadQuarter extends BaseEntity{

	@GeneratedValue(strategy=GenerationType.AUTO)
	@Id
	public Long id;

	@OneToOne
	public State state;

	public String name;

	public static Finder<Long, HeadQuarter> find = new Finder<Long, HeadQuarter>(Long.class, HeadQuarter.class);
	/**
	 * this method returns state from which HeadQuarter belongs
	 * 
	 * */
	public static State getState(final Long id){
		return HeadQuarter.find.byId(id).state;
	}
}
