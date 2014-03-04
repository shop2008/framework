/**
 * 
 */
package com.wxxr.mobile.stock.app.common;

import com.wxxr.mobile.stock.app.StockAppBizException;

/**
 * @author neillin
 *
 */
public class CacheUpdateFailedException extends StockAppBizException {

	public CacheUpdateFailedException() {
		super();
	}

	public CacheUpdateFailedException(String message, Throwable cause) {
		super(message, cause);
	}

	public CacheUpdateFailedException(String message) {
		super(message);
	}

	public CacheUpdateFailedException(Throwable cause) {
		super(cause);
	}

}
