package com.wxxr.mobile.core.ui.api;

import com.wxxr.mobile.core.microkernel.api.IKernelContext;

public interface IWorkbenchRTContext {
	IKernelContext getKernelContext();
	IWorkbenchManager getWorkbenchManager();
	Object getDomainModel(String modelName);
}
