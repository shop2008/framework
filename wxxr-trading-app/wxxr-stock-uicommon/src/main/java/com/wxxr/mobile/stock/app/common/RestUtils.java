/**
 * 
 */
package com.wxxr.mobile.stock.app.common;

import com.wxxr.mobile.core.microkernel.api.KUtils;
import com.wxxr.mobile.core.rpc.http.api.IRestProxyService;

/**
 * @author neillin
 *
 */
public class RestUtils {
	public static <T> T getRestService(Class<T> serviceInterface) {
		return KUtils.getService(IRestProxyService.class).getRestService(serviceInterface);
	}
	
	public static <T> T getRestService(Class<T> serviceInterface,String serverUrl) {
		return KUtils.getService(IRestProxyService.class).getRestService(serviceInterface,serverUrl);
	}

}
