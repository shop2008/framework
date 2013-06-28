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
	void showPage(IPage page,Map<String, String> params,IPageCallback callback);
	void hidePage(IPage page);
	IPage getCurrentActivePage();
}
