/**
 * 
 */
package com.wxxr.mobile.stock.app;

/**
 * @author neillin
 *
 */
public class AuthorizeFailedException extends StockAppBizException {

	private static final long serialVersionUID = -6727611220240602246L;

	public AuthorizeFailedException() {
		super();
	}

	public AuthorizeFailedException(String message, Throwable cause) {
		super(message, cause);
	}

	public AuthorizeFailedException(String message) {
		super(message);
	}

	public AuthorizeFailedException(Throwable cause) {
		super(cause);
	}

}
