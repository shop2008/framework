package com.wxxr.mobile.stock.app.service.handler;

import com.wxxr.mobile.core.command.api.ICommand;

public class ReadRemindMessageCommand implements ICommand<Void>{

	private String id;
	
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String getCommandName() {
		return ReadRemindMessageHandler.COMMAND_NAME;
	}

	@Override
	public Class<Void> getResultType() {
		return Void.class;
	}

	@Override
	public void validate() {
		
	}
	
	
}