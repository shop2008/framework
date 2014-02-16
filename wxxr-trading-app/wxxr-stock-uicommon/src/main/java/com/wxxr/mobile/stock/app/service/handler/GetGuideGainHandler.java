/**
 * 
 */
package com.wxxr.mobile.stock.app.service.handler;

import com.wxxr.mobile.core.command.api.CommandException;
import com.wxxr.mobile.core.command.api.ICommand;
import com.wxxr.mobile.core.command.api.ICommandExecutionContext;
import com.wxxr.mobile.core.command.api.ICommandHandler;
import com.wxxr.mobile.core.microkernel.api.KUtils;
import com.wxxr.mobile.core.security.api.IUserIdentityManager;
import com.wxxr.mobile.stock.app.common.RestUtils;
import com.wxxr.stock.restful.resource.ITradingProtectedResource;
import com.wxxr.stock.trading.ejb.api.StockResultVO;

/**
 *
 */
public class GetGuideGainHandler implements ICommandHandler{

	public static final String COMMAND_NAME="GetGuideGainCommand";
	
	@Override
	public void destroy() {
	}
	@Override
	public void init(ICommandExecutionContext context) {
	}

	public static class GetGuideGainCommand implements ICommand<StockResultVO>{
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

	@SuppressWarnings("unchecked")
	@Override
	public <T> T execute(ICommand<T> command) throws Exception {
		GetGuideGainCommand cmd=(GetGuideGainCommand) command;
		cmd.validate();
		StockResultVO vo = RestUtils.getRestService(ITradingProtectedResource.class).guideGain();
		return (T)vo;
	}
}
