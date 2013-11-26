/**
 * 
 */
package com.wxxr.mobile.core.microkernel.api;

/**
 * @author neillin
 *
 */
public interface IKernelComponent {
	void init(IKernelContext context);
	void destroy();
}
