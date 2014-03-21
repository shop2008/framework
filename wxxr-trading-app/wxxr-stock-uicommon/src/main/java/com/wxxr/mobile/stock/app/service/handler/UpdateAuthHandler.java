/**
 * 
 */
package com.wxxr.mobile.stock.app.service.handler;

import com.wxxr.mobile.core.async.api.IAsyncCallback;
import com.wxxr.security.vo.UserAuthenticaVO;
import com.wxxr.stock.common.valobject.ResultBaseVO;
import com.wxxr.stock.restful.resource.StockUserResource;
import com.wxxr.stock.restful.resource.StockUserResourceAsync;

/**
 * @author wangyan
 *
 */
public class UpdateAuthHandler extends BasicCommandHandler<ResultBaseVO,UpdateAuthCommand>{
	
	public final static String COMMAND_NAME="UpdateAuthCommand";

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.command.api.ICommandHandler#execute(com.wxxr.mobile.core.command.api.ICommand)
	 */
	@Override
	public void execute(final UpdateAuthCommand command, IAsyncCallback<ResultBaseVO> callback) {
		UserAuthenticaVO vo=new UserAuthenticaVO();
		vo.setAcctName(command.getAccountName());
		vo.setAcctBank(command.getBankName());
		vo.setBankNum(command.getBankNum());
		vo.setBankPosition(command.getBankAddr());
		getRestService(StockUserResourceAsync.class,StockUserResource.class).updateAttributeIdentify(vo).onResult(callback);
	}
}
