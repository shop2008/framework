/**
 * 
 */
package com.wxxr.trading.core.exception;

/**
 * @author neillin
 *
 */
public class AccountLockFailedException extends TradingException {

	private static final long serialVersionUID = -6331622918483028189L;

	public AccountLockFailedException() {
		super();
	}

	public AccountLockFailedException(String message, Throwable cause) {
		super(message, cause);
	}

	public AccountLockFailedException(String message) {
		super(message);
	}
	
	public AccountLockFailedException(Throwable cause) {
		super(cause);
	}
	/* (non-Javadoc)
	 * @see com.wxxr.stock.trading.exception.TradingException#getErrorCode()
	 */
	@Override
	public TradingError getErrorCode() {
		return TradingError.LOCK_FAILED;
	}

}
