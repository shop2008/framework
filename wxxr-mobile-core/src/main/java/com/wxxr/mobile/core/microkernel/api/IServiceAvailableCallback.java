/**
 * 
 */
package com.wxxr.mobile.core.microkernel.api;

/**
 * @author neillin
 *
 */
public interface IServiceAvailableCallback<T> {
	void serviceAvailable(T service);
}
