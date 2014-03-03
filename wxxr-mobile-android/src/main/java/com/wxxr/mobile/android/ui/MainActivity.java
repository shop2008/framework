/**
 * 
 */
package com.wxxr.mobile.android.ui;

import java.util.concurrent.TimeUnit;

import android.app.Activity;

import com.wxxr.mobile.android.app.AppUtils;
import com.wxxr.mobile.core.async.api.IProgressMonitor;
import com.wxxr.mobile.core.log.api.Trace;
import com.wxxr.mobile.core.ui.api.IWorkbenchManager;

/**
 * @author neillin
 *
 */
public abstract class MainActivity extends Activity {
	private static final Trace log = Trace.getLogger(MainActivity.class);
	
	private int minStartupTime = 5;
	private long confirmTime = 500;
	/* (non-Javadoc)
	 * @see android.app.Activity#onStart()
	 */
	@Override
	protected void onStart() {
		super.onStart();
		if(log.isInfoEnabled()){
			log.info("Starting android application ...");
		}
		setupContentView();
		startupKernel();
	}

	protected void startupKernel() {
		final IProgressMonitor monitor = new IProgressMonitor() {
			private long minIntervalPerWork = 0L, lastUpdateTime,startTime;
			private int totalWork,finishedWork;
			@Override
			public void updateProgress(int work, String message) {
				int workDone = work - finishedWork;
				finishedWork = work;
				long interval = System.currentTimeMillis()-lastUpdateTime;
				long minInterval = minIntervalPerWork*workDone;
				if(interval < minInterval){
					try {
						Thread.sleep(minInterval - interval);
					} catch (InterruptedException e) {
					}
				}
				MainActivity.this.updateProgress(work, message);
				this.lastUpdateTime = System.currentTimeMillis();
			}
			
			@Override
			public void taskFailed(Throwable cause, String message) {
				MainActivity.this.startupFailed(cause, message);
			}
			
			@Override
			public void taskCanceled(boolean value) {
				showHomePage();
			}
			
			@Override
			public void setTaskName(String name) {
				
			}
			
			@Override
			public void done(Object result) {
				if(this.startTime == 0){
					showProgressBar(50);
					AppUtils.invokeLater(new Runnable() {
						
						@Override
						public void run() {
							final int[] work = new int[]{1};
							final Runnable[] task = new Runnable[1];
							task[0] = new Runnable() {
								
								@Override
								public void run() {
									updateProgress(work[0], "");
									work[0]++;
									if(work[0] < 50){
										AppUtils.runOnUIThread(task[0], 10, TimeUnit.MILLISECONDS);
									}else{
										AppUtils.runOnUIThread(new Runnable() {
											
											@Override
											public void run() {
												updateProgressDone();
												showHomePage();
											}	
										});
									}
								}
							};
							AppUtils.runOnUIThread(task[0], 10, TimeUnit.MILLISECONDS);
						}
					}, 1, TimeUnit.MILLISECONDS);
				}else{
					updateProgressDone();
					AppUtils.runOnUIThread(new Runnable() {
						
						@Override
						public void run() {
							showHomePage();
						}
					}, confirmTime, TimeUnit.MILLISECONDS);
				}
				
			}
			
			@Override
			public void beginTask(int totalWork) {
				this.totalWork = totalWork;
				this.minIntervalPerWork = (minStartupTime*1000L)/totalWork;
				this.startTime = System.currentTimeMillis();
				this.lastUpdateTime = this.startTime;
				showProgressBar(totalWork);
			}
		};
//		 new Thread(){
//				@Override
//				public void run() {
//						AppUtils.getFramework().start(monitor);
//				}
//		}.start();
		if(AppUtils.getFramework().isStarted()){
			if(log.isInfoEnabled()){
				log.info("Kernel is started, going to show home page");
			}
			monitor.done(null);
			return;
		}else{
			AppUtils.getFramework().attachStartMonitor(monitor);
		}
	}

	protected abstract void setupContentView();
	protected abstract void showProgressBar(int totalwork);
	protected abstract void updateProgress(int workDone, String message);
	protected abstract void updateProgressDone();
	protected abstract void startupFailed(Throwable t, String message);
	
	protected void showHomePage(){
		AppUtils.getService(IWorkbenchManager.class).getWorkbench().showHomePage();
		this.finish();
	}

	/**
	 * @return the minStartupTime
	 */
	protected int getMinStartupTime() {
		return minStartupTime;
	}

	/**
	 * @param minStartupTime the minStartupTime to set
	 */
	protected void setMinStartupTime(int minStartupTime) {
		this.minStartupTime = minStartupTime;
	}

	/**
	 * @return the confirmTime
	 */
	protected long getConfirmTime() {
		return confirmTime;
	}

	/**
	 * @param confirmTime the confirmTime to set
	 */
	protected void setConfirmTime(long confirmTime) {
		this.confirmTime = confirmTime;
	}
}
