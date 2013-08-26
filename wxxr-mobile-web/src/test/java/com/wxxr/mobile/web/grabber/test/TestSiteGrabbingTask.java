/**
 * 
 */
package com.wxxr.mobile.web.grabber.test;

import java.util.regex.Pattern;

import com.wxxr.mobile.web.grabber.api.IGrabberServiceContext;
import com.wxxr.mobile.web.grabber.common.AbstractSiteGrabbingTask;
import com.wxxr.mobile.web.grabber.model.WebURL;

/**
 * @author neillin
 *
 */
public class TestSiteGrabbingTask extends AbstractSiteGrabbingTask {

	private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|bmp|gif|jpe?g|png|tiff?|pdf|htm|html|xhtml))$");
	private String domain;
	private Integer siteId;

	@Override
	public boolean shouldVisit(WebURL url) {
		String href = url.getURL().toLowerCase();
		if ((FILTERS.matcher(href).matches())&&((url.getDomain()+url.getPath()).startsWith(domain))){
			return true;
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.web.grabber.common.AbstractPageGrabbingTask#init(com.wxxr.mobile.web.grabber.api.IGrabberServiceContext, java.lang.String, java.lang.Object)
	 */
	@Override
	public void init(IGrabberServiceContext context, String url,
			Object customData) {
		if(!(customData instanceof String)){
			throw new IllegalArgumentException("Invalid domain name :"+customData);
		}
		this.domain = (String)customData;
		setMaxDepthOfCrawling(1000);
		super.init(context, url, customData);
	}

	public String getDomain() {
		return this.domain;
	}

	/**
	 * @return the siteId
	 */
	public Integer getSiteId() {
		if(getParentTask() != null){
			return ((TestSiteGrabbingTask)getParentTask()).getSiteId();
		}else{
			return siteId;
		}
	}

	/**
	 * @param siteId the siteId to set
	 */
	public void setSiteId(Integer siteId) {
		this.siteId = siteId;
	}

}
