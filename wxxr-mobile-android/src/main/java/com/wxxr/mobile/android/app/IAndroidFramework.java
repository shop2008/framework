/**
 * 
 */
package com.wxxr.mobile.android.app;

import android.app.Application;

import com.wxxr.mobile.core.api.IApplication;
import com.wxxr.mobile.core.microkernel.api.IKernelModule;

/**
 * @author neillin
 *
 */
public interface IAndroidFramework<C extends IAndroidAppContext, M extends IKernelModule<C>> extends IApplication<C, M> {
	Application getAndroidApplication();
	String getMacIdentity();
	String getApplicationId();
	String getApplicationVersion();
	String getApplicationBuildNnumber();
}
