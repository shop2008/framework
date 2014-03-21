package com.wxxr.mobile.stock.app.service.handler;

import com.wxxr.mobile.core.command.api.ICommand;

public class ReadAllUnreadMessageCommand implements ICommand<Void>{

	@Override
	public String getCommandName() {
		return ReadAllUnreadMessageHandler.COMMAND_NAME;
	}

	@Override
	public Class<Void> getResultType() {
		return Void.class;
	}

	@Override
	public void validate() {
		
	}
	
}