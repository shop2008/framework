/**
 * 
 */
package com.wxxr.mobile.web.grabber.api;

/**
 * @author neillin
 *
 */
public interface IWebGrabberService {
	boolean doCrawl(String htmlUrl, Object customData);
}
