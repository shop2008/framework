/**
 * 
 */
package com.wxxr.mobile.stock.app;

import org.codehaus.jackson.jaxrs.JacksonJsonProvider;

import com.wxxr.mobile.core.microkernel.api.IKernelContext;
import com.wxxr.mobile.core.rest.provider.GSONProvider;
import com.wxxr.mobile.core.rest.provider.XStreamProvider;
import com.wxxr.mobile.core.rpc.rest.ResteasyRestClientService;

/**
 * @author wangxuyang
 *
 */
public class MockRestClient extends ResteasyRestClientService{
	public void init(IKernelContext ctx){
		startup(ctx);
		/*getProviderFactory().register(XStreamProvider.class);
		getProviderFactory().register(GSONProvider.class);*/
		getProviderFactory().register(JacksonJsonProvider.class);
	}

}
