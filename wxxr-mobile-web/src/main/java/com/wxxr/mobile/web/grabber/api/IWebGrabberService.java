/**
 * 
 */
package com.wxxr.mobile.web.grabber.api;

/**
 * @author neillin
 *
 */
public interface IWebGrabberService {
	boolean grabWebPage(String htmlUrl, Object customData);
	boolean grabWebSite(String htmlUrl, Object customData);
}
