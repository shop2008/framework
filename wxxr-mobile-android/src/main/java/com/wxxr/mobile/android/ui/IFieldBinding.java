/**
 * 
 */
package com.wxxr.mobile.android.ui;

import com.wxxr.mobile.core.ui.api.DataChangedEvent;

/**
 * @author neillin
 *
 */
public interface IFieldBinding {
	void updateUI(boolean recursive);
	void handleDataChanged(DataChangedEvent event);
	void init(IBindingContext ctx);
	void activate();
	void deactivate();
	void destroy();
}
