package com.wxxr.mobile.stock.app.service.handler;

import com.wxxr.mobile.core.command.api.CommandException;
import com.wxxr.mobile.core.command.api.ICommand;
import com.wxxr.mobile.core.util.StringUtils;
import com.wxxr.stock.common.valobject.ResultBaseVO;

public class SubmitAuthCommand implements ICommand<ResultBaseVO>{

	/**用户名*/
	private String accountName;
	
	/**开户行*/
	private String bankName;
	
	/**开户行所在地*/
	private String bankAddr;
	
	/**银行卡号*/
	private String bankNum;
	
	
	/**
	 * @return the accountName
	 */
	public String getAccountName() {
		return accountName;
	}

	/**
	 * @param accountName the accountName to set
	 */
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	/**
	 * @return the bankName
	 */
	public String getBankName() {
		return bankName;
	}

	/**
	 * @param bankName the bankName to set
	 */
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	/**
	 * @return the bankAddr
	 */
	public String getBankAddr() {
		return bankAddr;
	}

	/**
	 * @param bankAddr the bankAddr to set
	 */
	public void setBankAddr(String bankAddr) {
		this.bankAddr = bankAddr;
	}

	/**
	 * @return the bankNum
	 */
	public String getBankNum() {
		return bankNum;
	}

	/**
	 * @param bankNum the bankNum to set
	 */
	public void setBankNum(String bankNum) {
		this.bankNum = bankNum;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.command.api.ICommand#getCommandName()
	 */
	@Override
	public String getCommandName() {
		return SumitAuthHandler.COMMAND_NAME;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.command.api.ICommand#getResultType()
	 */
	@Override
	public Class<ResultBaseVO> getResultType() {
		return ResultBaseVO.class;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.command.api.ICommand#validate()
	 */
	@Override
	public void validate() {		/**用户名*/
		if(StringUtils.isBlank(accountName)){
			throw new CommandException("开户名不能为空");
		}
		if(StringUtils.isBlank(bankName)){
			throw new CommandException("开户行不能为空");
		}
		if(StringUtils.isBlank(bankAddr)){
			throw new CommandException("开户行所在地不能为空");
		}
		if(StringUtils.isBlank(bankNum)){
			throw new CommandException("银行卡号不能为空");
		}
	}
	
}