/**
 * 
 */
package com.wxxr.mobile.core.ui.api;

import java.util.List;


/**
 * A page could map to a android activity, and the content view of the activity 
 * @author neillin
 *
 */
public interface IPage extends IView {
	IViewGroup getViewGroup(String grpName);
	List<IViewGroup> getAllViewGroups();
	IView getView(String viewId);
	IViewGroup createViewGroup(String grpName);
	void showView(String viewId,boolean backable);
	void hideView(String viewId);
}
