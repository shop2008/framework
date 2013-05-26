package com.wxxr.mobile.core.rpc.impl;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import com.wxxr.mobile.android.app.IAndroidAppContext;
import com.wxxr.mobile.android.app.IAndroidApplication;
import com.wxxr.mobile.core.microkernel.api.AbstractMicroKernel;
import com.wxxr.mobile.core.microkernel.api.IKernelContext;
import com.wxxr.mobile.core.microkernel.api.IKernelModule;
import com.wxxr.mobile.core.microkernel.api.IKernelServiceListener;
import com.wxxr.mobile.core.microkernel.api.IMicroKernel;
import com.wxxr.mobile.core.microkernel.api.IServiceAvailableCallback;
import com.wxxr.mobile.core.microkernel.api.ServiceFuture;

public abstract class MockApplication extends AbstractMicroKernel<IAndroidAppContext, IKernelModule<IAndroidAppContext>>  {

	
	private IAndroidAppContext context = new IAndroidAppContext() {
		private IKernelContext delegate = getAbstractContext();

		/**
		 * @param interfaceClazz
		 * @param handler
		 * @see com.wxxr.mobile.core.microkernel.api.IKernelContext#registerService(java.lang.Class, java.lang.Object)
		 */
		public <T> void registerService(Class<T> interfaceClazz, T handler) {
			delegate.registerService(interfaceClazz, handler);
		}

		/**
		 * @param interfaceClazz
		 * @param handler
		 * @see com.wxxr.mobile.core.microkernel.api.IKernelContext#unregisterService(java.lang.Class, java.lang.Object)
		 */
		public <T> void unregisterService(Class<T> interfaceClazz, T handler) {
			delegate.unregisterService(interfaceClazz, handler);
		}

		/**
		 * @param interfaceClazz
		 * @return
		 * @see com.wxxr.mobile.core.microkernel.api.IKernelContext#getService(java.lang.Class)
		 */
		public <T> T getService(Class<T> interfaceClazz) {
			return delegate.getService(interfaceClazz);
		}

		/**
		 * @param interfaceClazz
		 * @return
		 * @see com.wxxr.mobile.core.microkernel.api.IKernelContext#getServiceAsync(java.lang.Class)
		 */
		public <T> ServiceFuture<T> getServiceAsync(Class<T> interfaceClazz) {
			return delegate.getServiceAsync(interfaceClazz);
		}

		/**
		 * @param interfaceClazz
		 * @param callback
		 * @see com.wxxr.mobile.core.microkernel.api.IKernelContext#checkServiceAvailable(java.lang.Class, com.wxxr.mobile.core.microkernel.api.IServiceAvailableCallback)
		 */
		public <T> void checkServiceAvailable(Class<T> interfaceClazz,
				IServiceAvailableCallback<T> callback) {
			delegate.checkServiceAvailable(interfaceClazz, callback);
		}

		/**
		 * @param listener
		 * @see com.wxxr.mobile.core.microkernel.api.IKernelContext#addKernelServiceListener(com.wxxr.mobile.core.microkernel.api.IKernelServiceListener)
		 */
		public void addKernelServiceListener(IKernelServiceListener listener) {
			delegate.addKernelServiceListener(listener);
		}

		/**
		 * @param listener
		 * @return
		 * @see com.wxxr.mobile.core.microkernel.api.IKernelContext#removeKernelServiceListener(com.wxxr.mobile.core.microkernel.api.IKernelServiceListener)
		 */
		public boolean removeKernelServiceListener(
				IKernelServiceListener listener) {
			return delegate.removeKernelServiceListener(listener);
		}

		/**
		 * @param key
		 * @param value
		 * @see com.wxxr.mobile.core.microkernel.api.IKernelContext#setAttribute(java.lang.String, java.lang.Object)
		 */
		public void setAttribute(String key, Object value) {
			delegate.setAttribute(key, value);
		}

		/**
		 * @param key
		 * @return
		 * @see com.wxxr.mobile.core.microkernel.api.IKernelContext#removeAttribute(java.lang.String)
		 */
		public Object removeAttribute(String key) {
			return delegate.removeAttribute(key);
		}

		/**
		 * @param key
		 * @return
		 * @see com.wxxr.mobile.core.microkernel.api.IKernelContext#getAttribute(java.lang.String)
		 */
		public Object getAttribute(String key) {
			return delegate.getAttribute(key);
		}

		@Override
		public ExecutorService getExecutor() {
			return delegate.getExecutor();
		}

		/* (non-Javadoc)
		 * @see com.wxxr.mobile.android.app.IAndroidAppContext#getApplication()
		 */
		@Override
		public IAndroidApplication getApplication() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public IMicroKernel getKernel() {
			return MockApplication.this;
		}

		@Override
		public void invokeLater(Runnable task, long delay, TimeUnit unit) {
			delegate.invokeLater(task, delay, unit);
		}
		
	};
	

	@Override
	public IAndroidAppContext getContext() {
		return context;
	}
}
