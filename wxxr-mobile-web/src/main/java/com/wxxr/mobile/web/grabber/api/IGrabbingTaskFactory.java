/**
 * 
 */
package com.wxxr.mobile.web.grabber.api;

/**
 * @author neillin
 *
 */
public interface IGrabbingTaskFactory {
	IWebGrabbingTask createNewTask();
	void destroyTask(IWebGrabbingTask task);
}
