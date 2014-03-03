/**
 * 
 */
package com.wxxr.mobile.core.rpc.api;

import com.wxxr.mobile.core.rpc.http.api.HttpRequest;
import com.wxxr.mobile.core.rpc.http.api.HttpResponse;

/**
 * @author neillin
 *
 */
public interface IRequestTimeoutControl {
	void registerRequest(final HttpRequest request, final RequestCallback<HttpResponse, Request<HttpResponse>> callback) throws Exception;
	boolean unregisterRequest(final HttpRequest request);
	void notifySuccess(final HttpRequest request, HttpResponse response);
	void notifyFailed(final HttpRequest request, Throwable error);
}
