/**
 * 
 */
package com.wxxr.mobile.core.ui.common;

import com.wxxr.mobile.core.command.annotation.ConstraintLiteral;
import com.wxxr.mobile.core.ui.api.INavigationDescriptor;
import com.wxxr.mobile.core.ui.api.IProgressGuard;
import com.wxxr.mobile.core.ui.api.IUICommandHandler;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.api.ValidationException;

/**
 * @author neillin
 *
 */
public abstract class CommandHandlerDecorator implements IUICommandHandler {
	private final IUICommandHandler handler;
	
	public CommandHandlerDecorator(IUICommandHandler handler){
		this.handler = handler;
	}


	/**
	 * @return
	 * @see com.wxxr.mobile.core.ui.api.IUICommandHandler#getNavigations()
	 */
	public INavigationDescriptor[] getNavigations() {
		return handler.getNavigations();
	}

	/**
	 * @return
	 * @see com.wxxr.mobile.core.ui.api.IUICommandHandler#getProgressGuard()
	 */
	public IProgressGuard getProgressGuard() {
		return handler.getProgressGuard();
	}

	/**
	 * @return
	 * @see com.wxxr.mobile.core.ui.api.IUICommandHandler#getConstraints()
	 */
	public ConstraintLiteral[] getConstraints() {
		return handler.getConstraints();
	}


	/**
	 * @param event
	 * @return
	 * @see com.wxxr.mobile.core.ui.api.IUICommandHandler#doProcess(com.wxxr.mobile.core.ui.api.InputEvent)
	 */
	public Object doProcess(InputEvent event) {
		return handler.doProcess(event);
	}


	/**
	 * @param event
	 * @param result
	 * @return
	 * @see com.wxxr.mobile.core.ui.api.IUICommandHandler#doNavigation(com.wxxr.mobile.core.ui.api.InputEvent, java.lang.Object)
	 */
	public Object doNavigation(InputEvent event, Object result) {
		return handler.doNavigation(event, result);
	}


	/**
	 * @throws ValidationException
	 * @see com.wxxr.mobile.core.ui.api.IUICommandHandler#validateUserInput()
	 */
	public void validateUserInput() throws ValidationException {
		handler.validateUserInput();
	}
}
