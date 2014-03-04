/**
 * 
 */
package com.wxxr.mobile.stock.app.service.loader;

import java.util.List;
import java.util.Map;

import com.wxxr.mobile.core.async.api.IAsyncCallback;
import com.wxxr.mobile.core.microkernel.api.KUtils;
import com.wxxr.mobile.core.security.api.IUserIdentityManager;
import com.wxxr.mobile.stock.app.command.GetUserAttributesCommand;
import com.wxxr.mobile.stock.app.common.IReloadableEntityCache;
import com.wxxr.mobile.stock.app.model.AuthInfo;
import com.wxxr.stock.crm.customizing.ejb.api.UserAttributeVO;
import com.wxxr.stock.hq.ejb.api.UserAttributeVOs;
import com.wxxr.stock.restful.resource.StockUserResource;
import com.wxxr.stock.restful.resource.StockUserResourceAsync;

/**
 * @author wangyan
 *
 */
public class UserAttributeLoader extends AbstractEntityLoader<String, AuthInfo, UserAttributeVO, GetUserAttributesCommand>{

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.stock.app.common.IEntityLoader#createCommand(java.util.Map)
	 */
	@Override
	public GetUserAttributesCommand createCommand(
			Map<String, Object> params) {
		
		return new GetUserAttributesCommand();
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.stock.app.common.IEntityLoader#handleCommandResult(java.util.List, com.wxxr.mobile.stock.app.common.IReloadableEntityCache)
	 */
	@Override
	public boolean handleCommandResult(GetUserAttributesCommand cmd,List<UserAttributeVO> result,
			IReloadableEntityCache<String, AuthInfo> cache) {
		boolean updated = false;
		if(result!=null && !result.isEmpty()){
			String key = KUtils.getService(IUserIdentityManager.class).getUserId();
			AuthInfo authinfo = cache.getEntity(key);
			if (authinfo==null) {
				authinfo = new AuthInfo();
				cache.putEntity(key, authinfo);
			}
			for (UserAttributeVO vo : result) {
				if("BANK_POSITION".equals(vo.getAttrName())){
					authinfo.setBankAddr(vo.getAttrValue());
				}else if("BANK_NUM".equals(vo.getAttrName())){
					authinfo.setBankNum(vo.getAttrValue());
				}else if("ACCT_NAME".equals(vo.getAttrName())){
					authinfo.setAccountName(vo.getAttrValue());
				}else if("ACCT_BANK".equals(vo.getAttrName())){
					authinfo.setBankName(vo.getAttrValue());
				}else if("CONFIRMED".equals(vo.getAttrName())){
					authinfo.setConfirmed(Boolean.valueOf(vo.getAttrValue()));
				}
				updated = true;
			}
		}
		return updated;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.stock.app.service.loader.AbstractEntityLoader#getCommandName()
	 */
	@Override
	protected String getCommandName() {
		return GetUserAttributesCommand.COMMAND_NAME;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.stock.app.service.loader.AbstractEntityLoader#executeCommand(com.wxxr.mobile.core.command.api.ICommand)
	 */
	@Override
	protected void executeCommand(GetUserAttributesCommand cmd, IAsyncCallback<List<UserAttributeVO>> callback) {
		getRestService(StockUserResourceAsync.class,StockUserResource.class).getUserAttributes().
		onResult(new DelegateCallback<UserAttributeVOs, List<UserAttributeVO>>(callback) {

			@Override
			protected List<UserAttributeVO> getTargetValue(
					UserAttributeVOs vos) {
				return vos==null?null:vos.getUserAttributes();
			}
		});
	}

}

