/**
 * 
 */
package com.wxxr.mobile.core.ui.api;

import java.util.List;

/**
 * @author neillin
 *
 */
public interface IViewFrame extends IUIContainer{
	String getName();
	List<IView> getViews();
	IView getActiveView();
	IView getView(String name);
	void activateView(String name);
	void deactivateView(String name);
	void setVisible(boolean bool);
	void addView(IView view);
	boolean removeView(IView view);
}
