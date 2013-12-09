/**
 * 
 */
package com.wxxr.mobile.stock.app.service.loader;

import java.util.List;
import java.util.Map;

import com.wxxr.mobile.core.command.api.ICommand;
import com.wxxr.mobile.stock.app.bean.UserAttributeBean;
import com.wxxr.mobile.stock.app.common.IReloadableEntityCache;
import com.wxxr.stock.crm.customizing.ejb.api.UserAttributeVO;
import com.wxxr.stock.hq.ejb.api.UserAttributeVOs;
import com.wxxr.stock.restful.resource.StockUserResource;

/**
 * @author wangyan
 *
 */
public class UserAttributeLoader extends AbstractEntityLoader<String, UserAttributeBean, UserAttributeVO>{

	private final static String COMMAND_NAME="GetUserAttributes";
	
	private static class GetUserAttributesCommand implements ICommand<List<UserAttributeVO>>{

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
		public Class<List<UserAttributeVO>> getResultType() {
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
	public ICommand<List<UserAttributeVO>> createCommand(
			Map<String, Object> params) {
		
		return new GetUserAttributesCommand();
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.stock.app.common.IEntityLoader#handleCommandResult(java.util.List, com.wxxr.mobile.stock.app.common.IReloadableEntityCache)
	 */
	@Override
	public boolean handleCommandResult(ICommand<?> cmd,List<UserAttributeVO> result,
			IReloadableEntityCache<String, UserAttributeBean> cache) {
		boolean updated = false;
		if(result!=null && !result.isEmpty()){
			for (UserAttributeVO vo : result) {
				UserAttributeBean bean=cache.getEntity(vo.getAttrName());
				if(bean == null) {
					bean = new UserAttributeBean();
					bean.setName(vo.getAttrName());
					cache.putEntity(bean.getName(), bean);
				}
				bean.setValue(vo.getAttrValue());
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
	protected List<UserAttributeVO> executeCommand(
			ICommand<List<UserAttributeVO>> command) throws Exception {
		UserAttributeVOs vos=getRestService(StockUserResource.class).getUserAttributes();
		return vos==null?null:vos.getUserAttributes();
	}

}
