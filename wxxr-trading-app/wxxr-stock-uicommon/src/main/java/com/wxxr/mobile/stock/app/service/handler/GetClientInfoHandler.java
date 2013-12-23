package com.wxxr.mobile.stock.app.service.handler;

import com.wxxr.mobile.core.command.api.ICommand;
import com.wxxr.mobile.core.command.api.ICommandExecutionContext;
import com.wxxr.mobile.core.command.api.ICommandHandler;
import com.wxxr.mobile.stock.app.common.RestUtils;
import com.wxxr.stock.restful.json.ClientInfoVO;
import com.wxxr.stock.restful.resource.ClientResource;

public class GetClientInfoHandler implements ICommandHandler {
	
	public static final String COMMAND_NAME = "ReadClientInfoCommand";
	private ICommandExecutionContext context;
	
	public static class ReadClientInfoCommand implements ICommand<ClientInfoVO>{
		@Override
		public String getCommandName() {
			return COMMAND_NAME;
		}

		@Override
		public Class<ClientInfoVO> getResultType() {
			return ClientInfoVO.class;
		}

		@Override
		public void validate() {
			
		}
	}
	@Override
	public void destroy() {

	}

	@Override
	public <T> T execute(ICommand<T> command) throws Exception {
		
		ClientInfoVO vo =RestUtils.getRestService(ClientResource.class).getClientInfo();
	
		return (T) vo;
	}

	@Override
	public void init(ICommandExecutionContext context) {
		this.context=context;
	}

}
