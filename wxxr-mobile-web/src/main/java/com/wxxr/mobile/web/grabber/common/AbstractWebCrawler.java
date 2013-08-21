/**
 * 
 */
package com.wxxr.mobile.web.grabber.common;

import org.apache.http.HttpStatus;

import com.wxxr.mobile.core.log.api.Trace;
import com.wxxr.mobile.web.grabber.api.IHTMLParser;
import com.wxxr.mobile.web.grabber.api.IWebContentFetcher;
import com.wxxr.mobile.web.grabber.api.IWebContentStorage;
import com.wxxr.mobile.web.grabber.api.IWebCrawler;
import com.wxxr.mobile.web.grabber.api.IWebGrabbingTask;
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
	 * @see com.wxxr.mobile.web.grabber.api.IWebCrawler#processPage(com.wxxr.mobile.web.grabber.api.IWebGrabbingTask, com.wxxr.mobile.web.grabber.api.WebURL)
	 */
	@Override
	public void processPage(final IWebGrabbingTask task, WebURL curURL)
			throws Exception {
		if (curURL == null) {
			return;
		}
		IWebContentStorage storage = getService(IWebContentStorage.class);
		IHTMLParser parser = getService(IHTMLParser.class);
		WebContentFetchResult fetchResult = null;
		if(!storage.isDownloaded(task, curURL)){
			fetchResult = getService(IWebContentFetcher.class).fetchHeader(task,curURL);
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
				return;
			}
			Page page = new Page(curURL.getURL());
			if (!fetchResult.fetchContent(page)) {
				task.onContentFetchError(curURL,null);
				return;
			}
			storage.saveContent(task, page,curURL);
			fetchResult.discardContentIfNotConsumed();
		}
		IWebContent page = storage.getContent(task, curURL);
		HtmlProcessingData htmlProcessingData = null;
		try {
			if(!Util.isHtmlContent(page.getContentType())){
				htmlProcessingData = task.getHtmlData();
				htmlProcessingData.addDownloadedUrl(curURL);
				return;
			}
			try {
				htmlProcessingData = parser.parse(task, page, curURL.getURL());
			}catch(Throwable t){
				task.onParseError(curURL,t);
				return;
			}
			task.setHtmlData(htmlProcessingData);
			int maxCrawlDepth = task.getMaxDepthOfCrawling();
			for (WebURL webURL : htmlProcessingData.getOutgoingUrls()) {
				webURL.setParentUrl(curURL.getURL());
				if (!storage.isDownloaded(task,webURL)) {
					webURL.setDepth((short) (curURL.getDepth() + 1));
					if (maxCrawlDepth == -1 || curURL.getDepth() < maxCrawlDepth) {
						if (task.shouldVisit(webURL)) {
							task.scheduleURL(webURL);
						}
					}
				}
			}
		}finally {
			if(page != null){
				page.close();
			}
		}
	}

	protected abstract <T> T getService(Class<T> clazz);
	
}
