package com.wxxr.mobile.stock.app;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.wxxr.mobile.core.microkernel.api.AbstractMicroKernel;
import com.wxxr.mobile.core.microkernel.api.IKernelContext;
import com.wxxr.mobile.core.microkernel.api.IKernelModule;

public abstract class MockApplication extends AbstractMicroKernel<IKernelContext, IKernelModule<IKernelContext>> {

	private ExecutorService executor = Executors.newSingleThreadExecutor();
		
	private class AndroidAppContextImpl extends AbstractContext {

		@Override
		public void invokeLater(Runnable task) {
			executor.execute(task);
		}

	};
	
		private AndroidAppContextImpl context = new AndroidAppContextImpl();

	@Override
	public IKernelContext getContext() {
		return context;
	}

}
