/**
 * 
 */
package com.wxxr.mobile.stock.app.service.loader;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.wxxr.mobile.core.async.api.IAsyncCallback;
import com.wxxr.mobile.stock.app.bean.VoucherBean;
import com.wxxr.mobile.stock.app.command.GetVoucherCommand;
import com.wxxr.mobile.stock.app.common.IReloadableEntityCache;
import com.wxxr.stock.crm.customizing.ejb.api.ActivityUserVo;
import com.wxxr.stock.restful.resource.StockUserResource;
import com.wxxr.stock.restful.resource.StockUserResourceAsync;

/**
 * @author wangyan
 *
 */
public class VoucherLoader extends AbstractEntityLoader<String, VoucherBean, ActivityUserVo, GetVoucherCommand> {


	/* (non-Javadoc)
	 * @see com.wxxr.mobile.stock.app.common.IEntityLoader#createCommand(java.util.Map)
	 */
	@Override
	public GetVoucherCommand createCommand(
			Map<String, Object> params) {
		
		return new GetVoucherCommand();
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.stock.app.common.IEntityLoader#handleCommandResult(java.util.List, com.wxxr.mobile.stock.app.common.IReloadableEntityCache)
	 */
	@Override
	public boolean handleCommandResult(GetVoucherCommand cmd,List<ActivityUserVo> result,
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
		return GetVoucherCommand.COMMAND_NAME;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.stock.app.service.loader.AbstractEntityLoader#executeCommand(com.wxxr.mobile.core.command.api.ICommand)
	 */
	@Override
	protected void executeCommand(GetVoucherCommand cmd, IAsyncCallback<List<ActivityUserVo>> callback) {
		getRestService(StockUserResourceAsync.class,StockUserResource.class).getActivityUser().
		onResult(new DelegateCallback<ActivityUserVo, List<ActivityUserVo>>(callback) {

			@Override
			protected List<ActivityUserVo> getTargetValue(ActivityUserVo vo) {
				if(vo != null){
					List<ActivityUserVo> list=new ArrayList<ActivityUserVo>();
					list.add(vo);
					return list;
				}
				return null;
			}
		});
	}

}

