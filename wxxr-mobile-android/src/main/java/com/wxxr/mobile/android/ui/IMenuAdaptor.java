/**
 * 
 */
package com.wxxr.mobile.android.ui;

import android.view.View;

import com.wxxr.mobile.core.ui.api.IMenuHandler;

/**
 * @author neillin
 *
 */
public interface IMenuAdaptor {
	
	void init(IAndroidBindingContext ctx, View host);
	void destroy();
	
	String[] getMenuIds();
	IMenuHandler getMenuHandler(String menuId);
}
