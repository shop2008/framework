/**
 * 
 */
package com.wxxr.mobile.stock.app.service.handler;

import java.util.List;

import com.wxxr.mobile.core.command.api.ICommand;
import com.wxxr.mobile.core.command.api.ICommandExecutionContext;
import com.wxxr.mobile.core.command.api.ICommandHandler;
import com.wxxr.mobile.core.rpc.http.api.IRestProxyService;
import com.wxxr.stock.crm.customizing.ejb.api.UserVO;
import com.wxxr.stock.restful.resource.StockUserResource;

/**
 * @author wangyan
 *
 */
public class RefresUserInfoHandler implements ICommandHandler {

	
	public static final String COMMAND_NAME = "RefreshUserInfoCommand";

	private ICommandExecutionContext context;
	public static class RefreshUserInfoCommand implements ICommand<UserVO>{

		@Override
		public String getCommandName() {
			return COMMAND_NAME;
		}

		@Override
		public Class<UserVO> getResultType() {
			
			return UserVO.class;
		}

		@Override
		public void validate() {
			
			
		}
		
	}
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
		UserVO userVo= context.getKernelContext().getService(IRestProxyService.class).getRestService(StockUserResource.class).getUser();
		return (T) userVo;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.command.api.ICommandHandler#init(com.wxxr.mobile.core.command.api.ICommandExecutionContext)
	 */
	@Override
	public void init(ICommandExecutionContext context) {
		this.context=context;
	}

}
