/**
 * 
 */
package com.wxxr.mobile.stock.app;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

import com.wxxr.mobile.android.app.IAndroidFramework;
import com.wxxr.mobile.android.http.HttpRpcServiceModule;
import com.wxxr.mobile.core.microkernel.api.IKernelServiceListener;
import com.wxxr.mobile.core.microkernel.api.IMicroKernel;
import com.wxxr.mobile.core.microkernel.api.IServiceAvailableCallback;
import com.wxxr.mobile.core.microkernel.api.ServiceFuture;
import com.wxxr.mobile.core.rpc.rest.RestEasyClientModule;
import com.wxxr.mobile.core.util.ICancellable;
import com.wxxr.mobile.stock.app.service.impl.ArticleManagementServiceImpl;

/**
 * @author wangxuyang
 *
 */
public class ArticleServiceTest  extends ArticleManagementServiceImpl{
	private IStockAppContext context = new IStockAppContext() {
		
		public <T> void unregisterService(Class<T> interfaceClazz, T handler) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void setAttribute(String key, Object value) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public boolean removeKernelServiceListener(IKernelServiceListener listener) {
			// TODO Auto-generated method stub
			return false;
		}
		
		@Override
		public Object removeAttribute(String key) {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public <T> void registerService(Class<T> interfaceClazz, T handler) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public ICancellable invokeLater(Runnable task, long delay, TimeUnit unit) {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public <T> ServiceFuture<T> getServiceAsync(Class<T> interfaceClazz) {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public <T> T getService(Class<T> interfaceClazz) {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public IMicroKernel getKernel() {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public ExecutorService getExecutor() {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public Object getAttribute(String key) {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public <T> void checkServiceAvailable(Class<T> interfaceClazz,
				IServiceAvailableCallback<T> callback) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void addKernelServiceListener(IKernelServiceListener listener) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public IAndroidFramework getApplication() {
			// TODO Auto-generated method stub
			return null;
		}
	};
	@Test
	public void testFetchArticle() throws Exception {
		HttpRpcServiceModule<IStockAppContext> m = new HttpRpcServiceModule<IStockAppContext>();
		m.setEnablegzip(false);
		m.setConnectionPoolSize(30);
		/*registerKernelModule(m);
		registerKernelModule(new RestEasyClientModule<IStockAppContext>());*/
		
	}
}
