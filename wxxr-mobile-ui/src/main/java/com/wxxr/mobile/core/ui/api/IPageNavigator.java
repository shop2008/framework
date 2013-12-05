/**
 * 
 */
package com.wxxr.mobile.core.ui.api;


/**
 * @author neillin
 *
 */
public interface IPageNavigator {
	void showPage(IPage page,IViewNavigationCallback callback);
	void hidePage(IPage page);
	void showView(IView view,IViewNavigationCallback callback);
	void hideView(IView view);
	IPage getCurrentActivePage();
	IDialog createDialog(IView view, Object handback);
	
	void registerNavigationListener(IViewNavigationListener listener);
	boolean unregisterNavigationListener(IViewNavigationListener listener);
}
