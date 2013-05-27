package com.wxxr.mobile.core.rpc.impl;

import java.util.concurrent.ExecutorService;

import android.app.Application;

import com.wxxr.mobile.android.app.IAndroidAppContext;
import com.wxxr.mobile.android.app.IAndroidFramework;
import com.wxxr.mobile.core.microkernel.api.AbstractMicroKernel;
import com.wxxr.mobile.core.microkernel.api.IKernelModule;

public abstract class MockApplication extends AbstractMicroKernel<IAndroidAppContext, IKernelModule<IAndroidAppContext>> implements IAndroidFramework<IAndroidAppContext, IKernelModule<IAndroidAppContext>> {

	
	private class AndroidAppContextImpl extends AbstractContext implements IAndroidAppContext {

		@Override
		public IAndroidFramework getApplication() {
			return MockApplication.this;
		}};
	
		private AndroidAppContextImpl context = new AndroidAppContextImpl();

	@Override
	public IAndroidAppContext getContext() {
		return context;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.api.IApplication#isInDebugMode()
	 */
	@Override
	public boolean isInDebugMode() {
		return true;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.android.app.IAndroidFramework#getAndroidApplication()
	 */
	@Override
	public Application getAndroidApplication() {
		return null;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.api.IApplication#getExecutor()
	 */
	@Override
	public ExecutorService getExecutor() {
		return getExecutorService();
	}
}
