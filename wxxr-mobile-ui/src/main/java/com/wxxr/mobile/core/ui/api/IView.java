/**
 * 
 */
package com.wxxr.mobile.core.ui.api;

import java.util.List;

/**
 * @author neillin
 *
 */
public interface IView extends IUIContainer<IUIComponent>,IBindable<IView>{
	String getName();
		
	boolean isActive();
	
	void show();
	
	void hide();
	
	List<ValidationError> getErrors();
	
	void show(boolean backable);
	
	ISelectionProvider getSelectionProvider();

}
