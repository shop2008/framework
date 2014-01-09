/**
 * 
 */
package com.wxxr.mobile.core.test;

import com.wxxr.mobile.core.microkernel.api.AbstractModule;
import com.wxxr.mobile.core.microkernel.api.IKernelContext;

/**
 * @author neillin
 *
 */
public class TestService3Module<C extends IKernelContext> extends AbstractModule<C> implements TestService3 {

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.test.TestService1#helloWorld()
	 */
	@Override
	public String helloWorld() {
		return "Hello world !";
	}


	@Override
	protected void initServiceDependency() {
		
	}

	@Override
	protected void startService() {
		context.registerService(TestService3.class, this);
	}

	@Override
	protected void stopService() {
		context.unregisterService(TestService3.class, this);
	}

}
