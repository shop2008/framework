/**
 * 
 */
package com.wxxr.mobile.core.command.api;

/**
 * @author neillin
 *
 */
public class CommandException extends RuntimeException {

	private static final long serialVersionUID = 1373422218353819374L;

	public CommandException() {
		super();
	}

	public CommandException(String message, Throwable cause) {
		super(message, cause);
	}

	public CommandException(String message) {
		super(message);
	}

	public CommandException(Throwable cause) {
		super(cause);
	}

}
