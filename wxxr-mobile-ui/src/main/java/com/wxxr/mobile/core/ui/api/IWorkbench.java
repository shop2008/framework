/**
 * 
 */
package com.wxxr.mobile.core.ui.api;

import java.util.List;
import java.util.Map;

/**
 * @author neillin
 *
 */
public interface IWorkbench {
	List<String> getPageIds();
	String getActivePageId();
	IPage getPage(String pageId);
	void showPage(String pageId,Map<String, String> params,IPageCallback callback);
	void showHomePage();
	void hidePage(String pageId);
}
