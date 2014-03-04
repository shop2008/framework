/**
 * 
 */
package com.wxxr.mobile.stock.app;

import com.wxxr.mobile.core.async.api.IProgressMonitor;
import com.wxxr.mobile.core.log.api.Trace;

/**
 * @author neillin
 *
 */
public abstract class AbstractMonitorRunnable implements Runnable {

	private final IProgressMonitor monitor;
	private final Trace log;
	
	public AbstractMonitorRunnable(IProgressMonitor m, Trace log){
		this.monitor = m;
		this.log = log;
	}
	@Override
	public void run() {
		if(monitor != null){
			monitor.beginTask(-1);
		}
		try {
			Object ret = executeTask();
		if(monitor != null){
			monitor.done(ret);
		}
		}catch(Throwable t){
			if(log != null){
				log.warn("Failed to require user activation password", t);
			}
			if(monitor != null){
				monitor.taskFailed(t,"task failed");
			}
		}		
	}

	
	protected abstract Object executeTask() throws Throwable;
}
