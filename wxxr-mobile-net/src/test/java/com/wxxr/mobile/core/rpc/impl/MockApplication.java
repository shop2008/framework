package com.wxxr.mobile.core.rpc.impl;

import com.wxxr.mobile.core.microkernel.api.AbstractMicroKernel;
import com.wxxr.mobile.core.microkernel.api.IKernelContext;
import com.wxxr.mobile.core.microkernel.api.IKernelModule;

public abstract class MockApplication extends AbstractMicroKernel<IKernelContext, IKernelModule<IKernelContext>> {

	
	private class AndroidAppContextImpl extends AbstractContext {

	};
	
		private AndroidAppContextImpl context = new AndroidAppContextImpl();

	@Override
	public IKernelContext getContext() {
		return context;
	}

}
