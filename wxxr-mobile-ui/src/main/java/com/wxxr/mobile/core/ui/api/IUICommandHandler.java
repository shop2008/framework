/**
 * 
 */
package com.wxxr.mobile.core.ui.api;

import com.wxxr.mobile.core.command.annotation.ConstraintLiteral;

/**
 * @author neillin
 *
 */
public interface IUICommandHandler {
	Object doProcess(InputEvent event);
	Object doNavigation(InputEvent event,Object result);
	INavigationDescriptor[] getNavigations();
	IProgressGuard getProgressGuard();
	ConstraintLiteral[] getConstraints();
	void validateUserInput() throws ValidationException;
	
	public enum ExecutionStep {
		PROCESS,
		NAVIGATION
	}

}
