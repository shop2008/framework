/**
 * 
 */
package com.wxxr.mobile.core.ui.api;


/**
 * @author neillin
 *
 */
public interface IDataField<T> extends IUIComponent{
	ValidationError[] getValidationErrors();
	T getValue();
	AttributeKey<T> getValueKey();
	void setValue(T val);
}
