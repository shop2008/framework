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
	boolean isDownloaded(IWebPageGrabbingTask task, WebURL url);
	void saveContent(IWebPageGrabbingTask task,IWebContent content,WebURL url) throws IOException;
	IWebContent getContent(IWebPageGrabbingTask task, WebURL url) throws IOException;
	void makeContentReady(IWebPageGrabbingTask task) throws IOException;
	boolean isContentReady(IWebPageGrabbingTask task);
}
