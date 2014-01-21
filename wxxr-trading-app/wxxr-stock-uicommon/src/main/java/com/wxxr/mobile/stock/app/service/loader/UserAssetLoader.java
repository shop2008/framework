/**
 * 
 */
package com.wxxr.mobile.stock.app.service.loader;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.wxxr.mobile.core.command.annotation.NetworkConstraint;
import com.wxxr.mobile.core.command.annotation.SecurityConstraint;
import com.wxxr.mobile.core.command.api.ICommand;
import com.wxxr.mobile.core.microkernel.api.KUtils;
import com.wxxr.mobile.core.security.api.IUserIdentityManager;
import com.wxxr.mobile.stock.app.bean.UserAssetBean;
import com.wxxr.mobile.stock.app.common.IReloadableEntityCache;
import com.wxxr.stock.restful.resource.ITradingProtectedResource;
import com.wxxr.stock.trading.ejb.api.UserAssetVO;

/**
 * @author wangyan
 *
 */
public class UserAssetLoader extends AbstractEntityLoader<String, UserAssetBean, UserAssetVO> {

	private static final String COMMAND_NAME = "GetUserAssetCommand";

	@NetworkConstraint
	@SecurityConstraint(allowRoles={})
	private static class GetUserAssetCommand implements ICommand<List<UserAssetVO>>{

		/* (non-Javadoc)
		 * @see com.wxxr.mobile.core.command.api.ICommand#getCommandName()
		 */
		@Override
		public String getCommandName() {
			return COMMAND_NAME;
		}

		/* (non-Javadoc)
		 * @see com.wxxr.mobile.core.command.api.ICommand#getResultType()
		 */
		@Override
		public Class<List<UserAssetVO>> getResultType() {
			Class clazz=List.class;
			return clazz;
		}

		/* (non-Javadoc)
		 * @see com.wxxr.mobile.core.command.api.ICommand#validate()
		 */
		@Override
		public void validate() {
			
		}
		
	}
	/* (non-Javadoc)
	 * @see com.wxxr.mobile.stock.app.common.IEntityLoader#createCommand(java.util.Map)
	 */
	@Override
	public ICommand<List<UserAssetVO>> createCommand(Map<String, Object> params) {
		
		return new GetUserAssetCommand();
	}
	@Override
	public boolean handleCommandResult(ICommand<?> cmd,List<UserAssetVO> result,
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
		return COMMAND_NAME;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.stock.app.service.loader.AbstractEntityLoader#executeCommand(com.wxxr.mobile.core.command.api.ICommand)
	 */
	@Override
	protected List<UserAssetVO> executeCommand(
			ICommand<List<UserAssetVO>> command) throws Exception {
		UserAssetVO vo=getRestService(ITradingProtectedResource.class).getAcctUsable();
		List<UserAssetVO> list=new ArrayList<UserAssetVO>();
		list.add(vo);
		return list;
	}

}
