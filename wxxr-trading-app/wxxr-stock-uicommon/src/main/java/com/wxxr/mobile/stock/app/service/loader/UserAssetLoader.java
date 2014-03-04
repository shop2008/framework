/**
 * 
 */
package com.wxxr.mobile.stock.app.service.loader;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.wxxr.mobile.core.async.api.IAsyncCallback;
import com.wxxr.mobile.core.microkernel.api.KUtils;
import com.wxxr.mobile.core.security.api.IUserIdentityManager;
import com.wxxr.mobile.stock.app.bean.UserAssetBean;
import com.wxxr.mobile.stock.app.command.GetUserAssetCommand;
import com.wxxr.mobile.stock.app.common.IReloadableEntityCache;
import com.wxxr.stock.restful.resource.ITradingProtectedResource;
import com.wxxr.stock.restful.resource.ITradingProtectedResourceAsync;
import com.wxxr.stock.trading.ejb.api.UserAssetVO;

/**
 * @author wangyan
 *
 */
public class UserAssetLoader extends AbstractEntityLoader<String, UserAssetBean, UserAssetVO, GetUserAssetCommand> {

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.stock.app.common.IEntityLoader#createCommand(java.util.Map)
	 */
	@Override
	public GetUserAssetCommand createCommand(Map<String, Object> params) {
		
		return new GetUserAssetCommand();
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.stock.app.common.IEntityLoader#handleCommandResult(java.util.List, com.wxxr.mobile.stock.app.common.IReloadableEntityCache)
	 */
	@Override
	public boolean handleCommandResult(GetUserAssetCommand cmd,List<UserAssetVO> result,
			IReloadableEntityCache<String, UserAssetBean> cache) {

		boolean updated = false;
		if (result != null && !result.isEmpty()) {
			for (UserAssetVO vo : result) {
				UserAssetBean bean = null;
				if (KUtils.getService(IUserIdentityManager.class).isUserAuthenticated()) {
					bean = cache.getEntity(KUtils.getService(IUserIdentityManager.class).getUserId());
					if (bean == null) {
						bean = new UserAssetBean();
						cache.putEntity(KUtils.getService(IUserIdentityManager.class).getUserId(), bean);
					}
					bean.setBalance(vo.getBal());
					bean.setFrozen(vo.getFrozen());
					bean.setUsableBal(vo.getUsableBal());
					updated = true;
				}
				
			}
		}
		return updated;

	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.stock.app.service.loader.AbstractEntityLoader#getCommandName()
	 */
	@Override
	protected String getCommandName() {
		return GetUserAssetCommand.COMMAND_NAME;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.stock.app.service.loader.AbstractEntityLoader#executeCommand(com.wxxr.mobile.core.command.api.ICommand)
	 */
	@Override
	protected void executeCommand(GetUserAssetCommand cmd, IAsyncCallback<List<UserAssetVO>> callback) {
		getRestService(ITradingProtectedResourceAsync.class,ITradingProtectedResource.class).getAcctUsable().
		onResult(new DelegateCallback<UserAssetVO, List<UserAssetVO>>(callback) {

			@Override
			protected List<UserAssetVO> getTargetValue(UserAssetVO vo) {
				List<UserAssetVO> list=null;
				if(vo != null){
					list = new ArrayList<UserAssetVO>();
					list.add(vo);
				}
				return list;
			}
		});
	}

}

