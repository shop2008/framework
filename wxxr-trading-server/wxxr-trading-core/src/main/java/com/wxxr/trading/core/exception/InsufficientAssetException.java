/**
 * 
 */
package com.wxxr.trading.core.exception;

/**
 * @author neillin
 *
 */
public class InsufficientAssetException extends TradingException {

	private static final long serialVersionUID = 3886536482427595221L;

	public InsufficientAssetException() {
		super();
	}

	public InsufficientAssetException(String message, Throwable cause) {
		super(message, cause);
	}

	public InsufficientAssetException(String message) {
		super(message);
	}

	public InsufficientAssetException(Throwable cause) {
		super(cause);
	}

	/* (non-Javadoc)
	 * @see com.wxxr.stock.trading.exception.TradingException#getErrorCode()
	 */
	@Override
	public TradingError getErrorCode() {
		return TradingError.INSUFFICIENT_ASSET;
	}

}
