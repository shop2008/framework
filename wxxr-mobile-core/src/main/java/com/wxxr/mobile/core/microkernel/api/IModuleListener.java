/**
 * 
 */
package com.wxxr.mobile.core.microkernel.api;

/**
 * @author neillin
 *
 */
public interface IModuleListener {
	void moduleRegistered(IKernelModule<?> module);
	void moduleUnregistered(IKernelModule<?> module);
	void kernelStarting();
	void kernelStarted();
	void kernelStopping();
	void kernelStopped();
}
