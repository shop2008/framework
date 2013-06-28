/**
 * 
 */
package com.wxxr.mobile.core.ui.api;

import java.util.List;

/**
 * @author neillin
 *
 */
public interface IView extends IUIContainer {
	String getName();
	
	IDataField getDataField(String name);
	
	List<IDataField> getDataFields();
	
	boolean isActive();
	
	void activate();
	
	void deactivate();
	
	List<UIError> getErrors();
}
