/**
 * 
 */
package com.wxxr.mobile.web.grabber.api;


/**
 * @author neillin
 *
 */
public interface IWebLinkExractorRegistry {
	IWebLinkExtractor[] getLinkExtractors(String elementName);
	
	void registerExtractor(String elemName, IWebLinkExtractor extrator);
	boolean unregisterExtractor(String elemName, IWebLinkExtractor extrator);
	
}
