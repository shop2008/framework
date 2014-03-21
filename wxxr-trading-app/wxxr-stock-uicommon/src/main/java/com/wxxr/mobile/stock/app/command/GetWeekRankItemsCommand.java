package com.wxxr.mobile.stock.app.command;

import java.util.List;

import com.wxxr.mobile.core.command.annotation.NetworkConstraint;
import com.wxxr.mobile.core.command.api.ICommand;
import com.wxxr.stock.trading.ejb.api.WeekRankVO;

@NetworkConstraint
public class GetWeekRankItemsCommand implements ICommand<List<WeekRankVO>> {
	public static final String COMMAND_NAME = "GetWeekRankItems";

	@Override
	public String getCommandName() {
		return COMMAND_NAME;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Class<List<WeekRankVO>> getResultType() {
		Class clazz = List.class;
		return clazz;
	}

	@Override
	public void validate() {
	}
	
}