/**
 * 
 */
package com.wxxr.mobile.core.microkernel.api;

import java.util.concurrent.TimeUnit;

import com.wxxr.mobile.core.api.ApplicationFactory;
import com.wxxr.mobile.core.api.IApplication;

/**
 * @author neillin
 *
 */
public abstract class KUtils {
	public static IApplication<?, ?> getApplication() {
		return ApplicationFactory.getInstance().getApplication();
	}
	
	public static <T> T getService(Class<T> clazz) {
		return getApplication().getService(clazz);
	}
	
	public static void runOnUIThread(Runnable task, long delay, TimeUnit unit) {
		getApplication().runOnUIThread(task, delay, unit);
	}
	
	public static void runOnUIThread(Runnable task) {
		getApplication().runOnUIThread(task);
	}
	
	public static void invokeLater(Runnable task,long delay, TimeUnit unit){
		getApplication().invokeLater(task, delay, unit);
	}

}
