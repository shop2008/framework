/**
 * 
 */
package com.wxxr.mobile.core.ui.api;

import java.util.List;

/**
 * @author neillin
 *
 */
public interface IUIContainer<C extends IUIComponent> extends IUIComponent,Iterable<IUIComponent> {
	
	int getChildrenCount();
	
	C getChild(int idx);
	
	String[] getChildIds();
		
	C getChild(String name);
	
	<T extends C> List<T> getChildren(Class<T> clazz);
	
	<T extends C> T getChild(String name, Class<T> clazz);

	IUIContainer<C> addChild(IUIComponent child);
	
	IUIContainer<C> removeChild(IUIComponent child);
	
	IUIContainer<C> removeChild(String name);
}
