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
	boolean isDownloaded(IWebGrabbingTask task, WebURL url);
	void saveContent(IWebGrabbingTask task,IWebContent content,WebURL url) throws IOException;
	IWebContent getContent(IWebGrabbingTask task, WebURL url) throws IOException;
	void makeContentReady(IWebGrabbingTask task) throws IOException;
	boolean isContentReady(IWebGrabbingTask task);
}
