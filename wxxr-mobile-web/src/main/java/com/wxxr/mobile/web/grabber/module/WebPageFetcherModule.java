/**
 * 
 */
package com.wxxr.mobile.web.grabber.module;

import java.util.Date;

import com.wxxr.mobile.core.rpc.http.api.HttpRpcService;
import com.wxxr.mobile.web.grabber.api.IWebContentFetcher;
import com.wxxr.mobile.web.grabber.common.AbstractGrabberModule;
import com.wxxr.mobile.web.grabber.common.AbstractWebContentFetcher;

/**
 * @author neillin
 *
 */
public class WebPageFetcherModule extends AbstractGrabberModule {
	
	private AbstractWebContentFetcher fetcher = new AbstractWebContentFetcher() {
		
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
		addRequiredService(HttpRpcService.class);
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.microkernel.api.AbstractModule#startService()
	 */
	@Override
	protected void startService() {
		context.registerService(IWebContentFetcher.class, this.fetcher);
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.microkernel.api.AbstractModule#stopService()
	 */
	@Override
	protected void stopService() {
		context.unregisterService(IWebContentFetcher.class, this.fetcher);
	}

	/**
	 * @return
	 * @see com.wxxr.mobile.web.grabber.common.AbstractWebContentFetcher#getLastFetchTime()
	 */
	public Date getLastFetchTime() {
		return fetcher.getLastFetchTime();
	}

	/**
	 * @return
	 * @see com.wxxr.mobile.web.grabber.common.AbstractWebContentFetcher#getHttpRequestTimeout()
	 */
	public int getHttpRequestTimeout() {
		return fetcher.getHttpRequestTimeout();
	}

	/**
	 * @return
	 * @see com.wxxr.mobile.web.grabber.common.AbstractWebContentFetcher#getPolitenessDelay()
	 */
	public int getPolitenessDelay() {
		return fetcher.getPolitenessDelay();
	}

	/**
	 * @param httpRequestTimeout
	 * @see com.wxxr.mobile.web.grabber.common.AbstractWebContentFetcher#setHttpRequestTimeout(int)
	 */
	public void setHttpRequestTimeout(int httpRequestTimeout) {
		fetcher.setHttpRequestTimeout(httpRequestTimeout);
	}

	/**
	 * @param politenessDelay
	 * @see com.wxxr.mobile.web.grabber.common.AbstractWebContentFetcher#setPolitenessDelay(int)
	 */
	public void setPolitenessDelay(int politenessDelay) {
		fetcher.setPolitenessDelay(politenessDelay);
	}

}
