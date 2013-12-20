/**
 * 
 */
package com.wxxr.mobile.stock.client.service;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;

import com.wxxr.mobile.android.app.AppUtils;
import com.wxxr.mobile.android.ui.IAndroidPageNavigator;
import com.wxxr.mobile.core.microkernel.api.AbstractModule;
import com.wxxr.mobile.core.ui.api.IWorkbenchManager;
import com.wxxr.mobile.stock.app.IStockAppContext;

/**
 * @author neillin
 *
 */
public class GenericContentService extends AbstractModule<IStockAppContext> implements
		IGenericContentService {

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.stock.client.service.IPhoneCallService#showCallUI(java.lang.String)
	 */
	@Override
	public void showCallUI(String number) {
		Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+ number));
		IAndroidPageNavigator navigator = (IAndroidPageNavigator)AppUtils.getService(IWorkbenchManager.class).getPageNavigator();
		navigator.startActivity(intent);
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.stock.client.service.IPhoneCallService#showDialUI(java.lang.String)
	 */
	@Override
	public void showDialUI(String number) {
		Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+ number));
		IAndroidPageNavigator navigator = (IAndroidPageNavigator)AppUtils.getService(IWorkbenchManager.class).getPageNavigator();
		navigator.startActivity(intent);
	}

	@Override
	protected void initServiceDependency() {
		addRequiredService(IWorkbenchManager.class);
	}

	@Override
	protected void startService() {
		context.registerService(IGenericContentService.class, this);
	}

	@Override
	protected void stopService() {
		context.unregisterService(IGenericContentService.class, this);
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.stock.client.service.IGenericContentService#browseContent(java.lang.String)
	 */
	@Override
	public void browseContent(String contentUrl) {
		Uri uri = Uri.parse(contentUrl); 
		Intent intent  = new Intent(Intent.ACTION_VIEW,uri); 
		IAndroidPageNavigator navigator = (IAndroidPageNavigator)AppUtils.getService(IWorkbenchManager.class).getPageNavigator();
		navigator.startActivity(intent);
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.stock.client.service.IGenericContentService#showEmailUI(java.lang.String)
	 */
	@Override
	public void showEmailUI(String email) {
		Uri uri = Uri.parse("mailto:"+email); 
		Intent intent = new Intent(Intent.ACTION_SENDTO, uri); 
		IAndroidPageNavigator navigator = (IAndroidPageNavigator)AppUtils.getService(IWorkbenchManager.class).getPageNavigator();
		navigator.startActivity(intent);
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.stock.client.service.IGenericContentService#showMarket(java.lang.String)
	 */
	@Override
	public void showMarket(String packageName) {
		Uri uri = Uri.parse("market://search?q=pname:"+packageName);          
		Intent intent = new Intent(Intent.ACTION_VIEW, uri);          
		IAndroidPageNavigator navigator = (IAndroidPageNavigator)AppUtils.getService(IWorkbenchManager.class).getPageNavigator();
		try {
			navigator.startActivity(intent);
		} catch(ActivityNotFoundException e) {
		}
	}
}
