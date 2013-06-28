/**
 * 
 */
package com.wxxr.mobile.android.ui;

import com.wxxr.mobile.android.app.IAndroidAppContext;
import com.wxxr.mobile.core.ui.api.IPage;

/**
 * @author neillin
 *
 */
public interface IBindingContext {
	IAndroidAppContext getServiceContext();
	IPage getBindingPage();
	BindableActivity getBindingActivity();
}
