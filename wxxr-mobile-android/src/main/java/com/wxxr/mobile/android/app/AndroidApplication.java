/**
 * 
 */
package com.wxxr.mobile.android.app;

import java.util.concurrent.ExecutorService;

import com.wxxr.mobile.core.api.ApplicationFactory;
import com.wxxr.mobile.core.microkernel.api.AbstractMicroKernel;
import com.wxxr.mobile.core.microkernel.api.IKernelModule;

/**
 * @author neillin
 *
 */
public abstract class AndroidApplication<C extends IAndroidAppContext, M extends IKernelModule<C>> extends AbstractMicroKernel<C, M> implements IAndroidApplication<C,M>{

//	private Log4jConfigurator logConfig = new Log4jConfigurator();
	public AndroidApplication() {
		super();
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.microkernel.api.AbstractMicroKernel#start()
	 */
	@Override
	public void start() throws Exception {
//		logConfig.configureFileAppender(Level.ERROR);
//		if(this.isInDebugMode()){
//			logConfig.configureLogCatAppender(Level.DEBUG);
//		}
//		logConfig.configure();
		ApplicationFactory.getInstance().setApplication(this);
		super.start();
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.api.IApplication#getExecutor()
	 */
	@Override
	public ExecutorService getExecutor() {
		return getExecutorService();
	}

}
