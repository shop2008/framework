package com.wxxr.mobile.core.ui.common;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import com.wxxr.mobile.core.api.ApplicationFactory;
import com.wxxr.mobile.core.async.api.AbstractProgressMonitor;
import com.wxxr.mobile.core.async.api.ICancellable;
import com.wxxr.mobile.core.microkernel.api.KUtils;
import com.wxxr.mobile.core.ui.api.IDialog;
import com.wxxr.mobile.core.ui.api.IWorkbench;
import com.wxxr.mobile.core.ui.api.IWorkbenchRTContext;
import com.wxxr.mobile.core.ui.api.InputEvent;

public abstract class InvocationMonitor extends AbstractProgressMonitor {

//	private static final Trace log = Trace.register(InvocationMonitor.class);
	
	private class ShowProgress implements Runnable,ICancellable {

		private boolean cancelled;
		private IDialog dialog;
		
		
		@Override
		public synchronized void cancel() {
			cancelled = true;
			if(this.dialog != null){
				this.dialog.dismiss();
				this.dialog = null;
			}
		}

		@Override
		public boolean isCancelled() {
			return this.cancelled;
		}

		@Override
		public synchronized void run() {
			if(cancelled){
				return;
			}
			
			Map<String, Object> map = new HashMap<String, Object>();
			if(params != null){
				map.putAll(params);
			}
			if(title != null){
				map.put(IDialog.DIALOG_ATTRIBUTE_TITLE, title);
			}
			if(message != null){
				map.put(IDialog.DIALOG_ATTRIBUTE_MESSAGE, message);
			}
			if(icon == null){
				map.put(IDialog.DIALOG_ATTRIBUTE_ICON, icon);
			}
			cancelCommand = new UICommand("cancel");
			cancelCommand.setAttribute(AttributeKeys.label, "取 消");
			cancelCommand.setHandler(new AbstractUICommandHandler() {
												
					@Override
					public Object execute(InputEvent event) {
						if(cancellable != null){
							cancellable.cancel();
						}
						return null;
					}
			});
			cancelCommand.init(context);
			map.put(IDialog.DIALOG_ATTRIBUTE_RIGHT_BUTTON, cancelCommand);
			dialog = context.getWorkbenchManager().getWorkbench().createDialog(IWorkbench.PROGRESSMONITOR_DIALOG_ID, map);
			if(dialog != null){
				dialog.show();
			}
		}
		
	}
	
	private ICancellable task,cancellable;
	private ShowProgress progress;
	private int silentPeriod = 1;
	private String title,message,icon;
	private Map<String, Object> params;
//	private Future<?> future;
	private IWorkbenchRTContext context;
	private UICommand cancelCommand;
		
	public InvocationMonitor(IWorkbenchRTContext ctx){
		this.context = ctx;
	}
	
	
	
	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.api.AbstractProgressMonitor#beginTask(int)
	 */
	@Override
	public final void beginTask(int arg0) {

		if(this.silentPeriod == 0){
			progress = showProgressBar();
		}else if(this.silentPeriod < 0){
			return;
		}else{
			this.task = ApplicationFactory.getInstance().getApplication().invokeLater(new Runnable() {
				
				public void run() {
					progress = showProgressBar();
					task = null;
				}
			}, silentPeriod, (silentPeriod > 100) ? TimeUnit.MILLISECONDS : TimeUnit.SECONDS);
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
		if(this.progress != null){
			this.progress.cancel();
			this.progress = null;
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
		if(this.progress != null){
			this.progress.cancel();
			this.progress = null;
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
//		log.warn("Caught exception when executing task", e);
		if(task != null){
			task.cancel();
			task = null;
		}
		if(this.progress != null){
			this.progress.cancel();
			this.progress = null;
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
	
	
	protected ShowProgress showProgressBar() {
		    ShowProgress p = new ShowProgress();
			KUtils.runOnUIThread(p);
			return p;
		}

	
	protected Map<String, Object> getDialogParams(){
		return null;
	}
	
	public void executeOnMonitor(final Callable<Object> task){
		KUtils.invokeLater(new Runnable() {
			
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


	/**
	 * @return the silentPeriod
	 */
	public int getSilentPeriod() {
		return silentPeriod;
	}



	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}


	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}


	/**
	 * @return the icon
	 */
	public String getIcon() {
		return icon;
	}


	/**
	 * @return the params
	 */
	public Map<String, Object> getParams() {
		return params;
	}


	/**
	 * @param silentPeriod the silentPeriod to set
	 */
	public void setSilentPeriod(int silentPeriod) {
		this.silentPeriod = silentPeriod;
	}




	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}


	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}


	/**
	 * @param icon the icon to set
	 */
	public void setIcon(String icon) {
		this.icon = icon;
	}


	/**
	 * @param params the params to set
	 */
	public void setParams(Map<String, Object> params) {
		this.params = params;
	}



	/**
	 * @return the cancellable
	 */
	public ICancellable getCancellable() {
		return cancellable;
	}



	/**
	 * @param cancellable the cancellable to set
	 */
	public void setCancellable(ICancellable cancellable) {
		this.cancellable = cancellable;
		if((cancelCommand != null)&&(cancellable != null)&&(!cancellable.isCancelled())){
			cancelCommand.setAttribute(AttributeKeys.enabled, true);
		}else if((cancelCommand != null)&&((cancellable == null)||cancellable.isCancelled())){
			cancelCommand.setAttribute(AttributeKeys.enabled, false);
		}
	}
}
