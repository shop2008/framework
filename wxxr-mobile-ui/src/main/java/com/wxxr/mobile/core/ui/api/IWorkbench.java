/**
 * 
 */
package com.wxxr.mobile.core.ui.api;

import java.util.Map;

/**
 * @author neillin
 *
 */
public interface IWorkbench {
	public final static String HOME_PAGE_ID = "home";
	public final static String MESSAGE_BOX_ID = "messageBox";
	public final static String MESSAGE_BOX_MESSAGE_ID = "message";
	public final static String TOOL_BAR_VIEW_ID = "toolbarView";
	
	String[] getPageIds();
	String getActivePageId();
	IPage getPage(String pageId);
	void showPage(String pageId,Map<String, Object> params,IPageCallback callback);
	void showHomePage();
	void hidePage(String pageId);
	void showMessageBox(String message, Map<String, Object> params);
	IView createNInitializedView(String viewId);
}
