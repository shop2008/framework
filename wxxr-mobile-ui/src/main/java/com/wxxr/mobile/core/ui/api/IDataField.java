/**
 * 
 */
package com.wxxr.mobile.core.ui.api;


/**
 * @author neillin
 *
 */
public interface IDataField<T> extends IUIComponent{
	UIError getUIError();
	T getValue();
}
