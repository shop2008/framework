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
import com.wxxr.mobile.stock.app.bean.ClientInfoBean;
import com.wxxr.mobile.stock.app.bean.UserAssetBean;
import com.wxxr.mobile.stock.app.common.IReloadableEntityCache;
import com.wxxr.mobile.stock.app.common.RestUtils;
import com.wxxr.stock.restful.json.ClientInfoVO;
import com.wxxr.stock.restful.resource.ClientResource;

/**
 * @author wangyan
 *
 */
public class UserClientInfoLoader extends AbstractEntityLoader<String, ClientInfoBean, ClientInfoVO> {

	private static final String COMMAND_NAME = "GetClientInfoCommand";

	@NetworkConstraint
	@SecurityConstraint(allowRoles={})
	private static class GetClientInfoCommand implements ICommand<List<ClientInfoVO>>{

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
		public Class<List<ClientInfoVO>> getResultType() {
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
	public ICommand<List<ClientInfoVO>> createCommand(Map<String, Object> params) {
		
		return new GetClientInfoCommand();
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.stock.app.common.IEntityLoader#handleCommandResult(java.util.List, com.wxxr.mobile.stock.app.common.IReloadableEntityCache)
	 */
	@Override
	public boolean handleCommandResult(ICommand<?> cmd,List<ClientInfoVO> result,
			IReloadableEntityCache<String, ClientInfoBean> cache) {

		boolean updated = false;
		if (result != null && !result.isEmpty()) {
			for (ClientInfoVO vo : result) {
				ClientInfoBean bean = cache.getEntity(ClientInfoBean.class.getCanonicalName());
				if (bean == null) {
					bean = new ClientInfoBean();
					cache.putEntity(UserAssetBean.class.getCanonicalName(), bean);
				}
				bean.setDescription(vo.getDescription());
				bean.setStatus(vo.getStatus());
				bean.setUrl(vo.getUrl());
				bean.setVersion(vo.getVersion());
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
		return COMMAND_NAME;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.stock.app.service.loader.AbstractEntityLoader#executeCommand(com.wxxr.mobile.core.command.api.ICommand)
	 */
	@Override
	protected List<ClientInfoVO> executeCommand(ICommand<List<ClientInfoVO>> command) throws Exception {
		ClientInfoVO vo =RestUtils.getRestService(ClientResource.class).getClientInfo();
		List<ClientInfoVO> list=new ArrayList<ClientInfoVO>();
		list.add(vo);
		return list;
	}

}
