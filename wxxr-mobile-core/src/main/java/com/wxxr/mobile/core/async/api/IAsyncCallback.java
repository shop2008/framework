/**
 * 
 */
package com.wxxr.mobile.core.async.api;


/**
 * @author neillin
 *
 */
public interface IAsyncCallback<T> {
	void success(T result);
	void failed(Throwable cause);
	void cancelled();
	void setCancellable(ICancellable cancellable);
}
