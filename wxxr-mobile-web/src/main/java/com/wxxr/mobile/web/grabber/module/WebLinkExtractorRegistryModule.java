/**
 * 
 */
package com.wxxr.mobile.web.grabber.module;

import com.wxxr.mobile.web.grabber.api.IWebLinkExractorRegistry;
import com.wxxr.mobile.web.grabber.api.IWebLinkExtractor;
import com.wxxr.mobile.web.grabber.common.AbstractGrabberModule;
import com.wxxr.mobile.web.grabber.common.WebLinkExtractorRegistry;

/**
 * @author neillin
 *
 */
public class WebLinkExtractorRegistryModule extends AbstractGrabberModule {
	
	private WebLinkExtractorRegistry registry = new WebLinkExtractorRegistry();

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.microkernel.api.AbstractModule#initServiceDependency()
	 */
	@Override
	protected void initServiceDependency() {

	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.microkernel.api.AbstractModule#startService()
	 */
	@Override
	protected void startService() {
		context.registerService(IWebLinkExractorRegistry.class, this.registry);
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.microkernel.api.AbstractModule#stopService()
	 */
	@Override
	protected void stopService() {
		context.unregisterService(IWebLinkExractorRegistry.class, this.registry);
	}

	/**
	 * @param elemName
	 * @param extrator
	 * @see com.wxxr.mobile.web.grabber.common.WebLinkExtractorRegistry#registerExtractor(java.lang.String, com.wxxr.mobile.web.grabber.api.IWebLinkExtractor)
	 */
	public void registerExtractor(String elemName, IWebLinkExtractor extrator) {
		registry.registerExtractor(elemName, extrator);
	}

	/**
	 * @param elemName
	 * @param extrator
	 * @return
	 * @see com.wxxr.mobile.web.grabber.common.WebLinkExtractorRegistry#unregisterExtractor(java.lang.String, com.wxxr.mobile.web.grabber.api.IWebLinkExtractor)
	 */
	public boolean unregisterExtractor(String elemName,
			IWebLinkExtractor extrator) {
		return registry.unregisterExtractor(elemName, extrator);
	}

}
