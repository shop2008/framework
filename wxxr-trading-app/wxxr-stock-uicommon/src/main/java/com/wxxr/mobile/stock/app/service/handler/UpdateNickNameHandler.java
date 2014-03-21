package com.wxxr.mobile.stock.app.service.handler;

import com.wxxr.mobile.core.async.api.IAsyncCallback;
import com.wxxr.security.vo.UserParamVO;
import com.wxxr.stock.common.valobject.ResultBaseVO;
import com.wxxr.stock.restful.resource.StockUserResource;
import com.wxxr.stock.restful.resource.StockUserResourceAsync;

public class UpdateNickNameHandler extends BasicCommandHandler<ResultBaseVO,UpdateNickNameCommand> {
	public final static String COMMAND_NAME="UpdateNickNameCommand";
	
	
	@Override
	public void execute(final UpdateNickNameCommand command, IAsyncCallback<ResultBaseVO> callback) {
		UserParamVO vo=new UserParamVO();
		vo.setNickName(command.getNickName());
		getRestService(StockUserResourceAsync.class,StockUserResource.class).updateNickName(vo).onResult(callback);
	}

}
