/**
 * 
 */
package com.wxxr.mobile.android.http;

import com.wxxr.mobile.android.app.IAndroidAppContext;
import com.wxxr.mobile.core.microkernel.api.AbstractModule;
import com.wxxr.mobile.core.security.api.ISiteSecurityService;

/**
 * @author neillin
 *
 */
public class HttpRpcServiceModule<T extends IAndroidAppContext> extends AbstractModule<T> {

	private AbstractHttpRpcService service = new AbstractHttpRpcService();
	
	@Override
	protected void initServiceDependency() {
		addRequiredService(ISiteSecurityService.class);
	}

	@Override
	protected void startService() {
		service.startup(context);
	}

	@Override
	protected void stopService() {
		service.shutdown();
	}

}
