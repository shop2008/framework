/**
 * 
 */
package com.wxxr.mobile.android.app;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;

import com.wxxr.mobile.core.api.ApplicationFactory;
import com.wxxr.mobile.core.log.api.Trace;
import com.wxxr.mobile.core.microkernel.api.AbstractMicroKernel;
import com.wxxr.mobile.core.microkernel.api.IKernelModule;

/**
 * @author neillin
 *
 */
public abstract class AndroidFramework<C extends IAndroidAppContext, M extends IKernelModule<C>> extends AbstractMicroKernel<C, M> implements IAndroidFramework<C,M>{

	private static final Trace log = Trace.register(AndroidFramework.class);
	
	private ExecutorService executor;
	private int maxThread = 10;
	private UnexpectingExceptionHandler handler;
	private Map<String, String> info = new HashMap<String, String>();

//	private Log4jConfigurator logConfig = new Log4jConfigurator();
	public AndroidFramework() {
		super();
		handler = UnexpectingExceptionHandler.install();
	}

	public AndroidFramework(int maxThreads) {
		this.maxThread = maxThreads;
		UnexpectingExceptionHandler.install();
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.api.IApplication#getExecutor()
	 */
	@Override
	public ExecutorService getExecutor() {
		return getExecutorService();
	}
	
	/* (non-Javadoc)
	 * @see com.wxxr.mobile.android.app.AndroidFramework#start()
	 */
	@Override
	public void start() {
		ApplicationFactory.getInstance().setApplication(this);
		collectDeviceInfo();
		if(log.isInfoEnabled()){
			log.info("UnexpectingExceptionHandler installed, ui thread :"+handler.getUiThread());
		}
		log.warn("Starting up application ...");
		try {
			this.executor = new ThreadPoolExecutor(1, maxThread, 20, TimeUnit.SECONDS, 
					new SynchronousQueue<Runnable>(),
					new ThreadFactory() {
						private AtomicInteger sq = new AtomicInteger(1);
						@Override
						public Thread newThread(Runnable r) {
							return new Thread(r, "application thread-"+sq.getAndIncrement());
						}
					});
			super.start();
			log.warn("Application started !");
		} catch (Exception e) {
			log.fatal("Failed to start application",e);
			throw new RuntimeException("Failed to start application",e);
		}
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.microkernel.api.AbstractMicroKernel#stop()
	 */
	@Override
	public void stop() {
		log.warn("Stopping application ....");
		if(this.executor != null){
			this.executor.shutdownNow();
			this.executor = null;
		}
		super.stop();
		log.warn("Application stopped !");
	}

	@Override
	protected ExecutorService getExecutorService() {
		return executor;
	}

	@Override
	public boolean isInDebugMode() {
		return ( 0 != ( getAndroidApplication().getApplicationInfo().flags &= ApplicationInfo.FLAG_DEBUGGABLE ) );
	}

	public Map<String, String> getDeviceInfo() {
		return Collections.unmodifiableMap(this.info);
	}
	
	
	protected void collectDeviceInfo()
	{
		try
		{
			Context context = getAndroidApplication();
			PackageManager pm = context.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES);
			if (pi != null)
			{
				String versionName = pi.versionName == null ? "null" : pi.versionName;
				String versionCode = pi.versionCode + "";
				info.put("versionName", versionName);
				info.put("versionCode", versionCode);
				if(log.isInfoEnabled()){
					log.info("versionName -> ", versionName);
					log.info("versionCode -> ", versionCode);
				}
			}
		}
		catch (Throwable e)
		{
			e.printStackTrace();
		}

		Field[] fields = Build.class.getDeclaredFields();
		for (Field field : fields)
		{
			try
			{
				field.setAccessible(true);
				info.put(field.getName(), field.get("").toString());
				if(log.isDebugEnabled()){
					log.info( field.getName()+" -> "+ field.get(""));
				}
			}
			catch (Throwable e)
			{
				e.printStackTrace();
			}
		}
	}



}
