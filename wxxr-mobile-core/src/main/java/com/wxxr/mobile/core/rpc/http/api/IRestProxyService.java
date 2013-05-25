/**
 * 
 */
package com.wxxr.mobile.core.rpc.http.api;

import java.net.URI;

/**
 * @author neillin
 *
 */
public interface IRestProxyService {
	<T> T getRestService(Class<T> clazz, String target);
	<T> T getRestService(Class<T> clazz, URI target);
}
