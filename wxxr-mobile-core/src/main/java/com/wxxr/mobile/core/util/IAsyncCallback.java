/**
 * 
 */
package com.wxxr.mobile.core.util;

/**
 * @author neillin
 *
 */
public interface IAsyncCallback {
	void success(Object result);
	void failed(Object cause);
}
