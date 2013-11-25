/**
 * 
 */
package com.wxxr.mobile.core.command.api;

import com.wxxr.mobile.core.microkernel.api.IKernelContext;

/**
 * @author neillin
 *
 */
public interface ICommandExecutionContext {
	IKernelContext getKernelContext();
}
