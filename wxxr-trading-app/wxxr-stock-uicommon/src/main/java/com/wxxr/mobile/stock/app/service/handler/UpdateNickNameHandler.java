package com.wxxr.mobile.stock.app.service.handler;

import com.wxxr.mobile.core.command.api.CommandException;
import com.wxxr.mobile.core.command.api.ICommand;
import com.wxxr.mobile.core.command.api.ICommandExecutionContext;
import com.wxxr.mobile.core.command.api.ICommandHandler;
import com.wxxr.mobile.core.rpc.http.api.IRestProxyService;
import com.wxxr.mobile.core.util.StringUtils;
import com.wxxr.security.vo.UserParamVO;
import com.wxxr.stock.common.valobject.ResultBaseVO;
import com.wxxr.stock.restful.resource.StockUserResource;

public class UpdateNickNameHandler implements ICommandHandler {
	private ICommandExecutionContext context;
	public final static String COMMAND_NAME="UpdateNickNameCommand";
	
	
	public static class UpdateNickNameCommand implements ICommand<ResultBaseVO>{

		private String nickName;
		
		
		/**
		 * @return the nickName
		 */
		public String getNickName() {
			return nickName;
		}

		/**
		 * @param nickName the nickName to set
		 */
		public void setNickName(String nickName) {
			this.nickName = nickName;
		}

		@Override
		public String getCommandName() {
			return COMMAND_NAME;
		}

		@Override
		public Class<ResultBaseVO> getResultType() {
			return ResultBaseVO.class;
		}

		@Override
		public void validate() {
			if(StringUtils.isBlank(nickName)){
				throw new CommandException("昵称不能为空");
			}
		}
		
	}
	
	@Override
	public void destroy() {

	}

	@Override
	public <T> T execute(ICommand<T> cmd) throws Exception {
		UpdateNickNameCommand command=(UpdateNickNameCommand)cmd;
		UserParamVO vo=new UserParamVO();
		vo.setNickName(command.getNickName());
		ResultBaseVO resultBaseVO=context.getKernelContext().getService(IRestProxyService.class).
			getRestService(StockUserResource.class).updateNickName(vo);
		return (T) resultBaseVO;
	}

	@Override
	public void init(ICommandExecutionContext context) {
		this.context=context;
	}

}
