/**
 * 
 */
package com.wxxr.mobile.core.ui.api;

/**
 * @author neillin
 *
 */
public class ValidationException extends Exception {

	/**
	 * 
	 */
	public ValidationException() {
	}

	/**
	 * @param message
	 */
	public ValidationException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public ValidationException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public ValidationException(String message, Throwable cause) {
		super(message, cause);
	}

}
