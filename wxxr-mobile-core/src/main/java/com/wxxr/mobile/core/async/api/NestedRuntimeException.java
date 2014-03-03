/**
 * 
 */
package com.wxxr.mobile.core.async.api;

/**
 * @author neillin
 *
 */
public class NestedRuntimeException extends RuntimeException {

	private static final long serialVersionUID = 310419493916078442L;

	/**
	 * @param throwable
	 */
	public NestedRuntimeException(Throwable throwable) {
		super(throwable);
	}

}
