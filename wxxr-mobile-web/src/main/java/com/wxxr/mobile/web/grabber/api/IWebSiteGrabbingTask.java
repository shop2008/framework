/**
 * 
 */
package com.wxxr.mobile.web.grabber.api;

import com.wxxr.mobile.web.grabber.model.WebURL;

/**
 * @author neillin
 *
 */
public interface IWebSiteGrabbingTask extends IWebPageGrabbingTask {
	void schedulePageGrabbingTask(IWebSiteGrabbingTask task);
	IWebSiteGrabbingTask getNextPageGrabbingTask();
	IWebSiteGrabbingTask getParentTask();
	WebURL getLinkUrl();
	void setLinkUrl(WebURL url);
}
