/**
 * 
 */
package com.wxxr.mobile.core.ui.api;

/**
 * @author neillin
 *
 */
public interface IBinding {
	void notifyDataChanged(DataChangedEvent event);
	void activate();
	void deactivate();
	void destroy();
}
