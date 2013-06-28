/**
 * 
 */
package com.wxxr.mobile.core.ui.api;

import java.util.List;

/**
 * @author neillin
 *
 */
public interface IPage extends IUIContainer,IBindable {
	IViewFrame getViewFrame(String name);
	IViewFrame getMainViewFrame();
	List<IViewFrame> getAllFrames();
	void showView(String viewName);
	void hideView(String viewName);
}
