/**
 * 
 */
package com.wxxr.mobile.core.ui.api;

/**
 * @author neillin
 *
 */
public interface IDecoratable <T>{
	T doDecorate(String... decoratorName);
	Class<T> getDecoratorClass();
}
