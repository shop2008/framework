/**
 * 
 */
package com.wxxr.mobile.stock.app.service.handler;

import java.util.List;

import com.wxxr.mobile.core.async.api.IAsyncCallback;
import com.wxxr.mobile.core.util.StringUtils;
import com.wxxr.stock.crm.customizing.ejb.api.UserAttributeVO;
import com.wxxr.stock.hq.ejb.api.UserAttributeVOs;
import com.wxxr.stock.restful.resource.StockUserResource;
import com.wxxr.stock.restful.resource.StockUserResourceAsync;

/**
 * @author wangyan
 *
 */
public class GetUserAuthSettingHandler extends BasicCommandHandler<Boolean,GetUserAuthSettingCommand> {

	
	/* (non-Javadoc)
	 * @see com.wxxr.mobile.core.command.api.ICommandHandler#execute(com.wxxr.mobile.core.command.api.ICommand)
	 */
	@Override
	public void execute(final GetUserAuthSettingCommand command,IAsyncCallback<Boolean> callback) {
		getRestService(StockUserResourceAsync.class,StockUserResource.class).getUserAttributes().
		onResult(new DelegateCallback<UserAttributeVOs, Boolean>(callback) {

			@Override
			protected Boolean getTargetValue(
					UserAttributeVOs vos) {
				if (vos!=null) {
					List<UserAttributeVO> result = vos.getUserAttributes();
					if(result!=null && !result.isEmpty()){
						for (UserAttributeVO vo : result) {
							if("ACCT_NAME".equals(vo.getAttrName())){
								return StringUtils.isNotBlank(vo.getAttrValue());
							}
						}
					}
				}
				return false;
			}
		});
	}
}

