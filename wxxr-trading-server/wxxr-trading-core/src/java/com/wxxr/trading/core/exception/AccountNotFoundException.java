/**
 * 
 */
package com.wxxr.trading.core.exception;

/**
 * @author neillin
 *
 */
public class AccountNotFoundException extends TradingException {

	public AccountNotFoundException() {
		super();
	}

	public AccountNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public AccountNotFoundException(String message) {
		super(message);
	}

	public AccountNotFoundException(Throwable cause) {
		super(cause);
	}

	/* (non-Javadoc)
	 * @see com.wxxr.stock.trading.exception.TradingException#getErrorCode()
	 */
	@Override
	public TradingError getErrorCode() {
		return TradingError.ACCT_NOT_FOUND;
	}

}
