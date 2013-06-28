/**
 * 
 */
package com.wxxr.mobile.core.ui.api;

import java.util.List;

/**
 * @author neillin
 *
 */
public interface IUIContainer extends IUIComponent,Iterable<IUIComponent> {
	
	int getChildrenCount();
	
	IUIComponent getChild(int idx);
	
	void notifyDataChanged(DataChangedEvent event);
	
	IUIComponent getChild(String name);
	
	<T> List<T> getChildren(Class<T> clazz);
	
	<T> T getChild(String name, Class<T> clazz);

}
