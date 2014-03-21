/**
 * 
 */
package com.wxxr.mobile.stock.app.common;

/**
 * @author neillin
 *
 */
public interface ICacheVisitor<V> {
	/**
	 * return false will stop visiting
	 * @param key
	 * @param value
	 * @return
	 */
	boolean visit(Object key, V value);
}
