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
public class TestModule<C extends IKernelContext> extends AbstractModule<C> implements TestService2 {

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.test.TestService1#helloWorld()
	 */
	@Override
	public String helloWorld() {
		return "Hello world !";
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.test.TestService2#sayHi()
	 */
	@Override
	public String sayHi() {
		return "Hi there !";
	}

	@Override
	protected void initServiceDependency() {
		
	}

	@Override
	protected void startService() {
		context.registerService(TestService2.class, this);
	}

	@Override
	protected void stopService() {
		context.unregisterService(TestService2.class, this);
	}

}
