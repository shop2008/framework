package com.wxxr.mobile.stock.app.command;

import java.util.List;

import com.wxxr.mobile.core.command.annotation.NetworkConstraint;
import com.wxxr.mobile.core.command.api.ICommand;
import com.wxxr.stock.trading.ejb.api.MegagameRankVO;

@NetworkConstraint
public class GetT1RankItemsCommand implements ICommand<List<MegagameRankVO>> {

	public final static String COMMAND_NAME = "GetT1RankItems";

	@Override
	public String getCommandName() {
		return COMMAND_NAME;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Class<List<MegagameRankVO>> getResultType() {
		Class clazz = List.class;
		return clazz;
	}

	@Override
	public void validate() {
	}
	
}