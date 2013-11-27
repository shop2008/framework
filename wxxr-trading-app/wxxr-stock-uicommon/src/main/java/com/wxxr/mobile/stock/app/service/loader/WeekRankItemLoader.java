/**
 * 
 */
package com.wxxr.mobile.stock.app.service.loader;

import java.util.List;
import java.util.Map;

import com.wxxr.mobile.core.command.annotation.NetworkConstraint;
import com.wxxr.mobile.core.command.api.ICommand;
import com.wxxr.mobile.stock.app.bean.WeekRankBean;
import com.wxxr.mobile.stock.app.common.IReloadableEntityCache;
import com.wxxr.mobile.stock.app.utils.ConverterUtils;
import com.wxxr.stock.restful.resource.TradingResourse;
import com.wxxr.stock.trading.ejb.api.WeekRankVO;

/**
 * @author neillin
 *
 */
public class WeekRankItemLoader extends AbstractEntityLoader<String, WeekRankBean, WeekRankVO> {

	private static final String COMMAND_NAME = "GetWeekRankItems";
	
	@NetworkConstraint
	private static class GetWeekRankItemsCommand implements ICommand<List<WeekRankVO>> {

		@Override
		public String getCommandName() {
			return COMMAND_NAME;
		}

		@SuppressWarnings({ "rawtypes", "unchecked" })
		@Override
		public Class<List<WeekRankVO>> getResultType() {
			Class clazz = List.class;
			return clazz;
		}

		@Override
		public void validate() {
		}
		
	}
	
	@Override
	public ICommand<List<WeekRankVO>> createCommand(
			Map<String, Object> params) {
		return new GetWeekRankItemsCommand();
	}

	@Override
	public boolean handleCommandResult(List<WeekRankVO> result,
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
	protected List<WeekRankVO> executeCommand(ICommand<List<WeekRankVO>> command)
			throws Exception {
		return getRestService(TradingResourse.class).getWeekRank();
	}

	@Override
	protected String getCommandName() {
		return COMMAND_NAME;
	}


}
