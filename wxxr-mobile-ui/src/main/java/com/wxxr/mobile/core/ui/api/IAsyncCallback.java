/**
 * 
 */
package com.wxxr.mobile.core.ui.api;

/**
 * @author neillin
 *
 */
public interface IAsyncCallback {
	void success(Object result);
	void failed(Object cause);
}
