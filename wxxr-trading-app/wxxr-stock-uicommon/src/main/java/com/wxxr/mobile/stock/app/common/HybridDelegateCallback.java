package com.wxxr.mobile.stock.app.common;

import com.wxxr.mobile.core.async.api.IAsyncCallback;
import com.wxxr.mobile.core.async.api.ICancellable;

public abstract class HybridDelegateCallback<S,T> implements IAsyncCallback<S> {
	
	private final IAsyncCallback<T> delegate;
	
	public HybridDelegateCallback(IAsyncCallback<T> cb){
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
