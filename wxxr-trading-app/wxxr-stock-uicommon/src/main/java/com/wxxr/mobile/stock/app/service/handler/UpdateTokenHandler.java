package com.wxxr.mobile.stock.app.service.handler;

import com.wxxr.mobile.core.command.api.ICommand;
import com.wxxr.mobile.core.command.api.ICommandExecutionContext;
import com.wxxr.mobile.core.command.api.ICommandHandler;
import com.wxxr.mobile.core.rpc.http.api.IRestProxyService;
import com.wxxr.stock.crm.customizing.ejb.api.TokenVO;
import com.wxxr.stock.restful.resource.StockUserResource;

public class UpdateTokenHandler implements ICommandHandler {

	public static final String COMMAND_NAME="UpdateTokenCommand";
	
	private ICommandExecutionContext context;
	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.command.api.ICommandHandler#destroy()
	 */
	@Override
	public void destroy() {
		context=null;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.command.api.ICommandHandler#execute(com.wxxr.mobile.core.command.api.ICommand)
	 */
	@Override
	public <T> T execute(ICommand<T> command) throws Exception {
		UpdateTokenCommand cmd=(UpdateTokenCommand) command;
		TokenVO vo=context.getKernelContext().getService(IRestProxyService.class).
				getRestService(StockUserResource.class).updateToken(new TokenVO());
		return (T) vo;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.command.api.ICommandHandler#init(com.wxxr.mobile.core.command.api.ICommandExecutionContext)
	 */
	@Override
	public void init(ICommandExecutionContext context) {
		this.context=context;
	}
	
	public static class UpdateTokenCommand implements ICommand<TokenVO>{

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

}
