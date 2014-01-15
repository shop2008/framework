/**
 * 
 */
package com.wxxr.mobile.core.ui.api;

/**
 * @author neillin
 *
 */
public class ValidationException extends RuntimeException {

	private static final long serialVersionUID = 1815627271659979265L;
	private ValidationError[] detals;
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
	 * @return the detals
	 */
	public ValidationError[] getDetals() {
		return detals;
	}

	/**
	 * @param detals the detals to set
	 */
	public void setDetals(ValidationError[] detals) {
		this.detals = detals;
	}

}
