/**
 * 
 */
package com.wxxr.mobile.web.grabber.module;

import com.wxxr.mobile.web.grabber.api.IHTMLParser;
import com.wxxr.mobile.web.grabber.api.IWebLinkExractorRegistry;
import com.wxxr.mobile.web.grabber.common.AbstractGrabberModule;
import com.wxxr.mobile.web.grabber.common.HtmlParserImpl;

/**
 * @author neillin
 *
 */
public class HtmlParserModule extends AbstractGrabberModule {

	private HtmlParserImpl parser = new HtmlParserImpl();
	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.microkernel.api.AbstractModule#initServiceDependency()
	 */
	@Override
	protected void initServiceDependency() {
		addRequiredService(IWebLinkExractorRegistry.class);
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.microkernel.api.AbstractModule#startService()
	 */
	@Override
	protected void startService() {
		context.registerService(IHTMLParser.class, this.parser);
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.microkernel.api.AbstractModule#stopService()
	 */
	@Override
	protected void stopService() {
		context.unregisterService(IHTMLParser.class, this.parser);
	}

}
