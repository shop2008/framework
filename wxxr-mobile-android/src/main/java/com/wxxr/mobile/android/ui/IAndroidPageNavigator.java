/**
 * 
 */
package com.wxxr.mobile.android.ui;

import com.wxxr.mobile.core.ui.api.IPage;
import com.wxxr.mobile.core.ui.api.IPageNavigator;

/**
 * @author neillin
 *
 */
public interface IAndroidPageNavigator extends IPageNavigator {
	String PARAM_KEY_INTENT_FLAG = "INTENT_FLAG";
	
	void onPageCreate(IPage page, IBindableActivity activity);
	void onPageShow(IPage page);
	void onPageHide(IPage page);
	void onPageDetroy(IPage page);
	
	IBindableActivity getOnShowActivity();
	
	IBindableActivity getPageActivity(IPage page);
	
}
