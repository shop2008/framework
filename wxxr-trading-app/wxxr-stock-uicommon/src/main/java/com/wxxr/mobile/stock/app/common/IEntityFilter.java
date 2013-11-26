/**
 * 
 */
package com.wxxr.mobile.stock.app.common;

/**
 * @author neillin
 *
 */
public interface IEntityFilter<T> {
	boolean doFilter(T entity);
}
