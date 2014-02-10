/**
 * 
 */
package com.wxxr.trading.core.exception;

/**
 * 用于处理由于用户原因造成的业务处理错误，比如用户的资金余额不足，资金账户不存在等
 * @author neillin
 *
 */
public abstract class TradingException extends Exception {

	private static final long serialVersionUID = -1323551653839023733L;

	public TradingException() {
		super();
	}

	public TradingException(String message, Throwable cause) {
		super(message, cause);
	}

	public TradingException(String message) {
		super(message);
	}

	public TradingException(Throwable cause) {
		super(cause);
	}
	
	public abstract TradingError getErrorCode();

}
