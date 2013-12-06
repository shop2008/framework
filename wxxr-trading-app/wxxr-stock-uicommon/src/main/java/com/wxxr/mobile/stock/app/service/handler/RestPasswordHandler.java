package com.wxxr.mobile.stock.app.service.handler;

import org.apache.commons.lang.StringUtils;

import com.wxxr.mobile.core.command.annotation.NetworkConstraint;
import com.wxxr.mobile.core.command.api.CommandException;
import com.wxxr.mobile.core.command.api.ICommand;
import com.wxxr.mobile.core.command.api.ICommandExecutionContext;
import com.wxxr.mobile.core.command.api.ICommandHandler;
import com.wxxr.mobile.core.rpc.http.api.IRestProxyService;
import com.wxxr.stock.restful.resource.StockUserResource;

public class RestPasswordHandler implements ICommandHandler {

	public static final String COMMAND_NAME = "RestPasswordCommand";

	private ICommandExecutionContext context;
	@NetworkConstraint
	public static class RestPasswordCommand implements ICommand<Void>{

		private String userName;
		
		
		/**
		 * @return the userName
		 */
		public String getUserName() {
			return userName;
		}

		/**
		 * @param userName the userName to set
		 */
		public void setUserName(String userName) {
			this.userName = userName;
		}

		@Override
		public String getCommandName() {
			return COMMAND_NAME;
		}

		@Override
		public Class<Void> getResultType() {
			return Void.class;
		}

		@Override
		public void validate() {
			if(StringUtils.isBlank(userName)){
				throw new CommandException("用户名不能为空");
			}
		}
		
	}
	@Override
	public void destroy() {
		context=null;
	}

	@Override
	public <T> T execute(ICommand<T> command) throws Exception {
		context.getKernelContext().getService(IRestProxyService.class).
		getRestService(StockUserResource.class).resetPassword(((RestPasswordCommand)command).getUserName());
		return null;
	}

	@Override
	public void init(ICommandExecutionContext context) {
		this.context=context;
	}

}
