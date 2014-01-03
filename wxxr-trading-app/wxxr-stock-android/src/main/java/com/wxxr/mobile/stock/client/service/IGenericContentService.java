/**
 * 
 */
package com.wxxr.mobile.stock.client.service;

import android.content.Intent;

/**
 * @author neillin
 *
 */
public interface IGenericContentService {
	void showCallUI(String number);
	void showDialUI(String number);
	void browseContent(String contentUrl);
	void showEmailUI(String email);
	void showMarket(String packageName);
	void startAndroidIntent(Intent intent);
	void startDownloadService(String downloadUrl);
}
