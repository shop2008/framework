package com.wxxr.mobile.core.ui.common;

import java.util.HashMap;
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
import com.wxxr.mobile.core.ui.api.IWorkbenchRTContext;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.util.ICancellable;

public abstract class InvocationMonitor extends AbstractProgressMonitor {

	private static final Trace log = Trace.register(InvocationMonitor.class);
	
	private ICancellable task,progess;
	private int silentPeriod = 1;
	private boolean cancellable = false;
	private String title,message,icon;
	private Map<String, Object> params;
	private Future<?> future;
	private IWorkbenchRTContext context;
		
	public InvocationMonitor(IWorkbenchRTContext ctx){
		this.context = ctx;
	}
	
	
	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.api.AbstractProgressMonitor#beginTask(int)
	 */
	@Override
	public final void beginTask(int arg0) {

		if(this.silentPeriod == 0){
			progess = showProgressBar();
		}else if(this.silentPeriod < 0){
			return;
		}else{
			this.task = ApplicationFactory.getInstance().getApplication().invokeLater(new Runnable() {
				
				public void run() {
					progess = showProgressBar();
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
					if(isCancellable()){
						UICommand command = new UICommand("cancel");
						command.setAttribute(AttributeKeys.label, "取 消");
						command.setHandler(new AbstractUICommandHandler() {
														
							@Override
							public Object execute(InputEvent event) {
								if(future != null){
									future.cancel(true);
								}
								taskCanceled(true);
								return null;
							}
						});
						command.init(context);
						map.put(IDialog.DIALOG_ATTRIBUTE_RIGHT_BUTTON, command);
					}
					p[0] = context.getWorkbenchManager().getWorkbench().createDialog(IWorkbench.PROGRESSMONITOR_DIALOG_ID, map);
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
		this.future = ApplicationFactory.getInstance().getApplication().getExecutor().submit(new Runnable() {
			
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
		return this.future;
	}


	/**
	 * @return the silentPeriod
	 */
	public int getSilentPeriod() {
		return silentPeriod;
	}


	/**
	 * @return the cancellable
	 */
	public boolean isCancellable() {
		return cancellable;
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
	 * @param cancellable the cancellable to set
	 */
	public void setCancellable(boolean cancellable) {
		this.cancellable = cancellable;
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
}
