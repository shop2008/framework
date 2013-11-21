/**
 * 
 */
package com.wxxr.mobile.stock.app;

import com.wxxr.mobile.core.microkernel.api.IKernelContext;
import com.wxxr.mobile.core.rpc.rest.ResteasyRestClientService;

/**
 * @author wangxuyang
 *
 */
public class MockRestClient extends ResteasyRestClientService{
	public void init(IKernelContext ctx){
		startup(ctx);
	}

}
