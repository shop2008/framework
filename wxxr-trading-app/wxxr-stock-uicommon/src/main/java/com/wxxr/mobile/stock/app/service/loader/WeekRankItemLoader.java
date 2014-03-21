/**
 * 
 */
package com.wxxr.mobile.stock.app.service.loader;

import java.util.List;
import java.util.Map;

import com.wxxr.mobile.core.async.api.IAsyncCallback;
import com.wxxr.mobile.stock.app.bean.WeekRankBean;
import com.wxxr.mobile.stock.app.command.GetWeekRankItemsCommand;
import com.wxxr.mobile.stock.app.common.IReloadableEntityCache;
import com.wxxr.mobile.stock.app.utils.ConverterUtils;
import com.wxxr.stock.restful.resource.ITradingResource;
import com.wxxr.stock.restful.resource.ITradingResourceAsync;
import com.wxxr.stock.trading.ejb.api.WeekRankVO;
import com.wxxr.stock.trading.ejb.api.WeekRankVOs;

/**
 * @author neillin
 *
 */
public class WeekRankItemLoader extends AbstractEntityLoader<String, WeekRankBean, WeekRankVO, GetWeekRankItemsCommand> {

	
	@Override
	public GetWeekRankItemsCommand createCommand(
			Map<String, Object> params) {
		return new GetWeekRankItemsCommand();
	}

	@Override
	public boolean handleCommandResult(GetWeekRankItemsCommand cmd,List<WeekRankVO> result,
			IReloadableEntityCache<String, WeekRankBean> cache) {
		boolean updated = false;
		int rankNo = 1;
		for (WeekRankVO vo : result) {
			String id = vo.getUesrId();
			WeekRankBean bean = cache.getEntity(id);
			if(bean == null) {
				bean = ConverterUtils.fromVO(vo);
				bean.setRankSeq(rankNo++);
				cache.putEntity(id, bean);
				updated = true;
			}
		}
		return updated;
	}


	@Override
	protected void executeCommand(GetWeekRankItemsCommand cmd, IAsyncCallback<List<WeekRankVO>> callback) {
		getRestService(ITradingResourceAsync.class,ITradingResource.class).getWeekRank().
		onResult(new DelegateCallback<WeekRankVOs, List<WeekRankVO>>(callback) {

			@Override
			protected List<WeekRankVO> getTargetValue(WeekRankVOs vos) {
				return vos==null?null:vos.getWeekRanks();
			}
		});
	}

	@Override
	protected String getCommandName() {
		return GetWeekRankItemsCommand.COMMAND_NAME;
	}


}


