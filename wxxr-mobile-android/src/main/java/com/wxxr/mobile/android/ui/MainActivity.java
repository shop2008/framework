/**
 * 
 */
package com.wxxr.mobile.android.ui;

import android.app.Activity;

import com.wxxr.mobile.android.app.AppUtils;
import com.wxxr.mobile.core.api.IProgressMonitor;
import com.wxxr.mobile.core.ui.api.IWorkbenchManager;

/**
 * @author neillin
 *
 */
public abstract class MainActivity extends Activity {

	private int minStartupTime = 5;
	private long confirmTime = 500;
	/* (non-Javadoc)
	 * @see android.app.Activity#onStart()
	 */
	@Override
	protected void onStart() {
		super.onStart();
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
				updateProgressDone();
				try {
					Thread.sleep(confirmTime);
				} catch (InterruptedException e) {
				}
				showHomePage();
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
		AppUtils.getFramework().attachStartMonitor(monitor);
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
