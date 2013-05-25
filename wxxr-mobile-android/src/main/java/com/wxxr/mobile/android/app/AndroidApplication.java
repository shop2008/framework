/**
 * 
 */
package com.wxxr.mobile.android.app;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import android.content.pm.ApplicationInfo;

import com.wxxr.mobile.core.api.ApplicationFactory;
import com.wxxr.mobile.core.log.api.Trace;
import com.wxxr.mobile.core.microkernel.api.AbstractMicroKernel;
import com.wxxr.mobile.core.microkernel.api.IKernelModule;

/**
 * @author neillin
 *
 */
public abstract class AndroidApplication<C extends IAndroidAppContext, M extends IKernelModule<C>> extends AbstractMicroKernel<C, M> implements IAndroidApplication<C,M>{

	private static final Trace log = Trace.register(AndroidApplication.class);
	
	private ExecutorService executor;
	private int maxThread = 10;

//	private Log4jConfigurator logConfig = new Log4jConfigurator();
	public AndroidApplication() {
		super();
	}

	public AndroidApplication(int maxThreads) {
		this.maxThread = maxThreads;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.api.IApplication#getExecutor()
	 */
	@Override
	public ExecutorService getExecutor() {
		return getExecutorService();
	}
	
	/* (non-Javadoc)
	 * @see com.wxxr.mobile.android.app.AndroidApplication#start()
	 */
	@Override
	public void start() {
		ApplicationFactory.getInstance().setApplication(this);
		log.warn("Starting up DXHZ application ...");
		try {
			this.executor = new ThreadPoolExecutor(1, maxThread, 20, TimeUnit.SECONDS, 
					new SynchronousQueue<Runnable>(),
					new ThreadFactory() {
						private AtomicInteger sq = new AtomicInteger(1);
						@Override
						public Thread newThread(Runnable r) {
							return new Thread(r, "dxhz app thread -- "+sq.getAndIncrement());
						}
					});
			super.start();
			log.warn("DXHZ application started !");
		} catch (Exception e) {
			log.fatal("Failed to start dxhz application",e);
			throw new RuntimeException("Failed to start dxhz application",e);
		}
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.microkernel.api.AbstractMicroKernel#stop()
	 */
	@Override
	public void stop() {
		log.warn("Stopping DXHZ application ....");
		if(this.executor != null){
			this.executor.shutdownNow();
			this.executor = null;
		}
		super.stop();
		log.warn("DXHZ application stopped !");
	}

	@Override
	protected ExecutorService getExecutorService() {
		return executor;
	}

	@Override
	public boolean isInDebugMode() {
		return ( 0 != ( getAndroidApplication().getApplicationInfo().flags &= ApplicationInfo.FLAG_DEBUGGABLE ) );
	}


}
