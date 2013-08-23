/**
 * 
 */
package com.wxxr.mobile.web.grabber.test;

import java.util.regex.Pattern;

import com.wxxr.mobile.web.grabber.api.IGrabberServiceContext;
import com.wxxr.mobile.web.grabber.common.AbstractPageGrabbingTask;
import com.wxxr.mobile.web.grabber.model.WebURL;

/**
 * @author neillin
 *
 */
public class TestGrabbingTask extends AbstractPageGrabbingTask {

	private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|bmp|gif|jpe?g|png|tiff?|pdf))$");
	private Integer seqNo;

	@Override
	public boolean shouldVisit(WebURL url) {
		String href = url.getURL().toLowerCase();
		if (FILTERS.matcher(href).matches()) {
			return true;
		}
		return false;
	}


	public Integer getSeqNo(){
		return seqNo;
	}
	
	/* (non-Javadoc)
	 * @see com.wxxr.mobile.web.grabber.common.AbstractPageGrabbingTask#init(com.wxxr.mobile.web.grabber.api.IGrabberServiceContext, java.lang.String, java.lang.Object)
	 */
	@Override
	public void init(IGrabberServiceContext context, String url,
			Object customData) {
		if(!(customData instanceof Integer)){
			throw new IllegalArgumentException("Invalid sequence number :"+customData);
		}
		this.seqNo = (Integer)customData;
		super.init(context, url, customData);
	}

}
