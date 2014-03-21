package com.wxxr.mobile.stock.app.service.handler;

import com.wxxr.mobile.core.async.api.IAsyncCallback;
import com.wxxr.mobile.core.async.api.ICancellable;
import com.wxxr.mobile.core.command.api.ICommand;
import com.wxxr.mobile.core.command.api.ICommandExecutionContext;
import com.wxxr.mobile.core.command.api.ICommandHandler;
import com.wxxr.mobile.core.microkernel.api.IKernelContext;
import com.wxxr.mobile.core.rpc.http.api.IRestProxyService;


public abstract class BasicCommandHandler<V, C extends ICommand<V>> implements ICommandHandler<V,C>{
	
	protected static abstract class DelegateCallback<S,T> implements IAsyncCallback<S> {
		private final IAsyncCallback<T> delegate;
		
		public DelegateCallback(IAsyncCallback<T> cb){
			this.delegate = cb;
		}

		/**
		 * @param result
		 * @see com.wxxr.mobile.core.async.api.IAsyncCallback#success(java.lang.Object)
		 */
		public void success(S result) {
			delegate.success(getTargetValue(result));
		}
		
		protected abstract T getTargetValue(S value);

		/**
		 * @param cause
		 * @see com.wxxr.mobile.core.async.api.IAsyncCallback#failed(java.lang.Throwable)
		 */
		public void failed(Throwable cause) {
			delegate.failed(cause);
		}

		/**
		 * 
		 * @see com.wxxr.mobile.core.async.api.IAsyncCallback#cancelled()
		 */
		public void cancelled() {
			delegate.cancelled();
		}

		/**
		 * @param cancellable
		 * @see com.wxxr.mobile.core.async.api.IAsyncCallback#setCancellable(com.wxxr.mobile.core.async.api.ICancellable)
		 */
		public void setCancellable(ICancellable cancellable) {
			delegate.setCancellable(cancellable);
		}
	}
    private ICommandExecutionContext  context;
    
    @Override
    public void destroy() {
        
    }
    protected <T> T getRestService(Class<T> c, Class<?> ifaceRest) {
        return getKernelContext().getService(IRestProxyService.class).getRestService(c,ifaceRest);
    }
    
    protected <T> T getService(Class<T> iface) {
        return getKernelContext().getService(iface);
    }
	/**
	 * @return
	 */
	protected IKernelContext getKernelContext() {
		return context.getKernelContext();
	}


    @Override
    public void init(ICommandExecutionContext p_context) {
        context=p_context;
    }

    protected ICommandExecutionContext getContext() {
    	return this.context;
    }
}


