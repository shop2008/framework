package com.wxxr.mobile.stock.app.service.handler;

import com.wxxr.mobile.core.command.api.CommandException;
import com.wxxr.mobile.core.command.api.ICommand;
import com.wxxr.mobile.core.command.api.ICommandExecutionContext;
import com.wxxr.mobile.core.command.api.ICommandHandler;
import com.wxxr.mobile.core.rpc.http.api.IRestProxyService;
import com.wxxr.mobile.core.util.StringUtils;
import com.wxxr.security.vo.SimpleResultVo;
import com.wxxr.stock.restful.resource.UserResource;

public class RegisterHandher implements ICommandHandler {

	public static final String COMMAND_NAME = "UserRegisterCommand";
	private ICommandExecutionContext context;

	
	public static class UserRegisterCommand implements ICommand<SimpleResultVo>{
		private String userName;
		@Override
		public String getCommandName() {
			return COMMAND_NAME;
		}

		@Override
		public Class<SimpleResultVo> getResultType() {
			return SimpleResultVo.class;
		}

		@Override
		public void validate() {
			if(!StringUtils.isNumeric(userName)||userName.length()!=11){
				throw new CommandException("请输入正确的手机号");
			}
			
		}

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
		
		
	}
	@Override
	public void destroy() {
		
	}

	
	@SuppressWarnings("unchecked")
	@Override
	public <T> T execute(ICommand<T> command) throws Exception {
		SimpleResultVo result=context.getKernelContext().getService(IRestProxyService.class).getRestService(UserResource.class).register(((UserRegisterCommand)command).getUserName());
		return (T) result;
	}

	@Override
	public void init(ICommandExecutionContext context) {
		this.context=context;
	}

}
