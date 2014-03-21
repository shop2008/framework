package com.wxxr.mobile.stock.app.service.handler;

import com.wxxr.mobile.core.command.api.ICommand;

public class GetUserAuthSettingCommand implements ICommand<Boolean>{

	public static final String COMMAND_NAME="GetUserAuthSettingCommand";
	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.command.api.ICommand#getCommandName()
	 */
	@Override
	public String getCommandName() {
		return COMMAND_NAME;
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


}
