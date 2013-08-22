/**
 * 
 */
package com.wxxr.mobile.web.grabber.test;

import com.wxxr.mobile.core.rpc.http.apache.AbstractHttpRpcService;
import com.wxxr.mobile.core.rpc.http.api.HttpRpcService;
import com.wxxr.mobile.core.security.api.ISiteSecurityService;
import com.wxxr.mobile.web.grabber.common.AbstractGrabberModule;

/**
 * @author neillin
 *
 */
public class TestHttpService extends AbstractGrabberModule {

	private AbstractHttpRpcService httpService = new AbstractHttpRpcService();
	
	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.microkernel.api.AbstractModule#initServiceDependency()
	 */
	@Override
	protected void initServiceDependency() {
		addRequiredService(ISiteSecurityService.class);
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.microkernel.api.AbstractModule#startService()
	 */
	@Override
	protected void startService() {
		httpService.setConnectionPoolSize(10);
		httpService.startup(context);
		context.registerService(HttpRpcService.class, httpService);
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.microkernel.api.AbstractModule#stopService()
	 */
	@Override
	protected void stopService() {
		context.unregisterService(HttpRpcService.class, httpService);
		httpService.shutdown();
	}

}
