package com.wxxr.mobile.stock.app.service.handler;

import com.wxxr.mobile.core.command.annotation.NetworkConstraint;
import com.wxxr.mobile.core.command.annotation.SecurityConstraint;
import com.wxxr.mobile.core.command.api.ICommand;
@NetworkConstraint(allowConnectionTypes={})
@SecurityConstraint(allowRoles = {})
public class SubmitPushMesasgeCommand implements ICommand<Boolean>{

	private boolean binding;
	
	
	
	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.command.api.ICommand#getCommandName()
	 */
	@Override
	public String getCommandName() {
		return SubmitPushMesasgeHandler.COMMAND_NAME;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.command.api.ICommand#getResultType()
	 */
	@Override
	public Class<Boolean> getResultType() {
		return Boolean.class;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.command.api.ICommand#validate()
	 */
	@Override
	public void validate() {
		
	}

	/**
	 * @return the binding
	 */
	public boolean isBinding() {
		return binding;
	}

	/**
	 * @param binding the binding to set
	 */
	public void setBinding(boolean binding) {
		this.binding = binding;
	}
	
}