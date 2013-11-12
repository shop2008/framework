package com.wxxr.mobile.core.ui.common;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import com.wxxr.mobile.core.api.AbstractProgressMonitor;
import com.wxxr.mobile.core.api.ApplicationFactory;
import com.wxxr.mobile.core.log.api.Trace;
import com.wxxr.mobile.core.microkernel.api.KUtils;
import com.wxxr.mobile.core.ui.api.IDialog;
import com.wxxr.mobile.core.ui.api.IWorkbench;
import com.wxxr.mobile.core.ui.api.IWorkbenchManager;
import com.wxxr.mobile.core.util.ICancellable;

public abstract class InvocationMonitor extends AbstractProgressMonitor {

	private static final Trace log = Trace.register(InvocationMonitor.class);
	
	private ICancellable task,progess;
	private int waitTimeInSeconds = 2;
		
	public InvocationMonitor(int waitTime){
		this.waitTimeInSeconds = waitTime;
	}
	
	
	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.api.AbstractProgressMonitor#beginTask(int)
	 */
	@Override
	public final void beginTask(int arg0) {

		if(this.waitTimeInSeconds == 0){
			progess = showProgressBar();
		}else if(this.waitTimeInSeconds < 0){
			return;
		}else{
			this.task = ApplicationFactory.getInstance().getApplication().invokeLater(new Runnable() {
				
				public void run() {
					progess = showProgressBar();
				}
			}, waitTimeInSeconds, TimeUnit.SECONDS);
		}
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.api.AbstractProgressMonitor#done(java.lang.Object)
	 */
	@Override
	public final void done(final Object arg0) {
		if(task != null){
			task.cancel();
			task = null;
		}
		if(this.progess != null){
			this.progess.cancel();
			this.progess = null;
		}
		KUtils.runOnUIThread(new Runnable() {
				
				@Override
				public void run() {
					handleDone(arg0);
					
				}
			});
	}

	
	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.api.AbstractProgressMonitor#taskCanceled(boolean)
	 */
	@Override
	public void taskCanceled(final boolean arg0) {
		if(task != null){
			task.cancel();
			task = null;
		}
		if(this.progess != null){
			this.progess.cancel();
			this.progess = null;
		}
		KUtils.runOnUIThread(new Runnable() {
				
				@Override
				public void run() {
					handleConceled(arg0);
				}
			});
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.api.AbstractProgressMonitor#taskFailed(java.lang.Throwable)
	 */
	@Override
	public void taskFailed(final Throwable e,String message) {
		log.warn("Caught exception when executing task", e);
		if(task != null){
			task.cancel();
			task = null;
		}
		if(this.progess != null){
			this.progess.cancel();
			this.progess = null;
		}
		KUtils.runOnUIThread(new Runnable() {
				
				@Override
				public void run() {
					handleFailed(e);
				}
			});
	}
	
	protected abstract void handleDone(Object returnVal);
	
	
	protected void handleConceled(boolean bool){
		
	}
		
	protected abstract void handleFailed(Throwable cause);
	
	
	protected ICancellable showProgressBar() {
			final IDialog[] p = new IDialog[1];
			
			KUtils.runOnUIThread(new Runnable() {
				@Override
				public void run() {
					p[0] = KUtils.getService(IWorkbenchManager.class).getWorkbench().createDialog(IWorkbench.PROGRESSMONITOR_DIALOG_ID, null);
					if(p[0] != null){
						p[0].show();
					}
				}
			});
			return new ICancellable() {
				boolean cancelled = false;
				@Override
				public boolean isCancelled() {
					return cancelled;
				}
				
				@Override
				public void cancel() {
					if(p[0] != null){
						KUtils.runOnUIThread(new Runnable() {
							@Override
							public void run() {
								p[0].dismiss();
							}
						});
					}
					cancelled = true;
				}
			};
		}

	
	protected Map<String, Object> getDialogParams(){
		return null;
	}
	
	public Future<?> executeOnMonitor(final Callable<Object> task){
		return ApplicationFactory.getInstance().getApplication().getExecutor().submit(new Runnable() {
			
			@Override
			public void run() {
				beginTask(-1);
				try {
					Object retVal = task.call();
					done(retVal);
				}catch(Throwable t){
					taskFailed(t,null);
				}
			}
		});
	}
}
