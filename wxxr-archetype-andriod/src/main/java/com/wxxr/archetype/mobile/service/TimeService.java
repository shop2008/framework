/**
 * 
 */
package com.wxxr.archetype.mobile.service;

import com.wxxr.archetype.mobile.command.StartTimeCommand;
import com.wxxr.archetype.mobile.handler.StartTimeCommandHandler;
import com.wxxr.mobile.android.app.IAndroidAppContext;
import com.wxxr.mobile.core.async.api.AsyncFuture;
import com.wxxr.mobile.core.async.api.ExecAsyncException;
import com.wxxr.mobile.core.async.api.IAsyncCallback;
import com.wxxr.mobile.core.command.api.ICommandExecutor;
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
		context.getService(ICommandExecutor.class).registerCommandHandler(StartTimeCommand.Name, new StartTimeCommandHandler());
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
		addRequiredService(ICommandExecutor.class);
	}

//	private class AsyncCallbackImpl implements IAsyncCallback<TimeBean>{
//		private ICancellable cancel;
//		@Override
//		public void cancelled() {
//			if(cancel != null)
//			cancel.cancel();
//		}
//		@Override
//		public void failed(Throwable e) {
//			
//		}
//		@Override
//		public void setCancellable(ICancellable cancel) {
//			this.cancel = cancel;
//		}
//		@Override
//		public void success(TimeBean bean) {
//		}
//		public ICancellable getCancellable() {
//			return cancel;
//		}
//	}
	@Override
	public void startTicking() {
		AsyncFuture<TimeBean> control = new AsyncFuture();
		IAsyncCallback<TimeBean> asyncCallback= control.getInternalCallback();
		context.getService(ICommandExecutor.class).submitCommand(new StartTimeCommand(bean),asyncCallback);
		ExecAsyncException asyncException = new ExecAsyncException(control,"提示","请稍候....");
		throw asyncException;
	}


	@Override
	public void stopTicking() {
		this.bean.setTicking(false);
	}
}

