/**
 * 
 */
package com.wxxr.trading.core.exception;

/**
 * ���ڴ��������û�ԭ����ɵ�ҵ������󣬱����û����ʽ����㣬�ʽ��˻������ڵ�
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
