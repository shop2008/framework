/**
 * 
 */
package com.wxxr.mobile.core.ui.api;

/**
 * @author neillin
 *
 */
public interface IBindingDecoratorRegistry {
	void registerDecorator(String name, Class<?> clazz);
	boolean unregisterDecorator(String name, Class<?> clazz);
	void registerDecorator(String name, IBindingDecoratorFactory factory);
	boolean unregisterDecorator(String name, IBindingDecoratorFactory factory);
	boolean hasDecoratorRegistered(String name);
	<T> T createDecorator(String name, T decorator);
}
