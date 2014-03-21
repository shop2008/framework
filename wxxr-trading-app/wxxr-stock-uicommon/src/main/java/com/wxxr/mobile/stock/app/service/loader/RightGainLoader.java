/**
 * 
 */
package com.wxxr.mobile.stock.app.service.loader;

import java.util.List;
import java.util.Map;

import com.wxxr.mobile.core.async.api.IAsyncCallback;
import com.wxxr.mobile.stock.app.bean.GainBean;
import com.wxxr.mobile.stock.app.command.GetRightGainCommand;
import com.wxxr.mobile.stock.app.common.IReloadableEntityCache;
import com.wxxr.mobile.stock.app.utils.ConverterUtils;
import com.wxxr.stock.restful.resource.ITradingResource;
import com.wxxr.stock.restful.resource.ITradingResourceAsync;
import com.wxxr.stock.trading.ejb.api.GainVO;
import com.wxxr.stock.trading.ejb.api.GainVOs;

/**
 * @author wangyan
 *
 */
public class RightGainLoader extends AbstractEntityLoader<Long, GainBean, GainVO, GetRightGainCommand> {

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.stock.app.common.IEntityLoader#createCommand(java.util.Map)
	 */
	@Override
	public GetRightGainCommand createCommand(Map<String, Object> params) {
		GetRightGainCommand command=new GetRightGainCommand();
		if(params == null) {
			command.setStart(0);
			command.setLimit(20);
		} else {
			
			command.setStart((Integer)params.get("start"));
			command.setLimit((Integer)params.get("limit"));
		}
		return command;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.stock.app.common.IEntityLoader#handleCommandResult(java.util.List, com.wxxr.mobile.stock.app.common.IReloadableEntityCache)
	 */
	@Override
	public boolean handleCommandResult(GetRightGainCommand cmd,List<GainVO> result,
			IReloadableEntityCache<Long, GainBean> cache) {
		boolean updated = false;
		if(result!=null && !result.isEmpty()){
			for (GainVO vo : result) {
				Long accId = vo.getTradingAccountId();
				
				GainBean bean = cache.getEntity(accId);
				if(bean == null) {
					bean = new GainBean();
					ConverterUtils.fromVO(vo);
					cache.putEntity(accId, bean);
				}
				ConverterUtils.fromVO(vo, bean);
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
		return GetRightGainCommand.COMMAND_NAME;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.stock.app.service.loader.AbstractEntityLoader#executeCommand(com.wxxr.mobile.core.command.api.ICommand)
	 */
	@Override
	protected void executeCommand(GetRightGainCommand command, IAsyncCallback<List<GainVO>> callback) {
		GetRightGainCommand cmd=(GetRightGainCommand)command;
		getRestService(ITradingResourceAsync.class, ITradingResource.class).getTotalGain(cmd.getStart(), cmd.getLimit()).
		onResult(new DelegateCallback<GainVOs, List<GainVO>>(callback) {

			@Override
			protected List<GainVO> getTargetValue(GainVOs vos) {
				return vos==null?null:vos.getGains();
			}
		});
	}

}

