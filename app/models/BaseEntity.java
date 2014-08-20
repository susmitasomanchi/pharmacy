package models;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.MappedSuperclass;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

import com.avaje.ebean.annotation.CreatedTimestamp;

import play.db.ebean.Model;

@SuppressWarnings("serial")
@MappedSuperclass
public abstract class BaseEntity extends Model {


	@NotNull
	@CreatedTimestamp
	Timestamp createdOn;

	@Version
	Timestamp lastUpdate;

	@Override
	public void save() {
		this.createdOn = new Timestamp(new Date().getTime());
		super.save();
	}
}
