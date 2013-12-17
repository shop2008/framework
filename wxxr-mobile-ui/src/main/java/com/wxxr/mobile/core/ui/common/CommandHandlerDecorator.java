/**
 * 
 */
package com.wxxr.mobile.core.ui.common;

import com.wxxr.mobile.core.command.annotation.ConstraintLiteral;
import com.wxxr.mobile.core.ui.api.INavigationDescriptor;
import com.wxxr.mobile.core.ui.api.IProgressGuard;
import com.wxxr.mobile.core.ui.api.IUICommandHandler;
import com.wxxr.mobile.core.ui.api.InputEvent;

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
	 * @param event
	 * @return
	 * @see com.wxxr.mobile.core.ui.api.IUICommandHandler#execute(com.wxxr.mobile.core.ui.api.InputEvent)
	 */
	public Object execute(InputEvent event) {
		return handler.execute(event);
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
}
