/**
 * 
 */
package com.wxxr.mobile.android.app;

import java.util.concurrent.TimeUnit;

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
	
	public static void runOnUIThread(Runnable task, long delay, TimeUnit unit) {
		getFramework().runOnUIThread(task, delay, unit);
	}
	
	public static void runOnUIThread(Runnable task) {
		getFramework().runOnUIThread(task);
	}
	
	public static void invokeLater(Runnable task,long delay, TimeUnit unit){
		getFramework().invokeLater(task, delay, unit);
	}
}
