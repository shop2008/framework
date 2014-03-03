/**
 * 
 */
package com.wxxr.mobile.core.ui.api;

/**
 * @author neillin
 *
 */
public interface IDomainValueModel<T> extends IValueEvaluator<T> {	
	ValidationError[] updateDomainValue(Object value);	
	boolean isUpdatable();
}
