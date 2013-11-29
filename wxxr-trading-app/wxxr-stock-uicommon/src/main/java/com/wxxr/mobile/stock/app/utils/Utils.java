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
	public static  String getArticleHostURL(){
		return AppUtils.getService(IURLLocatorManagementService.class).getMagnoliaURL();
	}
	public static String getAbsoluteURL(String relativeUrl){
		return getArticleHostURL()+relativeUrl;
	}
}
