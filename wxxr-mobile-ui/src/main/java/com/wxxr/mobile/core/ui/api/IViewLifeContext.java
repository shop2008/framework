/**
 * 
 */
package com.wxxr.mobile.core.ui.api;

/**
 * @author neillin
 *
 */
public interface IViewLifeContext {
	void viewCreated(IView view);
	void viewDestroy(IView view);
	void viewShow(IView view);
	void viewHidden(IView view);
	IPage getActivePage();
	IView getActiveView();
}
