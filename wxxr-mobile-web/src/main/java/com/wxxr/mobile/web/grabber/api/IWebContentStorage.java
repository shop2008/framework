/**
 * 
 */
package com.wxxr.mobile.web.grabber.api;

import java.io.IOException;

import com.wxxr.mobile.web.grabber.model.IWebContent;
import com.wxxr.mobile.web.grabber.model.WebURL;

/**
 * @author neillin
 *
 */
public interface IWebContentStorage {
	/**
	 * return correspondent content file's last modified date in HTTP-date format:"EEE, dd MMM yyyy HH:mm:ss zzz"
	 * return null if content file doesn't exist
	 * @param task
	 * @param url
	 * @return
	 */
	String getContentLastModified(IWebPageGrabbingTask task, WebURL url);
	void saveContent(IWebPageGrabbingTask task,IWebContent content,WebURL url) throws IOException;
	IWebContent getContent(IWebPageGrabbingTask task, WebURL url) throws IOException;
	void makeContentReady(IWebPageGrabbingTask task) throws IOException;
	boolean isContentReady(IWebPageGrabbingTask task);
}
