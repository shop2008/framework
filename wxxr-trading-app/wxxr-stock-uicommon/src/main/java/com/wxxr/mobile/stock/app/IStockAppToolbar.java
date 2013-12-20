/**
 * 
 */
package com.wxxr.mobile.stock.app;

import java.util.Map;

import com.wxxr.mobile.core.ui.api.IAppToolbar;

/**
 * @author neillin
 *
 */
public interface IStockAppToolbar extends IAppToolbar {
	void showNotification(String message, String status, Map<String, String> parameters);
}
