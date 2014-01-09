/**
 * 
 */
package com.wxxr.mobile.core.microkernel.api;

/**
 * @author neillin
 *
 */
public interface IStatefulService extends Cloneable {
	Object clone();
	void destroy(Object serviceHandler);
	IServiceDecoratorBuilder getDecoratorBuilder();
}
