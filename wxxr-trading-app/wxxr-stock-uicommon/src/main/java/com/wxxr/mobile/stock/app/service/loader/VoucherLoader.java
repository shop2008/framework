/**
 * 
 */
package com.wxxr.mobile.stock.app.service.loader;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.wxxr.mobile.core.command.api.ICommand;
import com.wxxr.mobile.stock.app.bean.VoucherBean;
import com.wxxr.mobile.stock.app.common.IReloadableEntityCache;
import com.wxxr.stock.crm.customizing.ejb.api.ActivityUserVo;
import com.wxxr.stock.restful.resource.StockUserResource;

/**
 * @author wangyan
 *
 */
public class VoucherLoader extends AbstractEntityLoader<String, VoucherBean, ActivityUserVo> {

	private static final String COMMAND_NAME = "GetVoucherCommand";

	private static class GetVoucherCommand implements ICommand<List<ActivityUserVo>>{

		/* (non-Javadoc)
		 * @see com.wxxr.mobile.core.command.api.ICommand#getCommandName()
		 */
		@Override
		public String getCommandName() {
			return COMMAND_NAME;
		}



		/* (non-Javadoc)
		 * @see com.wxxr.mobile.core.command.api.ICommand#validate()
		 */
		@Override
		public void validate() {
			// TODO Auto-generated method stub
			
		}



		/* (non-Javadoc)
		 * @see com.wxxr.mobile.core.command.api.ICommand#getResultType()
		 */
		@Override
		public Class<List<ActivityUserVo>> getResultType() {
			Class clazz=List.class;
			return clazz;
		}
		
	}
	/* (non-Javadoc)
	 * @see com.wxxr.mobile.stock.app.common.IEntityLoader#createCommand(java.util.Map)
	 */
	@Override
	public ICommand<List<ActivityUserVo>> createCommand(
			Map<String, Object> params) {
		
		return new GetVoucherCommand();
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.stock.app.common.IEntityLoader#handleCommandResult(java.util.List, com.wxxr.mobile.stock.app.common.IReloadableEntityCache)
	 */
	@Override
	public boolean handleCommandResult(ICommand<?> cmd,List<ActivityUserVo> result,
			IReloadableEntityCache<String, VoucherBean> cache) {
		boolean updated = false;
		if (result != null && !result.isEmpty()) {
			for (ActivityUserVo vo : result) {
				VoucherBean bean = cache.getEntity(VoucherBean.class.getCanonicalName());
				if (bean == null) {
					bean = new VoucherBean();
					cache.putEntity(VoucherBean.class.getCanonicalName(), bean);
				}
				bean.setBalance(vo.getVoucherVol());
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
		// TODO Auto-generated method stub
		return COMMAND_NAME;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.stock.app.service.loader.AbstractEntityLoader#executeCommand(com.wxxr.mobile.core.command.api.ICommand)
	 */
	@Override
	protected List<ActivityUserVo> executeCommand(
			ICommand<List<ActivityUserVo>> command) throws Exception {
		ActivityUserVo vo=getRestService(StockUserResource.class).getActivityUser();
		List<ActivityUserVo> list=new ArrayList<ActivityUserVo>();
		list.add(vo);
		return list;
	}

}
