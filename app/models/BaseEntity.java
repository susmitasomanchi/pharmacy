package models;

import java.sql.Timestamp;

import javax.persistence.MappedSuperclass;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

import play.db.ebean.Model;

import com.avaje.ebean.annotation.CreatedTimestamp;

@SuppressWarnings("serial")
@MappedSuperclass
public abstract class BaseEntity extends Model {


	@NotNull
	@CreatedTimestamp
	Timestamp createdOn;

	@Version
	Timestamp lastUpdate;

}
