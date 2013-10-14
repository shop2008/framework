/**
 * 
 */
package com.wxxr.mobile.core.ui.api;

/**
 * @author neillin
 *
 */
public interface IDomainValueModel {
	
	void addListener(IDomainValueChangedListener listener);
	boolean removeListener(IDomainValueChangedListener listener);
	
	Object getValue();
	void updateValue(Object value) throws ValidationException;	
	boolean isUpdatable();
}
