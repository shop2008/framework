/**
 * 
 */
package com.wxxr.mobile.stock.app;

/**
 * @author neillin
 *
 */
public class LoginFailedException extends StockAppBizException {

	private static final long serialVersionUID = -3694437997431587353L;

	public LoginFailedException() {
		super();
	}

	public LoginFailedException(String message, Throwable cause) {
		super(message, cause);
	}

	public LoginFailedException(String message) {
		super(message);
	}

	public LoginFailedException(Throwable cause) {
		super(cause);
	}

}
