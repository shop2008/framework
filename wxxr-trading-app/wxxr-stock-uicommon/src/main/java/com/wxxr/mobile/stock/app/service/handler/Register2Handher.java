package com.wxxr.mobile.stock.app.service.handler;

import com.wxxr.mobile.core.command.api.ICommand;
import com.wxxr.mobile.core.command.api.ICommandExecutionContext;
import com.wxxr.mobile.core.command.api.ICommandHandler;
import com.wxxr.mobile.core.rpc.http.api.IRestProxyService;
import com.wxxr.security.vo.SimpleResultVo;
import com.wxxr.stock.restful.json.RegQueryVO;
import com.wxxr.stock.restful.resource.UserResource;

public class Register2Handher implements ICommandHandler {

	public static final String COMMAND_NAME = "UserRegister2Command";
	private ICommandExecutionContext context;

	
	public static class UserRegister2Command implements ICommand<SimpleResultVo>{
		private String userName;
		private String password;
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

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}
		
		
	}
	@Override
	public void destroy() {
		
	}

	
	@Override
	public <T> T execute(ICommand<T> command) throws Exception {
		UserRegister2Command cmd = (UserRegister2Command)command;
		RegQueryVO vo = new RegQueryVO();
		vo.setUsername(cmd.getUserName());
		vo.setPassword(cmd.getPassword());
		SimpleResultVo result=context.getKernelContext().getService(IRestProxyService.class).getRestService(UserResource.class).registerWithPassword(vo);
		return (T) result;
	}

	@Override
	public void init(ICommandExecutionContext context) {
		this.context=context;
	}

}
