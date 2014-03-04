/**
 * 
 */
package com.wxxr.trading.core.exception;

/**
 * 用于处理由于系统原因造成的业务处理错误，比如代码的bug，网络断连和设备失效等
 * @author neillin
 *
 */
public class TradingSystemException extends RuntimeException {

	private static final long serialVersionUID = -1323551653839023733L;

	public TradingSystemException() {
		super();
	}

	public TradingSystemException(String message, Throwable cause) {
		super(message, cause);
	}

	public TradingSystemException(String message) {
		super(message);
	}

	public TradingSystemException(Throwable cause) {
		super(cause);
	}
}
