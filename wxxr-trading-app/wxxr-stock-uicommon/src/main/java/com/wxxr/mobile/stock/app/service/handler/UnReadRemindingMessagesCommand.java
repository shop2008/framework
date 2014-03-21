package com.wxxr.mobile.stock.app.service.handler;

import java.util.List;

import com.wxxr.mobile.core.command.api.ICommand;
import com.wxxr.mobile.stock.app.bean.RemindMessageBean;

public class UnReadRemindingMessagesCommand implements ICommand<List<RemindMessageBean>>{

	@Override
	public String getCommandName() {
		return UnReadRemindingMessagesHandler.COMMAND_NAME;
	}

	@Override
	public Class<List<RemindMessageBean>> getResultType() {
		Class clazz=List.class;
		return clazz;
	}

	@Override
	public void validate() {
		
	}
	
}