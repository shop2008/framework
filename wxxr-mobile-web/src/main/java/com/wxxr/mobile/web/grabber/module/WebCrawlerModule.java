/**
 * 
 */
package com.wxxr.mobile.web.grabber.module;

import com.wxxr.mobile.web.grabber.api.IHTMLParser;
import com.wxxr.mobile.web.grabber.api.IWebContentFetcher;
import com.wxxr.mobile.web.grabber.api.IWebContentStorage;
import com.wxxr.mobile.web.grabber.api.IWebCrawler;
import com.wxxr.mobile.web.grabber.common.AbstractGrabberModule;
import com.wxxr.mobile.web.grabber.common.AbstractWebCrawler;

/**
 * @author neillin
 *
 */
public class WebCrawlerModule extends AbstractGrabberModule {

	private AbstractWebCrawler crawler = new AbstractWebCrawler() {
		
		@Override
		protected <T> T getService(Class<T> clazz) {
			return context.getService(clazz);
		}
	};
	
	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.microkernel.api.AbstractModule#initServiceDependency()
	 */
	@Override
	protected void initServiceDependency() {
		addRequiredService(IWebContentStorage.class);
		addRequiredService(IHTMLParser.class);
		addRequiredService(IWebContentFetcher.class);		

	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.microkernel.api.AbstractModule#startService()
	 */
	@Override
	protected void startService() {
		context.registerService(IWebCrawler.class, this.crawler);
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.microkernel.api.AbstractModule#stopService()
	 */
	@Override
	protected void stopService() {
		context.unregisterService(IWebCrawler.class, this.crawler);
	}

}
