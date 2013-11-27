/**
 * 
 */
package com.wxxr.mobile.core.command.common;

import com.wxxr.mobile.core.api.IDataExchangeCoordinator;
import com.wxxr.mobile.core.command.annotation.NetworkConnectionType;
import com.wxxr.mobile.core.command.annotation.NetworkConstraint;
import com.wxxr.mobile.core.command.api.CommandConstraintViolatedException;
import com.wxxr.mobile.core.command.api.ICommand;
import com.wxxr.mobile.core.command.api.ICommandExecutionContext;
import com.wxxr.mobile.core.command.api.ICommandValidator;
import com.wxxr.mobile.core.command.api.RequiredNetNotAvailablexception;

/**
 * @author neillin
 *
 */
public class NetworkConstaintValidator implements ICommandValidator {

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
		NetworkConstraint constraint = command.getClass().getAnnotation(NetworkConstraint.class);
		if(constraint == null){
			return;
		}
		NetworkConnectionType[] allowedTypes = constraint.allowConnectionTypes();
		IDataExchangeCoordinator coordinator = context.getKernelContext().getService(IDataExchangeCoordinator.class);
		if(coordinator == null){
			return;
		}
		int availableNetwork = coordinator.checkAvailableNetwork();
		if((allowedTypes.length == 0)&&(availableNetwork > 0)){
			return;
		}
		NetworkConnectionType availableNetworkType = NetworkConnectionType.byId(availableNetwork);
		for (NetworkConnectionType type : allowedTypes) {
			if(type == availableNetworkType){
				return;
			}
		}
		throw new RequiredNetNotAvailablexception(constraint);
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.command.api.ICommandValidator#destroy()
	 */
	public void destroy() {
		this.context = null;
	}

}