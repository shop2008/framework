/**
 * 
 */
package com.wxxr.mobile.android.app;

import java.util.concurrent.TimeUnit;

import com.wxxr.mobile.core.api.ApplicationFactory;
import com.wxxr.mobile.core.microkernel.api.KUtils;

/**
 * @author neillin
 *
 */
public class AppUtils {
	public static AndroidFramework<?, ?> getFramework() {
		return (AndroidFramework<?, ?>)KUtils.getApplication();
	}
	
	public static <T> T getService(Class<T> clazz) {
		return KUtils.getService(clazz);
	}
	
	public static void runOnUIThread(Runnable task, long delay, TimeUnit unit) {
		KUtils.runOnUIThread(task, delay, unit);
	}
	
	public static void runOnUIThread(Runnable task) {
		KUtils.runOnUIThread(task);
	}
	
	public static void invokeLater(Runnable task,long delay, TimeUnit unit){
		KUtils.invokeLater(task, delay, unit);
	}
}
