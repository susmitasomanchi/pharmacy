package models;

import java.sql.Timestamp;

import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

import play.db.ebean.Model;

@SuppressWarnings("serial")
@MappedSuperclass
public abstract class BaseEntity extends Model {

	@Version
	Timestamp lastUpdate;

}
