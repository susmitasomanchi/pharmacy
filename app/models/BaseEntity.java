package models;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.MappedSuperclass;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

import play.Logger;
import play.db.ebean.Model;

import com.avaje.ebean.annotation.CreatedTimestamp;

@SuppressWarnings("serial")
@MappedSuperclass
public abstract class BaseEntity extends Model {


	@NotNull
	@CreatedTimestamp
	public Timestamp createdOn;

	@Version
	public Timestamp lastUpdate;

	/*@Override
	public void save() {
		final Date now = new Date();
		this.createdOn = new Timestamp(now.getTime());
		Logger.info("createdOn: "+this.createdOn);
		super.save();
	}*/
}
