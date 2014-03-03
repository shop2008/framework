package com.wxxr.mobile.core.microkernel.api;

import junit.framework.TestCase;

import com.wxxr.mobile.core.test.TestModule;
import com.wxxr.mobile.core.test.TestService1;
import com.wxxr.mobile.core.test.TestService2;
import com.wxxr.mobile.core.test.TestService3;
import com.wxxr.mobile.core.test.TestService3Module;
import com.wxxr.mobile.core.test.TestServicePlugin;

public class AbstractMicroKernelTest extends TestCase {
	
	private MockApplication testApp;

	protected void setUp() throws Exception {
		super.setUp();
		testApp = new MockApplication() {
			
			@Override
			protected void initModules() {
				registerKernelModule(new TestModule<IKernelContext>());
			}
			
		};
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		if(testApp != null){
			testApp.stop();
		}
	}

	public void testService() throws Exception {
		testApp.start();
		TestService1 service1 = testApp.getService(TestService1.class);
		assertNotNull(service1);
		assertEquals("Hello world !", service1.helloWorld());
		
		TestService2 service2 = testApp.getService(TestService2.class);
		assertNotNull(service2);
		assertEquals("Hello world !", service2.helloWorld());
		assertEquals("Hi there !", service2.sayHi());
		
		TestService3 service3 = testApp.getService(TestService3.class);
		assertNull(service3);
		
	}
	
	public void testServiceDecorator() throws Exception {
		TestServicePlugin testDecorator = new TestServicePlugin();
		testApp.addServiceFeaturePlugin(testDecorator);
		assertTrue(testDecorator.isInitialized());
		testApp.start();
		
		TestService1 service1 = testApp.getService(TestService1.class);
		assertNotNull(service1);
		assertEquals("Hello world !", service1.helloWorld());

		testApp.registerKernelModule(new TestService3Module<IKernelContext>());
		TestService3 service3 = testApp.getService(TestService3.class);
		assertNotNull(service3);
		assertEquals("{Hello world !}", service3.helloWorld());
	}

}
