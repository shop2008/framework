/**
 * 
 */
package com.wxxr.mobile.android.http;

import java.util.Map;

import com.wxxr.mobile.android.app.IAndroidAppContext;
import com.wxxr.mobile.core.microkernel.api.AbstractModule;
import com.wxxr.mobile.core.rpc.http.apache.AbstractHttpRpcService;
import com.wxxr.mobile.core.rpc.http.apache.HttpRequestImpl;
import com.wxxr.mobile.core.rpc.http.api.HttpRequest;
import com.wxxr.mobile.core.security.api.ISiteSecurityService;

/**
 * @author neillin
 *
 */
public class HttpRpcServiceModule<T extends IAndroidAppContext> extends AbstractModule<T> {

	private AbstractHttpRpcService service = new AbstractHttpRpcService() {
		@Override
		public HttpRequest createRequest(String endpointUrl, Map<String, Object> params) {
			HttpRequest request=super.createRequest(endpointUrl, params);
			request.setHeader("deviceid", context.getApplication().getDeviceId());
			request.setHeader("deviceType", context.getApplication().getDeviceType());
			request.setHeader("appName", context.getApplication().getApplicationName());
			request.setHeader("appVer", context.getApplication().getApplicationVersion());
			return request;
		}
	};
	
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
