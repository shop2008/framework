/**
 * 
 */
package com.wxxr.mobile.core.microkernel.api;

/**
 * @author neillin
 *
 */
public interface IServiceDecoratorBuilder {
	<T> T createServiceDecorator(Class<T> clazz, IServiceDelegateHolder<T> holder);
}
