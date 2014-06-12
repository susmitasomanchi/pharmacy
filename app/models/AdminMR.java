package models;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Id;

@SuppressWarnings("serial")
@Entity
public class AdminMR extends BaseEntity{
	
	@Id
	Long id;
	String name;
	Address address;
	List<MedicalRepresentative> mrList = new ArrayList<MedicalRepresentative>();

}
