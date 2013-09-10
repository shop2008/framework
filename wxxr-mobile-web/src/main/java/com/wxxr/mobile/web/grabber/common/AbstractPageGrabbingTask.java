/**
 * 
 */
package com.wxxr.mobile.web.grabber.common;

import java.util.LinkedList;

import com.wxxr.mobile.core.log.api.Trace;
import com.wxxr.mobile.web.grabber.api.IGrabberServiceContext;
import com.wxxr.mobile.web.grabber.api.IWebPageGrabbingTask;
import com.wxxr.mobile.web.grabber.model.HtmlProcessingData;
import com.wxxr.mobile.web.grabber.model.WebURL;

/**
 * @author neillin
 *
 */
public abstract class AbstractPageGrabbingTask implements IWebPageGrabbingTask {
	private static Trace log = Trace.register(AbstractPageGrabbingTask.class);
	private int maxDepthOfCrawling = 1,maxOutgoingLinksToFollow = 1000,maxDownloadSize = 8*1024*1024;
	private boolean followRedirects = true,scanBinaryContent = true;
	private IGrabberServiceContext context;
	private Object customData;
	private WebURL pageUrl;
	private LinkedList<WebURL> queue = new LinkedList<WebURL>();
	private boolean hasError = false, finished = false;
	private HtmlProcessingData htmlData;
	private boolean proceedOnContentNotModified;
	/* (non-Javadoc)
	 * @see com.wxxr.mobile.web.grabber.api.IWebPageGrabbingTask#init(com.wxxr.mobile.web.grabber.api.IGrabberServiceContext, java.lang.String, java.lang.Object)
	 */
	@Override
	public void init(IGrabberServiceContext context, String url,
			Object customData) {
		this.context = context;
		WebURL webUrl = new WebURL();
		webUrl.setURL(url);
		webUrl.setDepth((short)0);
		this.pageUrl = webUrl;
		this.customData = customData;
		scheduleURL(webUrl);
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.web.grabber.api.IWebPageGrabbingTask#getNextScheduledURL()
	 */
	@Override
	public WebURL getNextScheduledURL() {
		synchronized(this.queue){
			if(this.queue.isEmpty()){
				return null;
			}
			return this.queue.removeFirst();
		}
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.web.grabber.api.IWebPageGrabbingTask#scheduleURL(com.wxxr.mobile.web.grabber.api.WebURL)
	 */
	@Override
	public void scheduleURL(WebURL url) {
		synchronized(this.queue){
			this.queue.add(url);
		}

	}


	/* (non-Javadoc)
	 * @see com.wxxr.mobile.web.grabber.api.IWebPageGrabbingTask#onContentFetchError(com.wxxr.mobile.web.grabber.api.WebURL)
	 */
	@Override
	public void onContentFetchError(WebURL webUrl,Throwable t) {
		if(t != null){
			log.warn("Failed to download content of :" + webUrl.getURL(),t);
		}else{
			log.warn("Failed to download content of :", webUrl.getURL());
		}
		this.hasError = true;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.web.grabber.api.IWebPageGrabbingTask#onParseError(com.wxxr.mobile.web.grabber.api.WebURL)
	 */
	@Override
	public void onParseError(WebURL webUrl,Throwable t) {
		if(t != null){
			log.warn("Failed to parse html content of :"+ webUrl.getURL(),t);
		}else{
			log.warn("Failed to parse html content of :", webUrl.getURL());
		}
		this.hasError = true;
	}

	
	/* (non-Javadoc)
	 * @see com.wxxr.mobile.web.grabber.api.IWebPageGrabbingTask#onProcessingError(com.wxxr.mobile.web.grabber.model.WebURL, java.lang.Throwable)
	 */
	@Override
	public void onProcessingError(WebURL webUrl, Throwable t) {
		if(t != null){
			log.warn("Failed to processing web content of :"+ webUrl.getURL(),t);
		}else{
			log.warn("Failed to processing web content of :", webUrl.getURL());
		}
		this.hasError = true;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.web.grabber.api.IWebPageGrabbingTask#handlePageStatusCode(com.wxxr.mobile.web.grabber.api.WebURL, int, java.lang.String)
	 */
	@Override
	public void handlePageStatusCode(WebURL webUrl, int statusCode,
			String statusDescription) {
		if(statusCode >= 400){
			log.warn("Server return 4xx error code :["+statusCode+"], desc :["+statusDescription+"] when downloading :", webUrl.getURL());
			this.hasError = true;
		}

	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.web.grabber.api.IWebPageGrabbingTask#finish(java.lang.Throwable)
	 */
	@Override
	public void finish(Throwable t) {
		if(t != null){
			log.error("Crawling task :["+toString()+"] finished with error",t);
			this.hasError = true;
			this.finished = true;
		}else{
			this.finished = true;
		}

	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.web.grabber.api.IWebPageGrabbingTask#isFinished()
	 */
	@Override
	public boolean isFinished() {
		return this.finished;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.web.grabber.api.IWebPageGrabbingTask#hasErrors()
	 */
	@Override
	public boolean hasErrors() {
		return this.hasError;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.web.grabber.api.IWebPageGrabbingTask#scanBinaryContent()
	 */
	@Override
	public boolean scanBinaryContent() {
		return this.scanBinaryContent;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.web.grabber.api.IWebPageGrabbingTask#followRedirects()
	 */
	@Override
	public boolean followRedirects() {
		return this.followRedirects;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.web.grabber.api.IWebPageGrabbingTask#getMaxDepthOfCrawling()
	 */
	@Override
	public int getMaxDepthOfCrawling() {
		return this.maxDepthOfCrawling;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.web.grabber.api.IWebPageGrabbingTask#getMaxOutgoingLinksToFollow()
	 */
	@Override
	public int getMaxOutgoingLinksToFollow() {
		return this.maxOutgoingLinksToFollow;
	}

	/**
	 * @return the followRedirects
	 */
	public boolean isFollowRedirects() {
		return followRedirects;
	}

	/**
	 * @return the scanBinaryContent
	 */
	public boolean isScanBinaryContent() {
		return scanBinaryContent;
	}

	/**
	 * @return the context
	 */
	public IGrabberServiceContext getContext() {
		return context;
	}

	/**
	 * @return the customData
	 */
	public Object getCustomData() {
		return customData;
	}

	/**
	 * @return the pageUrl
	 */
	public WebURL getPageUrl() {
		return pageUrl;
	}

	/**
	 * @param maxDepthOfCrawling the maxDepthOfCrawling to set
	 */
	public void setMaxDepthOfCrawling(int maxDepthOfCrawling) {
		this.maxDepthOfCrawling = maxDepthOfCrawling;
	}

	/**
	 * @param maxOutgoingLinksToFollow the maxOutgoingLinksToFollow to set
	 */
	public void setMaxOutgoingLinksToFollow(int maxOutgoingLinksToFollow) {
		this.maxOutgoingLinksToFollow = maxOutgoingLinksToFollow;
	}

	/**
	 * @param followRedirects the followRedirects to set
	 */
	public void setFollowRedirects(boolean followRedirects) {
		this.followRedirects = followRedirects;
	}

	/**
	 * @param scanBinaryContent the scanBinaryContent to set
	 */
	public void setScanBinaryContent(boolean scanBinaryContent) {
		this.scanBinaryContent = scanBinaryContent;
	}

	/**
	 * @return the maxDownloadSize
	 */
	public int getMaxDownloadSize() {
		return maxDownloadSize;
	}

	/**
	 * @param maxDownloadSize the maxDownloadSize to set
	 */
	public void setMaxDownloadSize(int maxDownloadSize) {
		this.maxDownloadSize = maxDownloadSize;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.web.grabber.api.IWebPageGrabbingTask#setHtmlData(com.wxxr.mobile.web.grabber.model.HtmlProcessingData)
	 */
	@Override
	public void setHtmlData(HtmlProcessingData data) {
		if(this.htmlData != null){
			throw new IllegalStateException("Only one html content support at current version !!!");
		}
		this.htmlData = data;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.web.grabber.api.IWebPageGrabbingTask#getHtmlData()
	 */
	@Override
	public HtmlProcessingData getHtmlData() {
		return this.htmlData;
	}

	/**
	 * @return the proceedOnContentNotModified
	 */
	@Override
	public boolean proceedOnContentNotModified() {
		return proceedOnContentNotModified;
	}

	/**
	 * @param proceedOnContentNotModified the proceedOnContentNotModified to set
	 */
	public void setProceedOnContentNotModified(boolean proceedOnContentNotModified) {
		this.proceedOnContentNotModified = proceedOnContentNotModified;
	}

	

}
