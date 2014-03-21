package com.wxxr.mobile.stock.app.service.handler;

import com.wxxr.mobile.core.command.api.ICommand;

public class ReadPullMessageCommand implements ICommand<Void>{

	private long id;
	@Override
	public String getCommandName() {
		return ReadPullMessageHandler.COMMAND_NAME;
	}

	@Override
	public Class<Void> getResultType() {
		return Void.class;
	}

	@Override
	public void validate() {
		
	}

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}
	
	
}