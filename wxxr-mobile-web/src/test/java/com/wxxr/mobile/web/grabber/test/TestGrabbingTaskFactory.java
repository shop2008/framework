/**
 * 
 */
package com.wxxr.mobile.web.grabber.test;

import com.wxxr.mobile.web.grabber.api.IGrabbingTaskFactory;
import com.wxxr.mobile.web.grabber.api.IWebGrabbingTask;
import com.wxxr.mobile.web.grabber.common.AbstractGrabberModule;

/**
 * @author neillin
 *
 */
public class TestGrabbingTaskFactory extends AbstractGrabberModule implements
		IGrabbingTaskFactory {

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.web.grabber.api.IGrabbingTaskFactory#createNewTask()
	 */
	@Override
	public IWebGrabbingTask createNewTask() {
		return new TestGrabbingTask();
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.web.grabber.api.IGrabbingTaskFactory#destroyTask(com.wxxr.mobile.web.grabber.api.IWebGrabbingTask)
	 */
	@Override
	public void destroyTask(IWebGrabbingTask task) {

	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.microkernel.api.AbstractModule#initServiceDependency()
	 */
	@Override
	protected void initServiceDependency() {
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.microkernel.api.AbstractModule#startService()
	 */
	@Override
	protected void startService() {
		context.registerService(IGrabbingTaskFactory.class, this);
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.microkernel.api.AbstractModule#stopService()
	 */
	@Override
	protected void stopService() {
		context.registerService(IGrabbingTaskFactory.class, this);
	}

}
