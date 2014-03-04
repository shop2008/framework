package com.wxxr.mobile.stock.app.command;

import com.wxxr.mobile.core.command.api.ICommand;
import com.wxxr.stock.trading.ejb.api.UserSignVO;
public class GetUserSignMessageCommand implements ICommand<UserSignVO>{

	public static final String COMMAND_NAME="GetUserSignMessageCommand";

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.command.api.ICommand#getCommandName()
	 */
	@Override
	public String getCommandName() {
		return COMMAND_NAME;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.command.api.ICommand#getResultType()
	 */
	@Override
	public Class<UserSignVO> getResultType() {
		return UserSignVO.class;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.command.api.ICommand#validate()
	 */
	@Override
	public void validate() {
	}


}
