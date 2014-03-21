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
public class SumitAuthHandler extends BasicCommandHandler<ResultBaseVO,SubmitAuthCommand>{
	
	public static final String COMMAND_NAME="SumitAuthCommand";

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.command.api.ICommandHandler#execute(com.wxxr.mobile.core.command.api.ICommand)
	 */
	@Override
	public void execute(final SubmitAuthCommand cmd, IAsyncCallback<ResultBaseVO> callback) {
		SubmitAuthCommand command=(SubmitAuthCommand)cmd;
		UserAuthenticaVO vo=new UserAuthenticaVO();
		vo.setAcctBank(command.getBankName());
		vo.setAcctName(command.getAccountName());
		vo.setBankNum(command.getBankNum());
		vo.setBankPosition(command.getBankAddr());
		getRestService(StockUserResourceAsync.class,StockUserResource.class).userAttributeIdentify(vo).onResult(callback);
	}
}
