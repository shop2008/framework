package com.wxxr.mobile.stock.app.service.handler;

import com.wxxr.mobile.core.command.api.CommandException;
import com.wxxr.mobile.core.command.api.ICommand;
import com.wxxr.stock.trading.ejb.api.StockResultVO;

public class ApplyDrawMoneyCommand implements ICommand<StockResultVO>{

	private long amount;
	
	
	/**
	 * @return the amount
	 */
	public long getAmount() {
		return amount;
	}

	/**
	 * @param amount the amount to set
	 */
	public void setAmount(long amount) {
		this.amount = amount;
	}

	@Override
	public String getCommandName() {
		return ApplyDrawMoneyHandler.COMMAND_NAME;
	}

	@Override
	public Class<StockResultVO> getResultType() {
		return StockResultVO.class;
	}

	@Override
	public void validate() {
		if(amount<=0){
			throw new CommandException("提取金额必须大于0");
		}
	}
	
}