/**
 * 
 */
package com.wxxr.mobile.core.ui.api;

/**
 * @author neillin
 *
 */
public interface IBindingDecoratorFactory {
	<T extends IUIComponent> IBinding<T> createDecorator(IBinding<T> binding);
}
