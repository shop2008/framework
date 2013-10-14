/**
 * 
 */
package com.wxxr.mobile.core.ui.api;

/**
 * @author neillin
 *
 */
public interface IWritable {
	void setValue(Object value);
	UIError getValidationError();
}
