/**
 * 
 */
package com.wxxr.mobile.core.async.api;

/**
 * @author neillin
 *
 */
public interface ICancellable {
	void cancel();
	boolean isCancelled();
}
