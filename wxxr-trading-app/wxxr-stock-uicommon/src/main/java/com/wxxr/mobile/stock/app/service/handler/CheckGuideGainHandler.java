/**
 * 
 */
package com.wxxr.mobile.stock.app.service.handler;

import com.wxxr.mobile.core.async.api.IAsyncCallback;
import com.wxxr.mobile.core.command.annotation.NetworkConstraint;
import com.wxxr.mobile.core.command.annotation.SecurityConstraint;
import com.wxxr.mobile.stock.app.command.CheckGuideGainCommand;
import com.wxxr.stock.restful.resource.ITradingProtectedResource;
import com.wxxr.stock.restful.resource.ITradingProtectedResourceAsync;
import com.wxxr.stock.trading.ejb.api.GuideResultVO;

/**
 *
 */
@SecurityConstraint(allowRoles={})
@NetworkConstraint
public class CheckGuideGainHandler extends BasicCommandHandler<GuideResultVO, CheckGuideGainCommand>{


	@Override
	public void execute(CheckGuideGainCommand cmd,IAsyncCallback<GuideResultVO> callback) {
		getRestService(ITradingProtectedResourceAsync.class,ITradingProtectedResource.class).checkGuideGainAllow().onResult(callback);
	}
}
