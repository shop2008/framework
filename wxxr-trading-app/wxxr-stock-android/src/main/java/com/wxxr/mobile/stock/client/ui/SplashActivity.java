/**
 * 
 */
package com.wxxr.mobile.stock.client.ui;


import android.widget.ProgressBar;
import android.widget.TextView;

import com.wxxr.mobile.android.ui.MainActivity;
import com.wxxr.mobile.stock.client.R;

/**
 * @author neillin
 *
 */
public class SplashActivity extends MainActivity {

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.android.ui.MainActivity#setupContentView()
	 */
	private ProgressBar loadingProgressBar;
	private TextView loadingInfo;
	@Override
	protected void setupContentView() {
		setContentView(R.layout.splash_layout);
		findView();
	}

	private void findView(){
		loadingProgressBar = (ProgressBar) findViewById(R.id.loading_progressBar);
		loadingInfo = (TextView) findViewById(R.id.loading_Info);
	}
	/* (non-Javadoc)
	 * @see com.wxxr.mobile.android.ui.MainActivity#showProgressBar(int)
	 */
	@Override
	protected void showProgressBar(int totalwork) {
		loadingProgressBar.setMax(totalwork);
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.android.ui.MainActivity#updateProgress(int, java.lang.String)
	 */
	@Override
	protected void updateProgress(int workDone, String message) {
		loadingProgressBar.setProgress(workDone);
		loadingInfo.setText(message);

	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.android.ui.MainActivity#startupFailed(java.lang.Throwable, java.lang.String)
	 */
	@Override
	protected void startupFailed(Throwable t, String message) {
		// TODO Auto-generated method stub

	}

}
