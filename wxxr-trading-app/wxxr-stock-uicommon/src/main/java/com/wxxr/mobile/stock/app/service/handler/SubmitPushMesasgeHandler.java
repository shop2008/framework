/**
 * 
 */
package com.wxxr.mobile.stock.app.service.handler;

import com.wxxr.mobile.core.command.api.CommandException;
import com.wxxr.mobile.core.command.api.ICommand;
import com.wxxr.mobile.core.command.api.ICommandExecutionContext;
import com.wxxr.mobile.core.command.api.ICommandHandler;
import com.wxxr.mobile.core.rpc.http.api.IRestProxyService;
import com.wxxr.mobile.stock.app.RestBizException;
import com.wxxr.stock.restful.resource.StockUserResource;

/**
 * @author wangyan
 *
 */
public class SubmitPushMesasgeHandler implements ICommandHandler{

	public static final String COMMAND_NAME="SubmitPushMesasgeCommand";
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
		SubmitPushMesasgeCommand cmd=(SubmitPushMesasgeCommand) command;
		if(cmd.isBinding()){
			try {
				context.getKernelContext().getService(IRestProxyService.class).
						getRestService(StockUserResource.class).bindApp();
			} catch (RestBizException e) {
				throw new CommandException("绑定失败");
			}
		}else{
			try {
				context.getKernelContext().getService(IRestProxyService.class).
				getRestService(StockUserResource.class).unbindApp();
			} catch (Exception e) {
				throw new CommandException("解绑失败");
			}
		}
		return (T) Boolean.TRUE;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.command.api.ICommandHandler#init(com.wxxr.mobile.core.command.api.ICommandExecutionContext)
	 */
	@Override
	public void init(ICommandExecutionContext context) {
		this.context=context;
	}

	public static class SubmitPushMesasgeCommand implements ICommand<Boolean>{

		private boolean binding;
		
		
		
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
		public Class<Boolean> getResultType() {
			return Boolean.class;
		}

		/* (non-Javadoc)
		 * @see com.wxxr.mobile.core.command.api.ICommand#validate()
		 */
		@Override
		public void validate() {
			
		}

		/**
		 * @return the binding
		 */
		public boolean isBinding() {
			return binding;
		}

		/**
		 * @param binding the binding to set
		 */
		public void setBinding(boolean binding) {
			this.binding = binding;
		}
		
	}
}
