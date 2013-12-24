/**
 * 
 */
package com.wxxr.mobile.core.microkernel.api;

/**
 * @author wangyan
 *
 */
public interface IStatefulService {
	 void init(IKernelContext context);
	 void startService();
	 void stopService();
}
