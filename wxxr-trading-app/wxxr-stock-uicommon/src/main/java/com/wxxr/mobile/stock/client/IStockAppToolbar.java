/**
 * 
 */
package com.wxxr.mobile.stock.client;

import java.util.Map;

import com.wxxr.mobile.core.ui.api.IAppToolbar;

/**
 * @author neillin
 *
 */
public interface IStockAppToolbar extends IAppToolbar {
	void showNotification(String message,Map<String, String> parameters);
	void setUpdateTitle(String title,Map<String, String> parameters);
}
