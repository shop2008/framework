package com.wxxr.mobile.core.ui.api;

import com.wxxr.mobile.core.microkernel.api.IKernelContext;

public interface IUIManagementContext {
	IKernelContext getKernelContext();
	IView createView(String name);
	IViewFrame createViewFrame(String name);
}
