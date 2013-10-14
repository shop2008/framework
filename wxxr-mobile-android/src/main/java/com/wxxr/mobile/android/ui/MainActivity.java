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

	/* (non-Javadoc)
	 * @see android.app.Activity#onStart()
	 */
	@Override
	protected void onStart() {
		super.onStart();
		setupContentView();
	}

	protected void startupKernel() {
		final IProgressMonitor monitor = new IProgressMonitor() {
			
			@Override
			public void updateProgress(int work, String message) {
				MainActivity.this.updateProgress(work, message);
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
				showHomePage();
			}
			
			@Override
			public void beginTask(int totalWork) {
				showProgressBar(totalWork);
			}
		};
		 new Thread(){
				@Override
				public void run() {
						AppUtils.getFramework().start(monitor);
				}
		}.start();
	}

	protected abstract void setupContentView();
	protected abstract void showProgressBar(int totalwork);
	protected abstract void updateProgress(int workDone, String message);
	protected abstract void startupFailed(Throwable t, String message);
	
	protected void showHomePage(){
		AppUtils.getService(IWorkbenchManager.class).getWorkbench().showHomePage();
	}
}
