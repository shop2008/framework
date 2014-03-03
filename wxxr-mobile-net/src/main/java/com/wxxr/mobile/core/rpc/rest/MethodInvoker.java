package com.wxxr.mobile.core.rpc.rest;

import com.wxxr.mobile.core.async.api.IAsyncCallback;

public interface MethodInvoker<T>
{
	void invoke(Object[] args, IAsyncCallback<T> callback);
}
