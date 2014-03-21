package com.wxxr.mobile.stock.app.service.loader;

import java.util.List;

import com.wxxr.mobile.core.command.api.ICommand;
import com.wxxr.mobile.stock.app.bean.RemindMessageBean;

class UnReadRemindingMessagesCommand implements ICommand<List<RemindMessageBean>>{
	public static final String COMMAND_NAME = "UnReadRemindingMessagesCommand";

	@Override
	public String getCommandName() {
		return COMMAND_NAME;
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