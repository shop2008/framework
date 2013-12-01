/**
 * 
 */
package com.wxxr.mobile.stock.app.service.handler;

import com.wxxr.mobile.core.command.api.CommandException;
import com.wxxr.mobile.core.command.api.ICommand;
import com.wxxr.mobile.core.command.api.ICommandExecutionContext;
import com.wxxr.mobile.core.command.api.ICommandHandler;
import com.wxxr.mobile.core.rpc.http.api.IRestProxyService;
import com.wxxr.mobile.core.util.StringUtils;
import com.wxxr.security.vo.UpdatePwdVO;
import com.wxxr.stock.common.valobject.ResultBaseVO;
import com.wxxr.stock.restful.resource.StockUserResource;

/**
 * @author wangyan
 *
 */
public class UpPwdHandler implements ICommandHandler{

	public final static String COMMAND_NAME="UpPwdCommand";
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
	public <T> T execute(ICommand<T> cmd) throws Exception {
		UpPwdCommand upCmd=(UpPwdCommand) cmd;
		UpdatePwdVO updatePwdvo=new UpdatePwdVO();
		updatePwdvo.setOldPwd(upCmd.getOldPwd());
		updatePwdvo.setPassword(upCmd.getNewPwd());
		ResultBaseVO resultBaseVO=context.getKernelContext().getService(IRestProxyService.class)
				.getRestService(StockUserResource.class).updatePwd(updatePwdvo);
		return (T) resultBaseVO;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.command.api.ICommandHandler#init(com.wxxr.mobile.core.command.api.ICommandExecutionContext)
	 */
	@Override
	public void init(ICommandExecutionContext context) {
		this.context=context;
	}

	public static class UpPwdCommand implements ICommand<ResultBaseVO>{

		private String newPwd;
		private	String newPwd2;
		private String oldPwd;
		
		
		/**
		 * @return the newPwd
		 */
		public String getNewPwd() {
			return newPwd;
		}

		/**
		 * @param newPwd the newPwd to set
		 */
		public void setNewPwd(String newPwd) {
			this.newPwd = newPwd;
		}

		/**
		 * @return the newPwd2
		 */
		public String getNewPwd2() {
			return newPwd2;
		}

		/**
		 * @param newPwd2 the newPwd2 to set
		 */
		public void setNewPwd2(String newPwd2) {
			this.newPwd2 = newPwd2;
		}

		/**
		 * @return the oldPwd
		 */
		public String getOldPwd() {
			return oldPwd;
		}

		/**
		 * @param oldPwd the oldPwd to set
		 */
		public void setOldPwd(String oldPwd) {
			this.oldPwd = oldPwd;
		}

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
		public Class<ResultBaseVO> getResultType() {
			return ResultBaseVO.class;
		}

		/* (non-Javadoc)
		 * @see com.wxxr.mobile.core.command.api.ICommand#validate()
		 */
		@Override
		public void validate() {
			if(StringUtils.isBlank(oldPwd)){
				throw new CommandException("旧密码不能为空");
			}
			if(StringUtils.isBlank(newPwd)){
				throw new CommandException("新密码不能为空");
			}
			if(!newPwd.equals(newPwd2)){
				throw new CommandException("两次密码不一致");
			}
		}
		
	}
}
