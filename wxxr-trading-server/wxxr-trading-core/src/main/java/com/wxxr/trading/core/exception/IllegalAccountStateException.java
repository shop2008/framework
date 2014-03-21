/**
 * 
 */
package com.wxxr.trading.core.exception;

/**
 * @author neillin
 *
 */
public class IllegalAccountStateException extends TradingException {

	public IllegalAccountStateException() {
		super();
	}

	public IllegalAccountStateException(String message, Throwable cause) {
		super(message, cause);
	}

	public IllegalAccountStateException(String message) {
		super(message);
	}

	public IllegalAccountStateException(Throwable cause) {
		super(cause);
	}

	private static final long serialVersionUID = -1737214111780812938L;

	/* (non-Javadoc)
	 * @see com.wxxr.stock.trading.exception.TradingException#getErrorCode()
	 */
	@Override
	public TradingError getErrorCode() {
		return TradingError.ILLEGAL_STATE;
	}

}
