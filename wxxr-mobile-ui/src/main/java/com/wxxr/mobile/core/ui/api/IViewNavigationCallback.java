/**
 * 
 */
package com.wxxr.mobile.core.ui.api;

/**
 * @author neillin
 *
 */
public interface IViewNavigationCallback {
	void onCreate(IView view);
	void onShow(IView view);
	void onHide(IView view);
	void onDestroy(IView view);
}
