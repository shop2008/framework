/**
 * 
 */
package com.wxxr.mobile.core.ui.api;

/**
 * @author neillin
 *
 */
public interface IBindingDecoratorFactory {
	<T> T createDecorator(T binding);
}
