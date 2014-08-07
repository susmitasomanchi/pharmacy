package models.mr;

import javax.persistence.Embeddable;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import play.db.ebean.Model;

@SuppressWarnings("serial")
@Embeddable
public class TourPlanConfiguration extends Model{

	@GeneratedValue(strategy=GenerationType.AUTO)
	@Id
	public Long id;

	public boolean isDoctorVisible;

	public boolean isSampleVisible;

	public boolean isPromotionVisible;

	public boolean isPobVisible;

	public boolean isRemarksVisible;

	//public static Finder<Long, TourPlanConfiguration> find = new Finder<Long, TourPlanConfiguration>(Long.class, TourPlanConfiguration.class);

}
