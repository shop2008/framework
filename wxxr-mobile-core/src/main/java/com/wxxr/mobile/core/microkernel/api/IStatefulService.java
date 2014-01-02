/**
 * 
 */
package com.wxxr.mobile.core.microkernel.api;

/**
 * @author wangyan
 *
 */
public interface IStatefulService<T extends IKernelContext> {
	 void init(T context);
	 void startService();
	 void stopService();
}
