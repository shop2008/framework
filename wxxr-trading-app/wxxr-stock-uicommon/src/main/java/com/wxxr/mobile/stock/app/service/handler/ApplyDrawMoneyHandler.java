/**
 * 
 */
package com.wxxr.mobile.stock.app.service.handler;

import com.wxxr.mobile.core.command.api.CommandException;
import com.wxxr.mobile.core.command.api.ICommand;
import com.wxxr.mobile.core.command.api.ICommandExecutionContext;
import com.wxxr.mobile.core.command.api.ICommandHandler;
import com.wxxr.mobile.core.rpc.http.api.IRestProxyService;
import com.wxxr.mobile.stock.app.service.handler.SumitAuthHandler.SubmitAuthCommand;
import com.wxxr.security.vo.UserAuthenticaVO;
import com.wxxr.stock.common.valobject.ResultBaseVO;
import com.wxxr.stock.restful.resource.ITradingProtectedResource;
import com.wxxr.stock.restful.resource.StockUserResource;
import com.wxxr.stock.trading.ejb.api.StockResultVO;

/**
 * @author wangyan
 *
 */
public class ApplyDrawMoneyHandler implements ICommandHandler {

	private ICommandExecutionContext context;
	public static final String COMMAND_NAME="ApplyDrawMoneyCommand";
	
	
	public static class ApplyDrawMoneyCommand implements ICommand<StockResultVO>{

		private long amount;
		
		
		/**
		 * @return the amount
		 */
		public long getAmount() {
			return amount;
		}

		/**
		 * @param amount the amount to set
		 */
		public void setAmount(long amount) {
			this.amount = amount;
		}

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
			if(amount<=0){
				throw new CommandException("提取金额必须大于0");
			}
			if (amount%10000!=0) {
				throw new CommandException("提取金额必须为100整数倍");
			}
		}
		
	}
	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.command.api.ICommandHandler#destroy()
	 */
	@Override
	public void destroy() {

	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.command.api.ICommandHandler#execute(com.wxxr.mobile.core.command.api.ICommand)
	 */
	@Override
	public <T> T execute(ICommand<T> cmd) throws Exception {
		ApplyDrawMoneyCommand command=(ApplyDrawMoneyCommand)cmd;

		StockResultVO result=context.getKernelContext().getService(IRestProxyService.class).
			getRestService(ITradingProtectedResource.class).drawMoney(command.getAmount());
		return (T)  result;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.command.api.ICommandHandler#init(com.wxxr.mobile.core.command.api.ICommandExecutionContext)
	 */
	@Override
	public void init(ICommandExecutionContext context) {
		this.context=context;
	}

}
