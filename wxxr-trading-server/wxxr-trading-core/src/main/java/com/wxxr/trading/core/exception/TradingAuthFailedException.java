/**
 * 
 */
package com.wxxr.trading.core.exception;


/**
 * @author neillin
 *
 */
public class TradingAuthFailedException extends TradingException {

	public TradingAuthFailedException() {
		super();
	}

	public TradingAuthFailedException(String message, Throwable cause) {
		super(message, cause);
	}

	public TradingAuthFailedException(String message) {
		super(message);
	}

	public TradingAuthFailedException(Throwable cause) {
		super(cause);
	}

	@Override
	public TradingError getErrorCode() {
		
		return null;
	}

}
