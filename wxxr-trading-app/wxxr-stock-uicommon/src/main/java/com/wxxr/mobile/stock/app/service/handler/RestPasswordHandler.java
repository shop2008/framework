package com.wxxr.mobile.stock.app.service.handler;



import com.wxxr.javax.ws.rs.core.Response;
import com.wxxr.mobile.core.async.api.IAsyncCallback;
import com.wxxr.mobile.core.async.api.ICancellable;
import com.wxxr.mobile.stock.app.StockAppBizException;
import com.wxxr.stock.restful.resource.UserResource;
import com.wxxr.stock.restful.resource.UserResourceAsync;

public class RestPasswordHandler extends BasicCommandHandler<Object,RestPasswordCommand> {

	public static final String COMMAND_NAME = "RestPasswordCommand";

	@Override
	public void execute(RestPasswordCommand command, final IAsyncCallback<Object> callback) {
		getRestService(UserResourceAsync.class,UserResource.class).resetPassword(((RestPasswordCommand)command).getUserName()).
		onResult(new IAsyncCallback<Response>() {

			@Override
			public void success(Response result) {
				if(result.getStatus() == 200){
					callback.success(null);
				}else{
					callback.failed(new StockAppBizException("Http请求错误，代码："+result.getStatus()));
				}
			}

			@Override
			public void failed(Throwable cause) {
				callback.failed(cause);
			}

			@Override
			public void cancelled() {
				callback.cancelled();
			}

			@Override
			public void setCancellable(ICancellable cancellable) {
				callback.setCancellable(cancellable);
			}
		});
	}

}
