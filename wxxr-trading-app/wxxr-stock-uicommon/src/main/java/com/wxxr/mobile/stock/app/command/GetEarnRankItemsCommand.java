package com.wxxr.mobile.stock.app.command;

import java.util.List;

import com.wxxr.mobile.core.command.annotation.NetworkConstraint;
import com.wxxr.mobile.core.command.api.ICommand;
import com.wxxr.stock.trading.ejb.api.HomePageVO;

@NetworkConstraint
public class GetEarnRankItemsCommand implements ICommand<List<HomePageVO>> {
	

	public final static String COMMAND_NAME = "GetEarnRankItems";

	private int start, limit;

	@Override
	public String getCommandName() {
		return COMMAND_NAME;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Class<List<HomePageVO>> getResultType() {
		Class clazz = List.class;
		return clazz;
	}

	@Override
	public void validate() {
		if((start < 0)||(limit <= 0)){
			throw new IllegalArgumentException("start and limit parameters must large than 0");
		}
	}

	/**
	 * @return the start
	 */
	public int getStart() {
		return start;
	}

	/**
	 * @return the limit
	 */
	public int getLimit() {
		return limit;
	}

	/**
	 * @param start the start to set
	 */
	public void setStart(int start) {
		this.start = start;
	}

	/**
	 * @param limit the limit to set
	 */
	public void setLimit(int limit) {
		this.limit = limit;
	}
	
}