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
	Object execute(InputEvent event);
	INavigationDescriptor[] getNavigations();
	IProgressGuard getProgressGuard();
	ConstraintLiteral[] getConstraints();
	void validateUserInput() throws ValidationException;
}
