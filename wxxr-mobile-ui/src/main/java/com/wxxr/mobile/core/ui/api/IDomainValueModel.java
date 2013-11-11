/**
 * 
 */
package com.wxxr.mobile.core.ui.api;

/**
 * @author neillin
 *
 */
public interface IDomainValueModel<T> extends IValueEvaluator<T> {	
	T getValue();
	ValidationError[] updateValue(Object value);	
	boolean isUpdatable();
}
