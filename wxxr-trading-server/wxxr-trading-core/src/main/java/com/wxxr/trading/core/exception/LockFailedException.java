package com.wxxr.trading.core.exception;

public class LockFailedException extends TradingException{

	
	public LockFailedException() {
		super();
	}

	public LockFailedException(String message, Throwable cause) {
		super(message, cause);
	}

	public LockFailedException(String message) {
		super(message);
	}

	public LockFailedException(Throwable cause) {
		super(cause);
	}

	@Override
	public TradingError getErrorCode() {
		return TradingError.LOCK_FAILED;
	}

}
