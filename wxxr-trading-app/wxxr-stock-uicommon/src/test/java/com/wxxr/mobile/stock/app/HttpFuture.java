/**
 * 
 */
package com.wxxr.mobile.stock.app;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.wxxr.mobile.core.rpc.api.Request;
import com.wxxr.mobile.core.rpc.api.RequestCallback;
import com.wxxr.mobile.core.rpc.api.Response;

/**
 * @author neillin
 *
 */
public class HttpFuture<S extends Response,T extends Request<S>> implements RequestCallback<S,T>{

	private CountDownLatch latch = new CountDownLatch(1);
	private S response;
	private Throwable error;
	
	@Override
	public void onResponseReceived(T request, S response) {
		this.response = response;
		this.error = null;
		this.latch.countDown();
	}

	@Override
	public void onError(T request, Throwable exception) {
		this.error = exception;
		this.response = null;
		this.latch.countDown();
		
	}

	@Override
	public void onPrepare(T request) throws Exception {		
	}
	
	public S getResponse(long timeout, TimeUnit unit) throws Throwable {		
		if(!this.latch.await(timeout, unit)){
			throw new TimeoutException();
		}
		if(error != null){
			throw error;
		}
		return this.response;
	}
}
