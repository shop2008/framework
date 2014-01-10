/**
 * 
 */
package com.wxxr.mobile.core.api;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import com.wxxr.mobile.core.microkernel.api.IKernelContext;
import com.wxxr.mobile.core.microkernel.api.IKernelModule;
import com.wxxr.mobile.core.microkernel.api.IMicroKernel;

/**
 * @author neillin
 *
 */
public interface IApplication<C extends IKernelContext, M extends IKernelModule<C>> extends IMicroKernel<C, M> {
	
	ExecutorService getExecutor();
	
	void runOnUIThread(Runnable task, long delay, TimeUnit unit);
	
	void runOnUIThread(Runnable task);
	
	boolean isInDebugMode();
	
	boolean isCurrentUIThread();
	
	
}
