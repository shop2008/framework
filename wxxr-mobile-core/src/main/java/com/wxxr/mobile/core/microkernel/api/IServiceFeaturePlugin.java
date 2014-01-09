/**
 * 
 */
package com.wxxr.mobile.core.microkernel.api;

/**
 * @author neillin
 *
 */
public interface IServiceFeaturePlugin {
	<T> T buildServiceHandler(Class<T> serviceInterface, Object handler,IServicePluginChain chain);
}
