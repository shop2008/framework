/**
 * 
 */
package com.wxxr.archetype.mobile.service;

import java.util.Date;
import java.util.Timer;

import com.wxxr.mobile.android.app.IAndroidAppContext;
import com.wxxr.mobile.core.log.api.Trace;
import com.wxxr.mobile.core.microkernel.api.AbstractModule;


/**
 * 
 * @author fudapeng
 * 
 */
public class TimeService extends AbstractModule<IAndroidAppContext> implements ITimeService {

	private static final Trace log = Trace.register(TimeService.class);
	private TimeBean bean = new TimeBean();
	private Ticker ticker;
	
	private class Ticker implements Runnable {
		private Thread thread;
		private boolean keepAlive;
		
		public void run() {
			this.thread = Thread.currentThread();
			this.keepAlive = true;
			while(this.keepAlive){
				if(bean.isTicking()){
					bean.setCurrentTime(System.currentTimeMillis());
				}
				try {
					Thread.sleep(500L);
				} catch (InterruptedException e) {
				}
			}
		}
		
		public void start() {
			new Thread(this,"Time Ticker").start();
		}
		
		public void stop() {
			this.keepAlive = false;
			if((this.thread != null)&&this.thread.isAlive()){
				this.thread.interrupt();
				try {
					this.thread.join(1000L);
				} catch (InterruptedException e) {
				}
				this.thread = null;
			}
		}
	}
	@Override
	public void startService() {
		this.ticker = new Ticker();
		this.ticker.start();
		context.registerService(ITimeService.class, this);
	}


	@Override
	public void stopService() {
		context.unregisterService(ITimeService.class, this);
		if(this.ticker != null){
			this.ticker.stop();
			this.ticker = null;
		}
	}
	
	@Override
	public TimeBean getTime() {
	       return bean;
	}

	@Override
	protected void initServiceDependency() {
	}


	@Override
	public void startTicking() {
		this.bean.setTicking(true);
	}


	@Override
	public void stopTicking() {
		this.bean.setTicking(false);
	}
}

