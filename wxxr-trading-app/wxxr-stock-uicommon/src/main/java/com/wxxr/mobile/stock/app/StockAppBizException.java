/**
 * 
 */
package com.wxxr.mobile.stock.app;

/**
 * @author wangxuyang
 *
 */
public class StockAppBizException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public StockAppBizException() {
		super();
	}

	public StockAppBizException(String message, Throwable cause) {
		super(message, cause);
	}

	public StockAppBizException(String message) {
		super(message);
	}

	public StockAppBizException(Throwable cause) {
		super(cause);
	}

}
