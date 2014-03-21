package com.wxxr.mobile.stock.app.service.handler;

import com.wxxr.mobile.core.command.api.ICommand;
import com.wxxr.security.vo.SimpleResultVo;

public class GetPushMessageSettingCommand implements ICommand<SimpleResultVo>{

	public static final String COMMAND_NAME="GetPushMessageSettingCommand";
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
	public Class<SimpleResultVo> getResultType() {
		return SimpleResultVo.class;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.command.api.ICommand#validate()
	 */
	@Override
	public void validate() {
	}


}
