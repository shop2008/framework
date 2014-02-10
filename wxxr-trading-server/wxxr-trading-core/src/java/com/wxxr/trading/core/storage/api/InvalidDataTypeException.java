/**
 * 
 */
package com.wxxr.trading.core.storage.api;

import com.wxxr.persistence.service.PersistenceException;

/**
 * @author neillin
 *
 */
public class InvalidDataTypeException extends PersistenceException {

	private static final long serialVersionUID = -1041686394289607481L;

	public InvalidDataTypeException(String message) {
		super(message);
	}

}
