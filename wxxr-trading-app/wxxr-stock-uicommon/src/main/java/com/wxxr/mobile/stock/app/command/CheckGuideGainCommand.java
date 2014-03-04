package com.wxxr.mobile.stock.app.command;

import com.wxxr.mobile.core.command.annotation.NetworkConstraint;
import com.wxxr.mobile.core.command.annotation.SecurityConstraint;
import com.wxxr.mobile.core.command.api.ICommand;
import com.wxxr.stock.trading.ejb.api.GuideResultVO;

@NetworkConstraint
@SecurityConstraint(allowRoles={})
public class CheckGuideGainCommand implements ICommand<GuideResultVO>{
	
	public static final String COMMAND_NAME="CheckGuideGainCommand";
	

	@Override
	public String getCommandName() {
		return COMMAND_NAME;
	}

	@Override
	public Class<GuideResultVO> getResultType() {
		return GuideResultVO.class;
	}

	@Override
	public void validate() {
		/*if (!KUtils.getService(IUserIdentityManager.class).isUserAuthenticated()) {
			throw new CommandException("用户未登录!");
		}*/
	}
}
