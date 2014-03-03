/**
 * 
 */
package com.wxxr.mobile.web.grabber.common;

import com.wxxr.mobile.core.async.api.Async;
import com.wxxr.mobile.core.async.api.IAsyncCallback;
import com.wxxr.mobile.core.rpc.api.Request;
import com.wxxr.mobile.core.rpc.api.RequestCallback;
import com.wxxr.mobile.core.rpc.http.api.HttpRequest;
import com.wxxr.mobile.core.rpc.http.api.HttpResponse;

/**
 * @author neillin
 *
 */
public class HttpRequestAsync implements Async<HttpResponse> {
	
	private final HttpRequest request;
	
	public HttpRequestAsync(HttpRequest req){
		if(req == null){
			throw new IllegalArgumentException("Invalid HttpRequest object : NULL !");
		}
		this.request = req;
	}

	@Override
	public void onResult(final IAsyncCallback<HttpResponse> callback) {
		callback.setCancellable(this.request);
		this.request.invokeAsync(new RequestCallback<HttpResponse, Request<HttpResponse>>() {
			
			@Override
			public void onResponseReceived(Request<HttpResponse> request,
					HttpResponse response) {
				callback.success(response);
			}
			
			@Override
			public void onError(Request<HttpResponse> request, Throwable exception) {
				callback.failed(exception);
			}

			@Override
			public void onPrepare(Request<HttpResponse> request)
					throws Exception {
				
			}
		});
	}

}
