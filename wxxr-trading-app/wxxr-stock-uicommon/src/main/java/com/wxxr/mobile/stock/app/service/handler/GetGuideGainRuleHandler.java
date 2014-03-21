/**
 * 
 */
package com.wxxr.mobile.stock.app.service.handler;

import com.wxxr.mobile.core.async.api.IAsyncCallback;
import com.wxxr.mobile.core.command.annotation.NetworkConstraint;
import com.wxxr.mobile.stock.app.command.GetGuideGainRuleCommand;
import com.wxxr.stock.restful.json.SimpleVO;
import com.wxxr.stock.restful.resource.ITradingResource;
import com.wxxr.stock.restful.resource.ITradingResourceAsync;

/**
 *
 */
@NetworkConstraint
public class GetGuideGainRuleHandler extends BasicCommandHandler<SimpleVO, GetGuideGainRuleCommand>{
	@Override
	public void execute(GetGuideGainRuleCommand cmd,IAsyncCallback<SimpleVO> callback) {
		getRestService(ITradingResourceAsync.class,ITradingResource.class).getGuideGainAmount().onResult(callback);
	}
}
