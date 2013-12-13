/**
 * 
 */
package com.wxxr.mobile.stock.app.utils;

import com.wxxr.mobile.android.app.AppUtils;
import com.wxxr.mobile.stock.app.service.IURLLocatorManagementService;

/**
 * @author wangxuyang
 *
 */
public class Utils {
	public static  String getHostURL(){
		return AppUtils.getService(IURLLocatorManagementService.class).getMagnoliaURL();
	}
	public static String getAbsoluteURL(String relativeUrl){
		return getHostURL()+relativeUrl;
	}
	public static String[] getAbsoluteURL(String... relativeUrls){
		if (relativeUrls!=null) {
			for (int i = 0; i < relativeUrls.length; i++) {
				relativeUrls[i]=getHostURL()+relativeUrls[i];
			}
		}
		return relativeUrls;
	}
}
