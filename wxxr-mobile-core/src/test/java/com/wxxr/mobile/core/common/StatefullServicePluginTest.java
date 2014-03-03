package com.wxxr.mobile.core.common;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import junit.framework.TestCase;

import com.wxxr.mobile.core.event.api.IEventRouter;
import com.wxxr.mobile.core.microkernel.api.IKernelContext;
import com.wxxr.mobile.core.microkernel.api.MockApplication;
import com.wxxr.mobile.core.security.api.LoginAction;
import com.wxxr.mobile.core.test.TestCounter;
import com.wxxr.mobile.core.test.TestCounterModule;

public class StatefullServicePluginTest extends TestCase {

	private MockApplication app;
	
	
	protected void setUp() throws Exception {
		super.setUp();
		this.app = new MockApplication() {
			
			@Override
			protected void initModules() {
				
			}
			
		};
	}

	protected void tearDown() throws Exception {
		if(this.app != null){
			this.app.stop();
			this.app = null;
		}
	}

	public void testBuildServiceHandler() throws Exception {
		this.app.addServiceFeaturePlugin(new StatefullServicePlugin());
		this.app.registerKernelModule(new SimpleUserIdentityManagerModule<IKernelContext>());
		this.app.registerKernelModule(new UserBasedSessionManagerModule<IKernelContext>());
		this.app.registerKernelModule(new TestCounterModule<IKernelContext>());
		this.app.start();
		
		IEventRouter router = this.app.getService(IEventRouter.class);
		assertNotNull(router);
		
		TestCounter counter = this.app.getService(TestCounter.class);
		assertNotNull(counter);
		assertEquals(0, counter.getCount());
		counter.countUp();
		assertEquals(1, counter.getCount());
		counter.countUp();
		assertEquals(2, counter.getCount());
		
		router.routeEvent(new TestLoginEvent("testUser1", LoginAction.LOGIN));
		Thread.sleep(100L);

		assertEquals(0, counter.getCount());
		counter.countUp();
		assertEquals(1, counter.getCount());
		counter.countUp();
		assertEquals(2, counter.getCount());
		
		router.routeEvent(new TestLoginEvent("testUser1", LoginAction.LOGOUT));
		Thread.sleep(100L);
		
		assertEquals(0, counter.getCount());
		counter.countUp();
		assertEquals(1, counter.getCount());
		counter.countUp();
		assertEquals(2, counter.getCount());
	}

}
