/**
 * 
 */
package com.wxxr.mobile.android.ui;

import com.wxxr.mobile.android.app.AppUtils;
import com.wxxr.mobile.core.ui.api.IViewBinder;
import com.wxxr.mobile.core.ui.api.IViewDescriptor;
import com.wxxr.mobile.core.ui.api.IWorkbenchManager;
import com.wxxr.mobile.core.ui.api.TargetUISystem;

/**
 * @author neillin
 *
 */
public abstract class BindingUtils {
	/**
	 * @return
	 */
	public static IAndroidPageNavigator getNavigator() {
		return (IAndroidPageNavigator)AppUtils.getService(IAndroidWorkbenchManager.class).getPageNavigator();
	}
	
	public static IViewBinder getViewBinder() {
		return AppUtils.getService(IAndroidWorkbenchManager.class).getViewBinder();
	}

	/**
	 * @return
	 */
	public static IAndroidBindingDescriptor getBindingDescriptor(String viewId) {
		IViewDescriptor descriptor = AppUtils.getService(IWorkbenchManager.class).getViewDescriptor(viewId);
		IAndroidBindingDescriptor bdescriptor =  (IAndroidBindingDescriptor)descriptor.getBindingDescriptor(TargetUISystem.ANDROID);
		return bdescriptor;
	}

	public static String getMessage(String message){
		if(RUtils.isResourceIdURI(message)){
			int msgId = RUtils.getInstance().getResourceIdByURI(message);
			message = AppUtils.getFramework().getAndroidApplication().getResources().getString(msgId);
		}
		return message;
	}
}
