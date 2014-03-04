/**
 * 
 */
package com.wxxr.mobile.stock.app.common;

/**
 * @author neillin
 *
 */
public interface IEntityFetcher<V> {
	V fetchFromCache(IBindableEntityCache<?, ?> cache);
}
