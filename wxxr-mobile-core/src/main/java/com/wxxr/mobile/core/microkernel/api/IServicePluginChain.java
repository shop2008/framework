/**
 * 
 */
package com.wxxr.mobile.core.microkernel.api;

/**
 * @author neillin
 *
 */
public interface IServicePluginChain {
	<T> T invokeNext(Class<T> serviceInterface,Object serviceHandler);
}
