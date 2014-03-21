/**
 * 
 */
package com.wxxr.trading.core.exception;

/**
 * ���ڴ�������ϵͳԭ����ɵ�ҵ������󣬱�������bug������������豸ʧЧ��
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
