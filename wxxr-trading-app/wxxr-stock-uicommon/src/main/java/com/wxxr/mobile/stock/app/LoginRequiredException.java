/**
 * 
 */
package com.wxxr.mobile.stock.app;

/**
 * @author neillin
 *
 */
public class LoginRequiredException extends StockAppBizException {

	private static final long serialVersionUID = 5469416129516128670L;

	public LoginRequiredException() {
		super();
	}

	public LoginRequiredException(String message, Throwable cause) {
		super(message, cause);
	}

	public LoginRequiredException(String message) {
		super(message);
	}

	public LoginRequiredException(Throwable cause) {
		super(cause);
	}

}
