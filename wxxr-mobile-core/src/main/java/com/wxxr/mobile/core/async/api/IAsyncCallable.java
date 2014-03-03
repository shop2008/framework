/**
 * 
 */
package com.wxxr.mobile.core.async.api;

/**
 * @author neillin
 *
 */
public interface IAsyncCallable<V> {
	void call(IAsyncCallback<V> callback);
}
