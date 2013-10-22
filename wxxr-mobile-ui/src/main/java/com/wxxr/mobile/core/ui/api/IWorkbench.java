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
	
	String[] getPageIds();
	String getActivePageId();
	IPage getPage(String pageId);
	void showPage(String pageId,Map<String, String> params,IPageCallback callback);
	void showHomePage();
	void hidePage(String pageId);
	void showMessageBox(String message, Map<String, String> params);
}
