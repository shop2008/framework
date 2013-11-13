/**
 * 
 */
package com.wxxr.mobile.core.ui.api;

import java.util.Map;

/**
 * @author neillin
 *
 */
public interface IProgressGuard {
	int getSilentPeriod();
	String getTitle();
	String getMessage();
	String getIcon();
	boolean isCancellable();
	Map<String, Object> getExtras();
}
