/**
 * 
 */
package com.wxxr.mobile.core.ui.api;

/**
 * @author neillin
 *
 */
public interface IBindingDecoratorRegistry {
	void registerDecorator(String name, Class<? extends IBinding<?>> clazz);
	boolean unregisterDecorator(String name, Class<? extends IBinding<?>> clazz);
	void registerDecorator(String name, IBindingDecoratorFactory factory);
	boolean unregisterDecorator(String name, IBindingDecoratorFactory factory);
	boolean hasDecoratorRegistered(String name);
	<T extends IUIComponent> IBinding<T> createDecorator(String name, IBinding<T> binding);
}
