/**
 * 
 */
package com.wxxr.mobile.android.app;

import com.wxxr.mobile.core.api.ApplicationFactory;

/**
 * @author neillin
 *
 */
public class AppUtils {
	public static AndroidFramework<?, ?> getFramework() {
		return (AndroidFramework<?,?>)ApplicationFactory.getInstance().getApplication();
	}
	
	public static <T> T getService(Class<T> clazz) {
		return getFramework().getService(clazz);
	}
	
	
}
