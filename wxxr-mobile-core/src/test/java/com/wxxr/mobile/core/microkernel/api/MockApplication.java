package com.wxxr.mobile.core.microkernel.api;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public abstract class MockApplication extends AbstractMicroKernel<IKernelContext, IKernelModule<IKernelContext>> {

	
	private ExecutorService executor = Executors.newSingleThreadExecutor();
	
	private class TestAppContextImpl extends AbstractContext {

		@Override
		public void invokeLater(Runnable task) {
			executor.execute(task);
		}

	};
	
	private TestAppContextImpl context = new TestAppContextImpl();

	@Override
	public IKernelContext getContext() {
		return context;
	}

}