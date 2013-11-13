/**
 * 
 */
package com.wxxr.mobile.core.ui.api;

/**
 * @author neillin
 *
 */
public interface IUICommandHandler {
	Object execute(InputEvent event);
	INavigationDescriptor[] getNavigations();
	IProgressGuard getProgressGuard();
}
