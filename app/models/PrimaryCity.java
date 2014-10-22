package models;

import java.util.HashMap;

import java.util.List;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import play.db.ebean.Model;
import utils.Constants;


@SuppressWarnings("serial")
@Entity
public class PrimaryCity extends BaseEntity{

	@GeneratedValue(strategy=GenerationType.AUTO)
	@Id
	public Long id;

	public String name;

	public State state;

	public static Model.Finder<Long, PrimaryCity> find = new Model.Finder<Long, PrimaryCity>(Long.class, PrimaryCity.class);

	public static List<PrimaryCity> getCitiesStateWise(){
		return find.where().orderBy("state").findList();
	}

	public static List<PrimaryCity> getAllInOrder(){
		return find.where().orderBy("name").findList();
	}

	public static Map<String,String> getAllPrimaryCities(){
		final Map<String,String> primatyCityMap = new HashMap<String,String>();
		for (final PrimaryCity primaryCity :  models.PrimaryCity.find.all()) {
			primatyCityMap.put(primaryCity.id.toString(), primaryCity.name);
		}
		return primatyCityMap;
	}


}
