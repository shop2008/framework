/**
 * 
 */
package com.wxxr.mobile.stock.app.service.handler;

import com.wxxr.mobile.core.async.api.IAsyncCallback;
import com.wxxr.security.vo.UpdatePwdVO;
import com.wxxr.stock.common.valobject.ResultBaseVO;
import com.wxxr.stock.restful.resource.StockUserResource;
import com.wxxr.stock.restful.resource.StockUserResourceAsync;

/**
 * @author wangyan
 *
 */
public class UpPwdHandler extends BasicCommandHandler<ResultBaseVO,UpPwdCommand>{

	public final static String COMMAND_NAME="UpPwdCommand";

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.command.api.ICommandHandler#execute(com.wxxr.mobile.core.command.api.ICommand)
	 */
	@Override
	public void execute(final UpPwdCommand upCmd, IAsyncCallback<ResultBaseVO> callback) {
		UpdatePwdVO updatePwdvo=new UpdatePwdVO();
		updatePwdvo.setOldPwd(upCmd.getOldPwd());
		updatePwdvo.setPassword(upCmd.getNewPwd());
		getRestService(StockUserResourceAsync.class,StockUserResource.class).updatePwd(updatePwdvo).onResult(callback);
	}
}
