/**
 * 
 */
package com.wxxr.mobile.web.grabber.common;

import org.apache.http.HttpStatus;

import com.wxxr.mobile.core.log.api.Trace;
import com.wxxr.mobile.web.grabber.api.IGrabbingTaskFactory;
import com.wxxr.mobile.web.grabber.api.IHTMLParser;
import com.wxxr.mobile.web.grabber.api.IWebContentFetcher;
import com.wxxr.mobile.web.grabber.api.IWebContentStorage;
import com.wxxr.mobile.web.grabber.api.IWebCrawler;
import com.wxxr.mobile.web.grabber.api.IWebPageGrabbingTask;
import com.wxxr.mobile.web.grabber.api.IWebSiteGrabbingTask;
import com.wxxr.mobile.web.grabber.model.HtmlProcessingData;
import com.wxxr.mobile.web.grabber.model.IWebContent;
import com.wxxr.mobile.web.grabber.model.Page;
import com.wxxr.mobile.web.grabber.model.WebContentFetchResult;
import com.wxxr.mobile.web.grabber.model.WebURL;

/**
 * @author neillin
 *
 */
public abstract class AbstractWebCrawler implements IWebCrawler {
	
	private static final Trace logger = Trace.register(AbstractWebCrawler.class);
	
	

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.web.grabber.api.IWebCrawler#processPage(com.wxxr.mobile.web.grabber.api.IWebPageGrabbingTask, com.wxxr.mobile.web.grabber.api.WebURL)
	 */
	@Override
	public void processPage(final IWebPageGrabbingTask task, WebURL curURL)
			throws Exception {
		if (curURL == null) {
			return;
		}
		IWebContentStorage storage = getService(IWebContentStorage.class);
		IHTMLParser parser = getService(IHTMLParser.class);
		WebContentFetchResult fetchResult = null;
		boolean hasNewDownloaded = false;
		String date = storage.getContentLastModified(task, curURL);
			fetchResult = getService(IWebContentFetcher.class).fetchHeader(task,curURL,date);
			int statusCode = fetchResult.getStatusCode();
			task.handlePageStatusCode(curURL, statusCode, CustomFetchStatus.getStatusDescription(statusCode));
			if (statusCode != HttpStatus.SC_OK) {
				if (statusCode == HttpStatus.SC_MOVED_PERMANENTLY || statusCode == HttpStatus.SC_MOVED_TEMPORARILY) {
					if (task.followRedirects()) {
						String movedToUrl = fetchResult.getMovedToUrl();
						if (movedToUrl == null) {
							return;
						}
						WebURL webURL = new WebURL();
						webURL.setURL(movedToUrl);
						webURL.setParentUrl(curURL.getParentUrl());
						webURL.setDepth(curURL.getDepth());
						webURL.setAnchor(curURL.getAnchor());
						if (task.shouldVisit(webURL)) {
							task.scheduleURL(webURL);
						}
					}
				} else if (fetchResult.getStatusCode() == CustomFetchStatus.PageTooBig) {
					logger.info("Skipping a page which was bigger than max allowed size: " , curURL.getURL());
				}
				if(statusCode != HttpStatus.SC_NOT_MODIFIED){
					return;
				}
			}
		if(statusCode != HttpStatus.SC_NOT_MODIFIED) {
			Page page = new Page(curURL.getURL());
			if (!fetchResult.fetchContent(page)) {
				task.onContentFetchError(curURL,null);
				return;
			}
			storage.saveContent(task, page,curURL);
			hasNewDownloaded = true;
			fetchResult.discardContentIfNotConsumed();
		}else{
			if(logger.isDebugEnabled()){
				logger.debug("content of :["+curURL.getURL()+"] was downloaded and not modified since :["+date+"]");
			}
			if(!task.proceedOnContentNotModified()){
				return;
			}
		}
		IWebContent page = storage.getContent(task, curURL);
		HtmlProcessingData htmlProcessingData = null;
		try {
			if(!Util.isHtmlContent(page.getContentType())){
				htmlProcessingData = task.getHtmlData();
				htmlProcessingData.addDownloadedUrl(curURL);
//				if(hasNewDownloaded && (date == null)){
//					htmlProcessingData.setHasNewDownloadedLinks(true);
//				}
				return;
			}
			if(curURL.getDepth() == 0){
				try {
					htmlProcessingData = parser.parse(task, page, curURL.getURL());
				}catch(Throwable t){
					task.onParseError(curURL,t);
					return;
				}
				task.setHtmlData(htmlProcessingData);
//				if(hasNewDownloaded && (date == null)){
//					htmlProcessingData.setNewHtml(true);
//				}
				int maxCrawlDepth = task.getMaxDepthOfCrawling();
				for (WebURL webURL : htmlProcessingData.getOutgoingUrls()) {
					webURL.setParentUrl(curURL.getURL());
					webURL.setDepth((short) (curURL.getDepth() + 1));
					if ((maxCrawlDepth == -1 || curURL.getDepth() < maxCrawlDepth)&&(webURL.getAnchor().isPrefetchable()&&task.shouldVisit(webURL))) {
							task.scheduleURL(webURL);
					}
				}
			}else if(task instanceof IWebSiteGrabbingTask){
				IWebSiteGrabbingTask newTask = task.getContext().getService(IGrabbingTaskFactory.class).createSiteTask();
				newTask.init(task.getContext(), curURL.getURL(), task.getCustomData());
				newTask.setLinkUrl(curURL);
				((IWebSiteGrabbingTask)task).schedulePageGrabbingTask(newTask);
			}
		}finally {
			if(page != null){
				page.close();
			}
		}
	}

	protected abstract <T> T getService(Class<T> clazz);
	
}
