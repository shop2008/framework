/**
 * 
 */
package com.wxxr.mobile.core.ui.api;

import java.util.concurrent.Future;

import com.wxxr.mobile.core.api.IProgressMonitor;
import com.wxxr.mobile.core.util.ICancellable;

/**
 * @author neillin
 *
 */
public interface IAsyncTaskControl {
	/**
	 * 
	 * @return ICancellable instance if the task could be cancelled 
	 */
	ICancellable getCancellable();
	Future<?> getFuture();
	void registerProgressMonitor(IProgressMonitor monitor);
	boolean unregisterProgressMonitor(IProgressMonitor monitor);
}
