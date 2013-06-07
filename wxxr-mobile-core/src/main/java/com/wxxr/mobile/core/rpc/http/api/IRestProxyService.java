/**
 * 
 */
package com.wxxr.mobile.core.rpc.http.api;


/**
 * @author neillin
 *
 */
public interface IRestProxyService {
	<T> T getRestService(Class<T> clazz);
	<T> T getRestService(Class<T> clazz, String target);
}
