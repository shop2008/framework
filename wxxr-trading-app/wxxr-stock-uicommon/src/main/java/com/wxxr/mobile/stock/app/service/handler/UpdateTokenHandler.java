package com.wxxr.mobile.stock.app.service.handler;

import com.wxxr.mobile.core.async.api.IAsyncCallback;
import com.wxxr.stock.crm.customizing.ejb.api.TokenVO;
import com.wxxr.stock.restful.resource.StockUserResource;
import com.wxxr.stock.restful.resource.StockUserResourceAsync;

public class UpdateTokenHandler extends BasicCommandHandler<TokenVO,UpdateTokenCommand> {

	public static final String COMMAND_NAME="UpdateTokenCommand";
	
	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.command.api.ICommandHandler#execute(com.wxxr.mobile.core.command.api.ICommand)
	 */
	@Override
	public void execute(final UpdateTokenCommand command, IAsyncCallback<TokenVO> callback) {
		getRestService(StockUserResourceAsync.class,StockUserResource.class).updateToken(new TokenVO()).onResult(callback);
	}

}
