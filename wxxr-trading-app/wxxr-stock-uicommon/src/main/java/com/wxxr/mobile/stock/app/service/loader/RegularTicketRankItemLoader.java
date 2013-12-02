/**
 * 
 */
package com.wxxr.mobile.stock.app.service.loader;

import java.util.List;
import java.util.Map;

import com.wxxr.mobile.core.command.annotation.NetworkConstraint;
import com.wxxr.mobile.core.command.api.ICommand;
import com.wxxr.mobile.stock.app.bean.RegularTicketBean;
import com.wxxr.mobile.stock.app.common.IReloadableEntityCache;
import com.wxxr.mobile.stock.app.utils.ConverterUtils;
import com.wxxr.stock.restful.resource.ITradingResource;
import com.wxxr.stock.trading.ejb.api.RegularTicketVO;

/**
 * @author neillin
 *
 */
public class RegularTicketRankItemLoader extends AbstractEntityLoader<String, RegularTicketBean, RegularTicketVO> {

	private final static String COMMAND_NAME = "GetRegularTicketItems";
	
	@NetworkConstraint
	private static class GetRegularTicketItemsCommand implements ICommand<List<RegularTicketVO>> {

		@Override
		public String getCommandName() {
			return COMMAND_NAME;
		}

		@SuppressWarnings({ "rawtypes", "unchecked" })
		@Override
		public Class<List<RegularTicketVO>> getResultType() {
			Class clazz = List.class;
			return clazz;
		}

		@Override
		public void validate() {
		}
		
	}
	
	@Override
	public ICommand<List<RegularTicketVO>> createCommand(
			Map<String, Object> params) {
		return new GetRegularTicketItemsCommand();
	}

	@Override
	public boolean handleCommandResult(ICommand<?> cmd,List<RegularTicketVO> result,
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
	protected List<RegularTicketVO> executeCommand(ICommand<List<RegularTicketVO>> command)
			throws Exception {
		return getRestService(ITradingResource.class).getRegularTicketRank();
	}

	@Override
	protected String getCommandName() {
		return COMMAND_NAME;
	}


}
