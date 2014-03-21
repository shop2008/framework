package com.wxxr.mobile.stock.app.command;

import java.util.List;

import com.wxxr.mobile.core.command.annotation.NetworkConstraint;
import com.wxxr.mobile.core.command.api.ICommand;
import com.wxxr.stock.trading.ejb.api.RegularTicketVO;

@NetworkConstraint
public class GetRegularTicketItemsCommand implements ICommand<List<RegularTicketVO>> {

	public final static String COMMAND_NAME = "GetRegularTicketItems";

	@Override
	public String getCommandName() {
		return COMMAND_NAME;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Class<List<RegularTicketVO>> getResultType() {
		Class clazz = List.class;
		return clazz;
	}

	@Override
	public void validate() {
	}
	
}