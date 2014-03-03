/**
 * 
 */
package com.wxxr.mobile.core.async.api;

/**
 * @author neillin
 *
 */
public interface InProgressTask {
	void registerProgressMonitor(IProgressMonitor monitor);
	boolean unregisterProgressMonitor(IProgressMonitor monitor);
}
