/**
 * 
 */
package com.wxxr.mobile.core.ui.api;

import java.util.Map;

/**
 * @author neillin
 *
 */
public interface IPageNavigator {
	void showPage(IPage page,Map<String, Object> params,IPageCallback callback);
	void hidePage(IPage page);
	void showView(IView view, boolean add2BackStack);
	void hideView(IView view);
	IPage getCurrentActivePage();
	IDialog createDialog(IView view, Object handback);
}
