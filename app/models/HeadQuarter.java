package models;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

@SuppressWarnings("serial")
@Entity
public class HeadQuarter extends BaseEntity{
	
	@OneToOne
	public State state;
	
	public String name;
	//List<FieldStation> fieldstation = new ArrayList<FieldStation>();

}
