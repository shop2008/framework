/**
 * 
 */
package com.wxxr.mobile.core.ui.api;

/**
 * @author neillin
 *
 */
public interface IUICommandHandler {
	String execute(InputEvent event);
	INavigationDescriptor[] getNavigations();
}
