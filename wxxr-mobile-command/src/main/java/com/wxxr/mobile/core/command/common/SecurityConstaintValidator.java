/**
 * 
 */
package com.wxxr.mobile.core.command.common;

import com.wxxr.mobile.core.command.annotation.SecurityConstraint;
import com.wxxr.mobile.core.command.api.CommandConstraintViolatedException;
import com.wxxr.mobile.core.command.api.ICommand;
import com.wxxr.mobile.core.command.api.ICommandExecutionContext;
import com.wxxr.mobile.core.command.api.ICommandValidator;
import com.wxxr.mobile.core.command.api.UserLoginRequiredException;
import com.wxxr.mobile.core.command.api.UserNotAuthorizedException;
import com.wxxr.mobile.core.security.api.IUserIdentityManager;

/**
 * @author neillin
 *
 */
public class SecurityConstaintValidator implements ICommandValidator {

	private ICommandExecutionContext context;
	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.command.api.ICommandValidator#init(com.wxxr.mobile.core.command.api.ICommandExecutionContext)
	 */
	public void init(ICommandExecutionContext ctx) {
		this.context = ctx;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.command.api.ICommandValidator#checkCommandConstraints(com.wxxr.mobile.core.command.api.ICommand)
	 */
	public void checkCommandConstraints(ICommand<?> command)
			throws CommandConstraintViolatedException {
		SecurityConstraint constraint = command.getClass().getAnnotation(SecurityConstraint.class);
		if(constraint == null){
			return;
		}
		String[] allowedRoles= constraint.allowRoles();
		IUserIdentityManager mgr = context.getKernelContext().getService(IUserIdentityManager.class);
		if(mgr == null){
			return;
		}
		if(!mgr.isUserAuthenticated()){
			throw new UserLoginRequiredException(constraint);
		}
		if(allowedRoles.length == 0){
			return;
		}
		if(!mgr.usrHasRoles(allowedRoles)){
			throw new UserNotAuthorizedException(constraint);
		}
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.command.api.ICommandValidator#destroy()
	 */
	public void destroy() {
		this.context = null;
	}

}
