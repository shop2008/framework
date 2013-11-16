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
	public final static String HOME_PAGE_ID = UIConstants.HOME_PAGE_ID;
	public final static String MESSAGE_BOX_ID = UIConstants.MESSAGE_BOX_ID;
	public final static String MESSAGE_BOX_MESSAGE_ID = UIConstants.MESSAGEBOX_ATTRIBUTE_MESSAGE;
	public final static String TOOL_BAR_VIEW_ID = UIConstants.TOOL_BAR_VIEW_ID;
	public final static String PROGRESSMONITOR_DIALOG_ID = UIConstants.PROGRESSMONITOR_DIALOG_ID;

	
	String[] getPageIds();
	String getActivePageId();
	IPage getPage(String pageId);
	void showPage(String pageId,Map<String, Object> params,IPageCallback callback);
	void showHomePage();
	void hidePage(String pageId);
	IView createNInitializedView(String viewId);
	IDialog createDialog(String viewId, Map<String, Object> params);
	ISelectionService getSelectionService();
}
