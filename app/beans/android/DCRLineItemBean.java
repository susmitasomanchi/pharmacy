package beans.android;

import java.io.Serializable;
import java.util.Date;


@SuppressWarnings("serial")
public class DCRLineItemBean implements Serializable{

	public Long id;

	public Long submitterAppUserId;
	public Long doctorId;
	public Date forDate;

	public Long[] sampleProductIdArray;
	public Long[] sampleProductQtyArray;
	public Long[] samplePromotionProductIdArray;

	public Date inTime;
	public Date outTime;

	public Float pob;

	public String remarks;

	public String latitude;
	public String longitude;

}
