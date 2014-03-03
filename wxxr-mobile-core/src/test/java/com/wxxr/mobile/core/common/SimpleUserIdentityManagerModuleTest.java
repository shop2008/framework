package com.wxxr.mobile.core.common;

import junit.framework.TestCase;

import com.wxxr.mobile.core.event.api.IEventRouter;
import com.wxxr.mobile.core.microkernel.api.IKernelContext;
import com.wxxr.mobile.core.microkernel.api.MockApplication;
import com.wxxr.mobile.core.security.api.IUserIdentityManager;
import com.wxxr.mobile.core.security.api.LoginAction;

public class SimpleUserIdentityManagerModuleTest extends TestCase {

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
		super.tearDown();
	}

	public void testGetUserId() throws Exception {
		this.app.registerKernelModule(new SimpleUserIdentityManagerModule<IKernelContext>());
		this.app.start();
		
		IUserIdentityManager usrMgr = this.app.getService(IUserIdentityManager.class);
		IEventRouter router = this.app.getService(IEventRouter.class);
		assertNotNull(usrMgr);
		assertNotNull(router);
		assertFalse(usrMgr.isUserAuthenticated());
		assertEquals(IUserIdentityManager.UNAUTHENTICATED_USER_ID, usrMgr.getUserId());
		router.routeEvent(new TestLoginEvent("testUser1", LoginAction.LOGOUT));
		assertFalse(usrMgr.isUserAuthenticated());
		assertEquals(IUserIdentityManager.UNAUTHENTICATED_USER_ID, usrMgr.getUserId());
		router.routeEvent(new TestLoginEvent("testUser1", LoginAction.LOGIN));
		assertTrue(usrMgr.isUserAuthenticated());
		assertEquals("testUser1", usrMgr.getUserId());
	
		router.routeEvent(new TestLoginEvent("testUser1", LoginAction.LOGOUT));
		assertFalse(usrMgr.isUserAuthenticated());
		assertEquals(IUserIdentityManager.UNAUTHENTICATED_USER_ID, usrMgr.getUserId());		
		
	}

}
