/**
 * 
 */
package com.wxxr.mobile.android.app;

import com.wxxr.mobile.core.microkernel.api.IKernelContext;

/**
 * @author neillin
 *
 */
public interface IAndroidAppContext extends IKernelContext {
	@SuppressWarnings("rawtypes")
	IAndroidApplication getApplication();
}
