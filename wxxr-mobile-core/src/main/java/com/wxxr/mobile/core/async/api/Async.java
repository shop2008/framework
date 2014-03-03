/**
 * 
 */
package com.wxxr.mobile.core.async.api;

/**
 * @author neillin
 *
 */
public interface Async<T> {
	void onResult(IAsyncCallback<T> callback);
}
