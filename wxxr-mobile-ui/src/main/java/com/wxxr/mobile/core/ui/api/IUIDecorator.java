/**
 * 
 */
package com.wxxr.mobile.core.ui.api;

/**
 * @author neillin
 *
 */
public interface IUIDecorator {
	void handleUIUpdating(IUIUpdatingContext context,IUIComponent comp, Object uiControl);
}
