/**
 * 
 */
package com.wxxr.mobile.core.ui.api;

/**
 * @author neillin
 *
 */
public interface IPage {
	String getPageId();
	String[] getViewIds();
	String getActiveViewId();
	void addView(IView view, Object layoutConstrains);
	IView removeView(String viewId);
	IView getView(String viewId);
	void activateView(String viewId);
}
