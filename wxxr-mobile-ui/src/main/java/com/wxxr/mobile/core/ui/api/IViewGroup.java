/**
 * 
 */
package com.wxxr.mobile.core.ui.api;


/**
 * @author neillin
 *
 */
public interface IViewGroup extends IUIContainer<IView>{
	String getName();
	String[] getViewIds();
	String getActiveViewId();
	void activateView(String name);
	void deactivateView(String name);
	IView getView(String name);
	boolean hasView(String name);
}