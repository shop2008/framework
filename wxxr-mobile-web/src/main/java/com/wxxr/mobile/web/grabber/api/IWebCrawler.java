/**
 * 
 */
package com.wxxr.mobile.web.grabber.api;

import com.wxxr.mobile.web.grabber.model.WebURL;

/**
 * @author neillin
 *
 */
public interface IWebCrawler {
	void processPage(IWebGrabbingTask task,WebURL curURL) throws Exception;
}
