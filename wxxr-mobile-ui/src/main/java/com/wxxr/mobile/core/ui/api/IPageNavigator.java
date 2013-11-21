/**
 * 
 */
package com.wxxr.mobile.core.ui.api;


/**
 * @author neillin
 *
 */
public interface IPageNavigator {
	void showPage(IPage page,IPageCallback callback);
	void hidePage(IPage page);
	void showView(IView view);
	void hideView(IView view);
	IPage getCurrentActivePage();
	IDialog createDialog(IView view, Object handback);
}
