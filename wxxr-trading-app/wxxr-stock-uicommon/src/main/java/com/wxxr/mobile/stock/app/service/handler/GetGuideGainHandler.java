/**
 * 
 */
package com.wxxr.mobile.stock.app.service.handler;

import com.wxxr.mobile.core.async.api.IAsyncCallback;
import com.wxxr.mobile.core.command.annotation.NetworkConstraint;
import com.wxxr.mobile.core.command.annotation.SecurityConstraint;
import com.wxxr.mobile.stock.app.command.GetGuideGainCommand;
import com.wxxr.stock.restful.resource.ITradingProtectedResource;
import com.wxxr.stock.restful.resource.ITradingProtectedResourceAsync;
import com.wxxr.stock.trading.ejb.api.StockResultVO;

/**
 *
 */
@SecurityConstraint(allowRoles={})
@NetworkConstraint
public class GetGuideGainHandler extends BasicCommandHandler<StockResultVO, GetGuideGainCommand>{


	@Override
	public void execute(GetGuideGainCommand cmd,IAsyncCallback<StockResultVO> callback) {
		getRestService(ITradingProtectedResourceAsync.class,ITradingProtectedResource.class).guideGain().onResult(callback);
	}
}
