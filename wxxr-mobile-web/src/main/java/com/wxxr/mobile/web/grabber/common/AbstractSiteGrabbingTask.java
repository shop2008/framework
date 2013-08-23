/**
 * 
 */
package com.wxxr.mobile.web.grabber.common;

import java.util.LinkedList;

import com.wxxr.mobile.web.grabber.api.IWebSiteGrabbingTask;
import com.wxxr.mobile.web.grabber.model.WebURL;

/**
 * @author neillin
 *
 */
public abstract class AbstractSiteGrabbingTask extends AbstractPageGrabbingTask implements IWebSiteGrabbingTask{
	
	private AbstractSiteGrabbingTask parent;
	private LinkedList<IWebSiteGrabbingTask> tasks = new LinkedList<IWebSiteGrabbingTask>();
	private WebURL linkUrl;

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.web.grabber.api.IWebSiteGrabbingTask#schedulePageGrabbingTask(com.wxxr.mobile.web.grabber.api.IWebPageGrabbingTask)
	 */
	@Override
	public void schedulePageGrabbingTask(IWebSiteGrabbingTask task) {
		synchronized(this.tasks){
			if(!this.tasks.contains(task)){
				this.tasks.addLast(task);
			}
			((AbstractSiteGrabbingTask)task).parent = this;
		}
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.web.grabber.api.IWebSiteGrabbingTask#getNextPageGrabbingTask()
	 */
	@Override
	public IWebSiteGrabbingTask getNextPageGrabbingTask() {
		synchronized(this.tasks){
			return this.tasks.isEmpty() ? null : this.tasks.removeFirst();
		}
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.web.grabber.api.IWebSiteGrabbingTask#getParentTask()
	 */
	@Override
	public IWebSiteGrabbingTask getParentTask() {
		return this.parent;
	}

	/**
	 * @return the linkUrl
	 */
	public WebURL getLinkUrl() {
		return linkUrl;
	}

	/**
	 * @param linkUrl the linkUrl to set
	 */
	public void setLinkUrl(WebURL linkUrl) {
		this.linkUrl = linkUrl;
	}


}
