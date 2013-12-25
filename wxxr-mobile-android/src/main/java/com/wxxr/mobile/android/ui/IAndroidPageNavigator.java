/**
 * 
 */
package com.wxxr.mobile.android.ui;

import android.content.Intent;

import com.wxxr.mobile.core.ui.api.IPage;
import com.wxxr.mobile.core.ui.api.IPageNavigator;
import com.wxxr.mobile.core.ui.api.IView;

/**
 * @author neillin
 *
 */
public interface IAndroidPageNavigator extends IPageNavigator {
	String PARAM_KEY_INTENT_FLAG = "INTENT_FLAG";
	String PARAM_KEY_ADD2BACKSTACK = "add2BackStack";
	String PARAM_KEY_INTENT_PREFIX = "INTENT_";
	
	void onViewCreate(IView view, IBindableActivity activity);
	void onViewShow(IView view);
	void onViewHide(IView view);
	void onViewDetroy(IView view);
	
	IBindableActivity getOnShowActivity();
	
	IBindableActivity getPageActivity(IPage page);
	
	void startActivity(Intent intent);
	
	void startService(Intent intent);
}
