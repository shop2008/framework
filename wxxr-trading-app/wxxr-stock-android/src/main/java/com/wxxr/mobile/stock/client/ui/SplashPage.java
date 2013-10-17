/**
 * 
 */
package com.wxxr.mobile.stock.client.ui;

import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.wxxr.mobile.android.app.AppUtils;
import com.wxxr.mobile.android.ui.MainActivity;
import com.wxxr.mobile.stock.client.R;

/**
 * @author neillin
 *
 */
public class SplashPage extends MainActivity {

	private ProgressBar progressBar;
	private TextView textView;
	private boolean inDebugMode = false;
	/* (non-Javadoc)
	 * @see com.wxxr.mobile.android.ui.MainActivity#setupContentView()
	 */
	@Override
	protected void setupContentView() {
		setContentView(R.layout.splash_layout);
		this.progressBar = (ProgressBar)findViewById(R.id.loading_progressBar);
		this.textView = (TextView)findViewById(R.id.loading_Info);
		setMinStartupTime(3);
		this.inDebugMode = AppUtils.getFramework().isInDebugMode();
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.android.ui.MainActivity#showProgressBar(int)
	 */
	@Override
	protected void showProgressBar(final int totalwork) {
		AppUtils.runOnUIThread(new Runnable() {			
			@Override
			public void run() {
				if(progressBar != null){
					progressBar.setMax(totalwork);
				}
//				if((inDebugMode == false)&&(textView != null)){
//					textView.setVisibility(View.GONE);
//				}
			}
		});
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.android.ui.MainActivity#updateProgress(int, java.lang.String)
	 */
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

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.android.ui.MainActivity#startupFailed(java.lang.Throwable, java.lang.String)
	 */
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
