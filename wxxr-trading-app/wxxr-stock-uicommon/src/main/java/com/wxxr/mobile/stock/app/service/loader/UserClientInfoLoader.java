/**
 * 
 */
package com.wxxr.mobile.stock.app.service.loader;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.wxxr.mobile.core.async.api.IAsyncCallback;
import com.wxxr.mobile.stock.app.bean.ClientInfoBean;
import com.wxxr.mobile.stock.app.command.GetClientInfoCommand;
import com.wxxr.mobile.stock.app.common.IReloadableEntityCache;
import com.wxxr.stock.restful.json.ClientInfoVO;
import com.wxxr.stock.restful.resource.ClientResource;
import com.wxxr.stock.restful.resource.ClientResourceAsync;

/**
 * @author wangyan
 *
 */
public class UserClientInfoLoader extends AbstractEntityLoader<String, ClientInfoBean, ClientInfoVO, GetClientInfoCommand> {


	/* (non-Javadoc)
	 * @see com.wxxr.mobile.stock.app.common.IEntityLoader#createCommand(java.util.Map)
	 */
	@Override
	public GetClientInfoCommand createCommand(Map<String, Object> params) {
		
		return new GetClientInfoCommand();
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.stock.app.common.IEntityLoader#handleCommandResult(java.util.List, com.wxxr.mobile.stock.app.common.IReloadableEntityCache)
	 */
	@Override
	public boolean handleCommandResult(GetClientInfoCommand cmd,List<ClientInfoVO> result,
			IReloadableEntityCache<String, ClientInfoBean> cache) {

		boolean updated = false;
		if (result != null && !result.isEmpty()) {
			for (ClientInfoVO vo : result) {
				ClientInfoBean bean = cache.getEntity(ClientInfoBean.class.getCanonicalName());
				if (bean == null) {
					bean = new ClientInfoBean();
					cache.putEntity(ClientInfoBean.class.getCanonicalName(), bean);
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
		return GetClientInfoCommand.COMMAND_NAME;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.stock.app.service.loader.AbstractEntityLoader#executeCommand(com.wxxr.mobile.core.command.api.ICommand)
	 */
	@Override
	protected void executeCommand(GetClientInfoCommand cmd, IAsyncCallback<List<ClientInfoVO>> callback) {
		getRestService(ClientResourceAsync.class, ClientResource.class).getClientInfo().
		onResult(new DelegateCallback<ClientInfoVO, List<ClientInfoVO>>(callback) {

			@Override
			protected List<ClientInfoVO> getTargetValue(ClientInfoVO vo) {
				List<ClientInfoVO> list= null;
				if(vo != null) {
					list = new ArrayList<ClientInfoVO>();
					list.add(vo);
				}
				return list;
			}
		});
	}

}

