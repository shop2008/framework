package com.wxxr.mobile.stock.app.service.handler;

import com.wxxr.mobile.core.command.api.ICommand;
import com.wxxr.mobile.core.command.api.ICommandExecutionContext;
import com.wxxr.mobile.core.command.api.ICommandHandler;
import com.wxxr.mobile.core.rpc.http.api.IRestProxyService;
import com.wxxr.stock.restful.resource.ITradingResource;
import com.wxxr.stock.trading.ejb.api.UserSignVO;

public class SignUpHandler implements ICommandHandler {

	public static final String COMMAND_NAME="SignUpCommand";
	
	private ICommandExecutionContext context;
	
	@Override
	public void destroy() {
		context=null;
	}

	
	@Override
	public <T> T execute(ICommand<T> command) throws Exception {
		SignUpCommand cmd=(SignUpCommand) command;
		UserSignVO vo=context.getKernelContext().getService(IRestProxyService.class).getRestService(ITradingResource.class).userSign();
		return (T) vo;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.command.api.ICommandHandler#init(com.wxxr.mobile.core.command.api.ICommandExecutionContext)
	 */
	@Override
	public void init(ICommandExecutionContext context) {
		this.context=context;
	}
	
	public static class SignUpCommand implements ICommand<UserSignVO>{

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

}
