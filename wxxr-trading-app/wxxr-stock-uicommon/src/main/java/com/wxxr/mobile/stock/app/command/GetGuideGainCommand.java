package com.wxxr.mobile.stock.app.command;

import com.wxxr.mobile.core.command.annotation.NetworkConstraint;
import com.wxxr.mobile.core.command.annotation.SecurityConstraint;
import com.wxxr.mobile.core.command.api.CommandException;
import com.wxxr.mobile.core.command.api.ICommand;
import com.wxxr.mobile.core.microkernel.api.KUtils;
import com.wxxr.mobile.core.security.api.IUserIdentityManager;
import com.wxxr.stock.trading.ejb.api.StockResultVO;

@NetworkConstraint
@SecurityConstraint(allowRoles={})
public class GetGuideGainCommand implements ICommand<StockResultVO>{
	
	public static final String COMMAND_NAME="GetGuideGainCommand";
	

	@Override
	public String getCommandName() {
		return COMMAND_NAME;
	}

	@Override
	public Class<StockResultVO> getResultType() {
		return StockResultVO.class;
	}

	@Override
	public void validate() {
		if (!KUtils.getService(IUserIdentityManager.class).isUserAuthenticated()) {
			throw new CommandException("用户未登录!");
		}
	}
}
