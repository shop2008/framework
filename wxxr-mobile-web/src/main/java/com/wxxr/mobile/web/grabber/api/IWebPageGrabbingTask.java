/**
 * 
 */
package com.wxxr.mobile.web.grabber.api;

import com.wxxr.mobile.web.grabber.model.HtmlProcessingData;
import com.wxxr.mobile.web.grabber.model.WebURL;

/**
 * @author neillin
 *
 */
public interface IWebPageGrabbingTask {
	void init(IGrabberServiceContext context, String url,Object customData);
	
	WebURL getNextScheduledURL();
	
	void scheduleURL(WebURL url);
	
	boolean shouldVisit(WebURL url);
	/**
	 * This function is called if the content of a url could not be fetched.
	 * 
	 * @param webUrl
	 */
	void onContentFetchError(WebURL webUrl,Throwable t);

	/**
	 * This function is called if there has been an error in parsing the
	 * content.
	 * 
	 * @param webUrl
	 */
	void onParseError(WebURL webUrl,Throwable t);
	
	/**
	 * This function is called if there has been an error in parsing the
	 * content.
	 * 
	 * @param webUrl
	 */
	void onProcessingError(WebURL webUrl,Throwable t);

	
	/**
	 * This function is called once the header of a page is fetched. It can be
	 * implemented to perform custom logic for different status
	 * codes. For example, 404 pages can be logged, etc.
	 * 
	 * @param webUrl
	 * @param statusCode
	 * @param statusDescription
	 */
	void handlePageStatusCode(WebURL webUrl, int statusCode, String statusDescription);
	
	void setHtmlData(HtmlProcessingData data);
	
	HtmlProcessingData getHtmlData();
	
	void finish(Throwable t);
	
	boolean isFinished();
	
	boolean hasErrors();
	
	boolean scanBinaryContent();
	
	boolean followRedirects();
	
	int getMaxDepthOfCrawling();
	
	int getMaxOutgoingLinksToFollow();
	
	int getMaxDownloadSize();
	
	IGrabberServiceContext getContext();
	
	Object getCustomData();
	
	WebURL getPageUrl();
	
	boolean proceedOnContentNotModified();

}
