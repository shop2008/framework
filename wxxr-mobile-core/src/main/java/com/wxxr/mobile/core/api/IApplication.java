/**
 * 
 */
package com.wxxr.mobile.core.api;

import java.util.concurrent.ExecutorService;

import com.wxxr.mobile.core.microkernel.api.IKernelContext;
import com.wxxr.mobile.core.microkernel.api.IKernelModule;
import com.wxxr.mobile.core.microkernel.api.IMicroKernel;
import com.wxxr.mobile.core.microkernel.api.IServiceAvailableCallback;
import com.wxxr.mobile.core.microkernel.api.ServiceFuture;

/**
 * @author neillin
 *
 */
public interface IApplication<C extends IKernelContext, M extends IKernelModule<C>> extends IMicroKernel<C, M> {
	
	ExecutorService getExecutor();
	
	boolean isInDebugMode();
	
	
}
