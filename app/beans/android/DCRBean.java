package beans.android;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DCRBean {

	public Long id;

	public Date forDate;

	public Long submitterAppUserId;

	public String dcrStatus;

	public Date submittedDate;

	public Date responseOn;

	public Date reOpenedDate;

	public List<Long> dcrLineItemIdList = new ArrayList<Long>();

}
