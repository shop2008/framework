/**
 * 
 */
package com.wxxr.trading.core.exception;

/**
 * @author neillin
 *
 */
public class RecommendNotAllowedException extends TradingException {

	public RecommendNotAllowedException() {
		super();
	}

	public RecommendNotAllowedException(String message, Throwable cause) {
		super(message, cause);
	}

	public RecommendNotAllowedException(String message) {
		super(message);
	}

	public RecommendNotAllowedException(Throwable cause) {
		super(cause);
	}

	/* (non-Javadoc)
	 * @see com.wxxr.stock.trading.exception.TradingException#getErrorCode()
	 */
	@Override
	public TradingError getErrorCode() {
		return TradingError.RECOMMEND_NOT_ALLOWED;
	}

}
