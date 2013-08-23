/**
 * 
 */
package com.wxxr.mobile.web.grabber.api;

/**
 * @author neillin
 *
 */
public interface IGrabbingTaskFactory {
	IWebPageGrabbingTask createPageTask();
	IWebSiteGrabbingTask createSiteTask();
}
