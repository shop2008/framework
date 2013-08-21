/**
 * 
 */
package com.wxxr.mobile.core.rpc.http.apache;

import java.util.concurrent.ExecutorService;

import org.apache.http.client.methods.HttpRequestBase;

import com.wxxr.mobile.core.rpc.http.api.HttpResponse;

/**
 * @author neillin
 *
 */
public interface IHttpClientContext {
	
	HttpResponse invoke(HttpRequestBase request) throws Exception;
	
	ExecutorService getExecutor();
}
