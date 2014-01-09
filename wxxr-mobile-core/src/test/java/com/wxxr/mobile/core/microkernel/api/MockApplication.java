package com.wxxr.mobile.core.microkernel.api;
public abstract class MockApplication extends AbstractMicroKernel<IKernelContext, IKernelModule<IKernelContext>> {

	
	private class TestAppContextImpl extends AbstractContext {

	};
	
	private TestAppContextImpl context = new TestAppContextImpl();

	@Override
	public IKernelContext getContext() {
		return context;
	}

}