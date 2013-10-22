/**
 * 
 */
package com.wxxr.mobile.android.ui;

import android.content.Context;
import android.view.View;

import com.wxxr.mobile.core.ui.api.IBindingContext;
import com.wxxr.mobile.core.ui.api.IWorkbenchManager;
import com.wxxr.mobile.core.ui.api.IWorkbenchRTContext;

/**
 * @author neillin
 *
 */
public interface IAndroidBindingContext extends IBindingContext {
	Context getUIContext();
	View getBindingControl();

}
