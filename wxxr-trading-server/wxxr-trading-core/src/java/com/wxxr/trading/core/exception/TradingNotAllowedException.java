/**
 * 
 */
package com.wxxr.trading.core.exception;

/**
 * @author neillin
 *
 */
public class TradingNotAllowedException extends TradingException {

	public TradingNotAllowedException() {
		super();
	}

	public TradingNotAllowedException(String message, Throwable cause) {
		super(message, cause);
	}

	public TradingNotAllowedException(String message) {
		super(message);
	}

	public TradingNotAllowedException(Throwable cause) {
		super(cause);
	}

	/* (non-Javadoc)
	 * @see com.wxxr.stock.trading.exception.TradingException#getErrorCode()
	 */
	@Override
	public TradingError getErrorCode() {
		return TradingError.TRADING_NOT_ALLOWED;
	}

}
