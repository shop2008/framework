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
	ValidationError[] updateValue(Object value);	
	boolean isUpdatable();
}
