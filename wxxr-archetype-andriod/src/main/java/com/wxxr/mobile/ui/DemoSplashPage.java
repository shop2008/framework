package com.wxxr.mobile.ui;


import com.wxxr.mobile.android.app.AppUtils;
import com.wxxr.mobile.android.ui.MainActivity;
import com.wxxr.mobile.R;

import android.widget.ProgressBar;
import android.widget.TextView;

public class DemoSplashPage extends MainActivity {

	private ProgressBar progressBar;
	private TextView textView;
	private boolean inDebugMode = false;
	
	@Override
	protected void setupContentView() {
		setContentView(R.layout.splash_page_layout);
		this.progressBar = (ProgressBar)findViewById(R.id.loading_progressBar);
		this.textView = (TextView)findViewById(R.id.loading_Info);
		setMinStartupTime(2);
		this.inDebugMode = AppUtils.getFramework().isInDebugMode();
	}
	
	@Override
	protected void showProgressBar(final int totalwork) {
		AppUtils.runOnUIThread(new Runnable() {			
			@Override
			public void run() {
				if(progressBar != null){
					progressBar.setMax(totalwork);
				}
			}
		});
	}

	@Override
	protected void updateProgress(final int workDone, final String message) {
		AppUtils.runOnUIThread(new Runnable() {			
			@Override
			public void run() {
				if(progressBar != null){
					progressBar.setProgress(workDone);
				}
				if(inDebugMode && (message != null)&&(textView != null)){
					textView.setText(message);
				}
			}
		});

	}

	@Override
	protected void startupFailed(Throwable t, final String message) {
		AppUtils.runOnUIThread(new Runnable() {	
			@Override
			public void run() {
				if(textView != null){
					textView.setText("系统启动失败 :["+message+"]");
				}
			}
		});
	}

	@Override
	protected void updateProgressDone() {
		AppUtils.runOnUIThread(new Runnable() {	
			@Override
			public void run() {
				if(textView != null){
					textView.setText("系统启动完成 ！");
				}
			}
		});
	}
}
