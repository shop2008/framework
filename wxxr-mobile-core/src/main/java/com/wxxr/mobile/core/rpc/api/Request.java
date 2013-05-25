/**
 * 
 */
package com.wxxr.mobile.core.rpc.api;

import java.util.concurrent.TimeUnit;

/**
 * @author neillin
 *
 */
public interface Request<T extends Response> {
	void invokeAsync(long timeout,TimeUnit unit, RequestCallback callback);
	
	T invoke(long timeout,TimeUnit unit) throws Exception;
	
	void setRequestEntity(DataEntity entity);
	
	DataEntity getRequestEntity();

}
