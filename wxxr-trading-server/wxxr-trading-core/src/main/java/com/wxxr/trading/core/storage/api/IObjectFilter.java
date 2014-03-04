/**
 * 
 */
package com.wxxr.trading.core.storage.api;

/**
 * @author neillin
 *
 */
public interface IObjectFilter<T> {
	boolean doFilter(T object);
}
