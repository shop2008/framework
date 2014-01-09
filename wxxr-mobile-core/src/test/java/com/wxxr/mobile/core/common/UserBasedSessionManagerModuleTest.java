package com.wxxr.mobile.core.common;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.wxxr.mobile.core.event.api.IEventRouter;
import com.wxxr.mobile.core.microkernel.api.IKernelContext;
import com.wxxr.mobile.core.microkernel.api.MockApplication;
import com.wxxr.mobile.core.security.api.LoginAction;
import com.wxxr.mobile.core.session.api.ISession;
import com.wxxr.mobile.core.session.api.ISessionManager;

import junit.framework.TestCase;

public class UserBasedSessionManagerModuleTest extends TestCase {
	
	private MockApplication app;
	
	
	protected void setUp() throws Exception {
		super.setUp();
		this.app = new MockApplication() {
			
			@Override
			protected void initModules() {
				
			}
			
			@Override
			protected ExecutorService getExecutorService() {
				return Executors.newCachedThreadPool();
			}
		};
	}

	protected void tearDown() throws Exception {
		if(this.app != null){
			this.app.stop();
			this.app = null;
		}
	}

	public void testGetCurrentSession() throws Exception {
		this.app.registerKernelModule(new SimpleUserIdentityManagerModule<IKernelContext>());
		this.app.registerKernelModule(new UserBasedSessionManagerModule<IKernelContext>());
		this.app.start();
		
		ISessionManager sessionMgr = this.app.getService(ISessionManager.class);
		IEventRouter router = this.app.getService(IEventRouter.class);
		assertNotNull(sessionMgr);
		assertNotNull(router);
		
		ISession session = sessionMgr.getCurrentSession(false);
		assertNull(session);
		session = sessionMgr.getCurrentSession(true);
		assertNotNull(session);
		session.setValue("testAttr1", "testValue1");
		
		router.routeEvent(new TestLoginEvent("testUser1", LoginAction.LOGIN));
		Thread.sleep(100L);
		session = sessionMgr.getCurrentSession(false);
		assertNull(session);
		session = sessionMgr.getCurrentSession(true);
		assertNotNull(session);
		
		assertFalse(session.hasValue("testAttr1"));
		assertEquals("testUser1", session.getSessionId());
		session.setValue("testAttr2", "testValue2");
		assertTrue(session.hasValue("testAttr2"));
		assertEquals("testValue2", session.getValue("testAttr2"));
		
		router.routeEvent(new TestLoginEvent("testUser1", LoginAction.LOGOUT));
		Thread.sleep(100L);
		session = sessionMgr.getCurrentSession(false);
		assertNull(session);
		session = sessionMgr.getCurrentSession(true);
		assertNotNull(session);
		assertFalse(session.hasValue("testAttr1"));
		assertFalse(session.hasValue("testAttr2"));
		
		router.routeEvent(new TestLoginEvent("testUser1", LoginAction.LOGIN));
		Thread.sleep(100L);
		session = sessionMgr.getCurrentSession(false);
		assertNull(session);
		session = sessionMgr.getCurrentSession(true);
		assertNotNull(session);
		assertFalse(session.hasValue("testAttr1"));
		assertFalse(session.hasValue("testAttr2"));

	}

}
