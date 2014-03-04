/**
 * 
 */
package com.wxxr.mobile.stock.app.service.handler;

import com.wxxr.javax.ws.rs.core.Response;
import com.wxxr.mobile.core.async.api.Async;
import com.wxxr.mobile.core.async.api.IAsyncCallback;
import com.wxxr.mobile.core.async.api.ICancellable;
import com.wxxr.mobile.stock.app.StockAppBizException;
import com.wxxr.stock.restful.resource.StockUserResource;
import com.wxxr.stock.restful.resource.StockUserResourceAsync;

/**
 * @author wangyan
 *
 */
public class SubmitPushMesasgeHandler extends BasicCommandHandler<Boolean,SubmitPushMesasgeCommand>{

	public static final String COMMAND_NAME="SubmitPushMesasgeCommand";
	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.command.api.ICommandHandler#execute(com.wxxr.mobile.core.command.api.ICommand)
	 */
	@Override
	public void execute(SubmitPushMesasgeCommand command, final IAsyncCallback<Boolean> callback) {
		final SubmitPushMesasgeCommand cmd=(SubmitPushMesasgeCommand) command;
		Async<Response> async = null;
		if(cmd.isBinding()){
			async = getRestService(StockUserResourceAsync.class,StockUserResource.class).bindApp();
		}else{
			async = getRestService(StockUserResourceAsync.class,StockUserResource.class).unbindApp();
		}
		async.onResult(new IAsyncCallback<Response>() {

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
