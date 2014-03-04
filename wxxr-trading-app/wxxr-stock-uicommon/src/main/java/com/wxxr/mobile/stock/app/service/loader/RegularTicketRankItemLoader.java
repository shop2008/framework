/**
 * 
 */
package com.wxxr.mobile.stock.app.service.loader;

import java.util.List;
import java.util.Map;

import com.wxxr.mobile.core.async.api.IAsyncCallback;
import com.wxxr.mobile.stock.app.bean.RegularTicketBean;
import com.wxxr.mobile.stock.app.command.GetRegularTicketItemsCommand;
import com.wxxr.mobile.stock.app.common.IReloadableEntityCache;
import com.wxxr.mobile.stock.app.utils.ConverterUtils;
import com.wxxr.stock.restful.resource.ITradingResource;
import com.wxxr.stock.restful.resource.ITradingResourceAsync;
import com.wxxr.stock.trading.ejb.api.RegularTicketVO;
import com.wxxr.stock.trading.ejb.api.RegularTicketVOs;

/**
 * @author neillin
 *
 */
public class RegularTicketRankItemLoader extends AbstractEntityLoader<String, RegularTicketBean, RegularTicketVO, GetRegularTicketItemsCommand> {
	@Override
	public GetRegularTicketItemsCommand createCommand(
			Map<String, Object> params) {
		return new GetRegularTicketItemsCommand();
	}

	@Override
	public boolean handleCommandResult(GetRegularTicketItemsCommand cmd,List<RegularTicketVO> result,
			IReloadableEntityCache<String, RegularTicketBean> cache) {
		boolean updated = false;
		int rankNo = 1;
		for (RegularTicketVO vo : result) {
			String id = vo.getNickName();
			RegularTicketBean bean = cache.getEntity(id);
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
	protected void executeCommand(GetRegularTicketItemsCommand cmd, IAsyncCallback<List<RegularTicketVO>> callback) {
		getRestService(ITradingResourceAsync.class, ITradingResource.class).getRegularTicketRank().
		onResult(new DelegateCallback<RegularTicketVOs, List<RegularTicketVO>>(callback) {

			@Override
			protected List<RegularTicketVO> getTargetValue(
					RegularTicketVOs vos) {
				return vos==null?null:vos.getRegularTickets();
			}
		});
	}

	@Override
	protected String getCommandName() {
		return GetRegularTicketItemsCommand.COMMAND_NAME;
	}


}


