package com.wxxr.mobile.stock.app.service.handler;

import com.wxxr.mobile.core.command.api.ICommand;
import com.wxxr.stock.crm.customizing.ejb.api.TokenVO;

public class UpdateTokenCommand implements ICommand<TokenVO>{

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.command.api.ICommand#getCommandName()
	 */
	@Override
	public String getCommandName() {
		return UpdateTokenHandler.COMMAND_NAME;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.command.api.ICommand#getResultType()
	 */
	@Override
	public Class<TokenVO> getResultType() {
		return TokenVO.class;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.command.api.ICommand#validate()
	 */
	@Override
	public void validate() {
	}


}