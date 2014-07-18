package utils;

import java.util.List;
import models.mr.MedicalRepresentative;

public class Hierarchy {

public static MedicalRepresentative getUpper(MedicalRepresentative mr)
{
	//MedicalRepresentative medicalrepresentative = MedicalRepresentative.find.where().eq("id", mr).findUnique();
	if(mr.manager!=null)
	{
	return mr.manager;
	}
	
	else
		
	return null;
}

public static List<MedicalRepresentative> getSubordinates(MedicalRepresentative mr)
{
	
	List<MedicalRepresentative> mrlist=MedicalRepresentative.find.where().eq("manager", mr).findList();	
	return mrlist;	
	
}

}

